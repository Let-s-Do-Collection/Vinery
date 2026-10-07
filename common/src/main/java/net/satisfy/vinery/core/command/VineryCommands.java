package net.satisfy.vinery.core.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.satisfy.vinery.core.block.GrapevinePotBlock;
import net.satisfy.vinery.core.block.entity.ApplePressBlockEntity;
import net.satisfy.vinery.core.block.entity.FermentationBarrelBlockEntity;
import net.satisfy.vinery.core.block.entity.GrapevinePotBlockEntity;
import net.satisfy.vinery.core.entity.TraderMuleEntity;
import net.satisfy.vinery.core.entity.WanderingWinemakerEntity;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.wine.GrapeType;
import net.satisfy.vinery.core.wine.JuiceUtil;
import net.satisfy.vinery.core.wine.WineEffects;
import net.satisfy.vinery.platform.PlatformHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class VineryCommands {
    private static final int MAX_GROW_STEPS = 64;

    @FunctionalInterface
    private interface BlockAction {
        int run(CommandContext<CommandSourceStack> ctx, BlockPos pos) throws CommandSyntaxException;
    }

    public static void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, selection) -> dispatcher.register(
                Commands.literal("vinery")
                        .then(Commands.literal("dump")
                                .then(Commands.literal("grapes").executes(ctx -> dumpGrapes(ctx.getSource())))
                                .then(Commands.literal("juices").executes(ctx -> dumpJuices(ctx.getSource())))
                                .then(Commands.literal("effects").executes(ctx -> dumpEffects(ctx.getSource()))))
                        .then(Commands.literal("grow").requires(source -> source.hasPermission(2))
                                .executes(ctx -> grow(ctx.getSource(), lookedAt(ctx.getSource()), 1))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(ctx -> grow(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), 1))
                                        .then(Commands.argument("steps", IntegerArgumentType.integer(1, MAX_GROW_STEPS))
                                                .executes(ctx -> grow(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), IntegerArgumentType.getInteger(ctx, "steps"))))))
                        .then(Commands.literal("barrel").requires(source -> source.hasPermission(2))
                                .then(atBlock(Commands.literal("finish"), (ctx, pos) -> finishBarrel(ctx.getSource(), pos))))
                        .then(Commands.literal("press").requires(source -> source.hasPermission(2))
                                .then(atBlock(Commands.literal("finish"), (ctx, pos) -> finishPress(ctx.getSource(), pos))))
                        .then(Commands.literal("pot").requires(source -> source.hasPermission(2))
                                .then(Commands.literal("fill")
                                        .then(atBlock(Commands.argument("grape", StringArgumentType.word()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(grapeIds(), builder)),
                                                (ctx, pos) -> fillPot(ctx.getSource(), pos, StringArgumentType.getString(ctx, "grape"))))))
                        .then(Commands.literal("winemaker").requires(source -> source.hasPermission(2))
                                .then(Commands.literal("spawn")
                                        .executes(ctx -> spawnWinemaker(ctx.getSource(), BlockPos.containing(ctx.getSource().getPosition())))
                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                .executes(ctx -> spawnWinemaker(ctx.getSource(), BlockPosArgument.getSpawnablePos(ctx, "pos")))))
                                .then(Commands.literal("locate").executes(ctx -> locateWinemakers(ctx.getSource()))))
                        .then(Commands.literal("effect").requires(source -> source.hasPermission(2))
                                .then(Commands.argument("wine", ResourceLocationArgument.id()).suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(wineIds(), builder))
                                        .executes(ctx -> giveEffect(ctx, List.of(ctx.getSource().getPlayerOrException()), -1))
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes(ctx -> giveEffect(ctx, EntityArgument.getPlayers(ctx, "targets"), -1))
                                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 256))
                                                        .executes(ctx -> giveEffect(ctx, EntityArgument.getPlayers(ctx, "targets"), IntegerArgumentType.getInteger(ctx, "level") - 1))))))
                        .then(Commands.literal("reload").requires(source -> source.hasPermission(2))
                                .executes(ctx -> reload(ctx.getSource())))
        ));
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T atBlock(T node, BlockAction action) {
        return node
                .executes(ctx -> withPos(ctx, lookedAt(ctx.getSource()), action))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(ctx -> withPos(ctx, BlockPosArgument.getLoadedBlockPos(ctx, "pos"), action)));
    }

    private static int withPos(CommandContext<CommandSourceStack> ctx, BlockPos pos, BlockAction action) throws CommandSyntaxException {
        if (pos == null) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("command.vinery.no_block", "Look at a block or pass a position"));
            return 0;
        }
        return action.run(ctx, pos);
    }

    private static int grow(CommandSourceStack source, BlockPos pos, int steps) {
        if (pos == null) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.no_block", "Look at a block or pass a position"));
            return 0;
        }
        ServerLevel level = source.getLevel();
        int grown = 0;
        for (int i = 0; i < steps; i++) {
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof BonemealableBlock block) || !block.isValidBonemealTarget(level, pos, state)) {
                break;
            }
            block.performBonemeal(level, level.random, pos, state);
            grown++;
        }
        if (grown == 0) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.grow.cannot", "This block can't grow any further"));
            return 0;
        }
        int result = grown;
        source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.grow.done", "Grew %s step(s)", result), true);
        return grown;
    }

    private static int finishBarrel(CommandSourceStack source, BlockPos pos) {
        if (!(source.getLevel().getBlockEntity(pos) instanceof FermentationBarrelBlockEntity barrel)) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.barrel.not_barrel", "That isn't a Fermentation Barrel"));
            return 0;
        }
        if (!barrel.finishFermentation()) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.barrel.idle", "Nothing is fermenting in this barrel"));
            return 0;
        }
        source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.barrel.finished", "Fermentation finishes next tick"), true);
        return 1;
    }

    private static int finishPress(CommandSourceStack source, BlockPos pos) {
        if (!(source.getLevel().getBlockEntity(pos) instanceof ApplePressBlockEntity press)) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.press.not_press", "That isn't an Apple Press"));
            return 0;
        }
        if (!press.finishProcessing()) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.press.idle", "The Apple Press is empty"));
            return 0;
        }
        source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.press.finished", "Apple Press finishes next tick"), true);
        return 1;
    }

    private static int fillPot(CommandSourceStack source, BlockPos pos, String grapeId) {
        ServerLevel level = source.getLevel();
        BlockState state = level.getBlockState(pos);
        GrapeType type = GrapeType.fromString(grapeId);
        if (type == null || type.equals(GrapeTypeRegistry.NONE)) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.pot.unknown_grape", "Unknown grape type: %s", grapeId));
            return 0;
        }
        if (!(state.getBlock() instanceof GrapevinePotBlock) || !(level.getBlockEntity(pos) instanceof GrapevinePotBlockEntity pot)) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.pot.not_pot", "That isn't a Grapevine Pot"));
            return 0;
        }
        int progress = GrapevinePotBlockEntity.pointsNeeded() - GrapevinePotBlockEntity.POINTS_PER_STOMP;
        int stage = GrapevinePotBlock.FULL_STAGE + progress * (GrapevinePotBlock.MAX_STAGE - GrapevinePotBlock.FULL_STAGE) / GrapevinePotBlockEntity.pointsNeeded();
        level.setBlockAndUpdate(pos, state
                .setValue(GrapevinePotBlock.GRAPEVINE_TYPE, type)
                .setValue(GrapevinePotBlock.STORAGE, GrapevinePotBlock.MAX_GRAPES)
                .setValue(GrapevinePotBlock.STAGE, stage));
        pot.setProgress(progress);
        source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.pot.filled", "Filled with %s, one jump left", grapeId), true);
        return 1;
    }

    private static int spawnWinemaker(CommandSourceStack source, BlockPos pos) {
        ServerLevel level = source.getLevel();
        WanderingWinemakerEntity winemaker = EntityTypeRegistry.WANDERING_WINEMAKER.get().spawn(level, pos, MobSpawnType.COMMAND);
        if (winemaker == null) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.winemaker.failed", "Couldn't spawn the Wandering Winemaker here"));
            return 0;
        }
        winemaker.setDespawnDelay(PlatformHelper.getTraderSpawnDelay());
        if (PlatformHelper.shouldSpawnWithMules()) {
            for (int i = 0; i < 2; i++) {
                TraderMuleEntity mule = EntityTypeRegistry.MULE.get().spawn(level, pos, MobSpawnType.COMMAND);
                if (mule != null) {
                    mule.setLeashedTo(winemaker, true);
                }
            }
        }
        source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.winemaker.spawned", "Spawned the Wandering Winemaker at %s", formatPos(pos)), true);
        return 1;
    }

    private static int locateWinemakers(CommandSourceStack source) {
        int found = 0;
        for (ServerLevel level : source.getServer().getAllLevels()) {
            for (WanderingWinemakerEntity winemaker : level.getEntities(EntityTypeRegistry.WANDERING_WINEMAKER.get(), entity -> entity.isAlive())) {
                BlockPos pos = winemaker.blockPosition();
                String dimension = level.dimension().location().toString();
                MutableComponent where = ComponentUtils.wrapInSquareBrackets(Component.literal(formatPos(pos) + " " + dimension)).withStyle(style -> style
                        .withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/execute in " + dimension + " run tp @s " + pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, trades(winemaker))));
                int minutes = winemaker.getDespawnDelay() / 1200;
                source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.winemaker.found", "Wandering Winemaker %s, leaves in %s min", where, minutes), false);
                found++;
            }
        }
        if (found == 0) {
            source.sendFailure(Component.translatableWithFallback("command.vinery.winemaker.none", "No Wandering Winemaker is loaded right now"));
        }
        return found;
    }

    private static Component trades(WanderingWinemakerEntity winemaker) {
        MutableComponent text = Component.translatableWithFallback("command.vinery.winemaker.trades", "Trades:");
        for (MerchantOffer offer : winemaker.getOffers()) {
            text.append("\n").append(offer.getResult().getCount() + "x ").append(offer.getResult().getHoverName())
                    .append(Component.literal(" ← " + offer.getCostA().getCount() + "x ").append(offer.getCostA().getHoverName()).withStyle(ChatFormatting.GRAY));
        }
        return text;
    }

    private static int giveEffect(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> players, int amplifier) {
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "wine");
        Item wine = BuiltInRegistries.ITEM.getOptional(id).orElse(null);
        WineEffects.WineEffect effect = wine == null ? null : WineEffects.get(wine);
        if (effect == null) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("command.vinery.effect.unknown", "%s has no wine effect", id.toString()));
            return 0;
        }
        int resolved = amplifier >= 0 ? amplifier : effect.amplifier();
        for (ServerPlayer player : players) {
            player.addEffect(new MobEffectInstance(effect.effect(), effect.duration(), resolved), ctx.getSource().getEntity());
        }
        int count = players.size();
        ctx.getSource().sendSuccess(() -> Component.translatableWithFallback("command.vinery.effect.given", "Applied the effect of %s to %s player(s)", wine.getDefaultInstance().getHoverName(), count), true);
        return count;
    }

    private static int reload(CommandSourceStack source) {
        if (!PlatformHelper.reloadConfig()) {
            source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.reload.automatic", "NeoForge reloads the Vinery config on its own when the file changes"), false);
            return 0;
        }
        source.sendSuccess(() -> Component.translatableWithFallback("command.vinery.reload.done", "Reloaded the Vinery config"), true);
        return 1;
    }

    private static int dumpGrapes(CommandSourceStack source) {
        List<GrapeType> types = new ArrayList<>(GrapeTypeRegistry.GRAPE_TYPE_TYPES);
        types.remove(GrapeTypeRegistry.NONE);
        types.sort(Comparator.comparing(GrapeType::getId));
        header(source, "command.vinery.dump.grapes", "%s grape types", types.size());
        for (GrapeType type : types) {
            String line = type.getId() + " (" + (type.isRed() ? "red" : "white") + (type.isLattice() ? ", lattice" : "") + ") fruit=" + key(type.getFruit()) + " seeds=" + key(type.getSeeds()) + " bottle=" + key(type.getBottle());
            source.sendSuccess(() -> Component.literal(line), false);
        }
        return types.size();
    }

    private static int dumpJuices(CommandSourceStack source) {
        List<String> juices = JuiceUtil.types();
        header(source, "command.vinery.dump.juices", "%s juice types", juices.size());
        for (String juice : juices) {
            String line = juice + " → " + key(JuiceUtil.juiceItem(juice));
            source.sendSuccess(() -> Component.literal(line), false);
        }
        return juices.size();
    }

    private static int dumpEffects(CommandSourceStack source) {
        List<ResourceLocation> wines = wineIds();
        header(source, "command.vinery.dump.effects", "%s wines with an effect", wines.size());
        for (ResourceLocation id : wines) {
            WineEffects.WineEffect effect = WineEffects.get(BuiltInRegistries.ITEM.get(id));
            String line = id + " → " + effect.effect().unwrapKey().map(key -> key.location().toString()).orElse("?") + " " + (effect.amplifier() + 1) + ", " + effect.duration() + " ticks";
            source.sendSuccess(() -> Component.literal(line), false);
        }
        return wines.size();
    }

    private static void header(CommandSourceStack source, String key, String fallback, int count) {
        source.sendSuccess(() -> Component.translatableWithFallback(key, fallback, count).withStyle(ChatFormatting.GOLD), false);
    }

    private static List<String> grapeIds() {
        return GrapeTypeRegistry.GRAPE_TYPE_TYPES.stream().filter(type -> !type.equals(GrapeTypeRegistry.NONE)).map(GrapeType::getId).sorted().toList();
    }

    private static List<ResourceLocation> wineIds() {
        return BuiltInRegistries.ITEM.stream().filter(item -> WineEffects.get(item) != null).map(BuiltInRegistries.ITEM::getKey).sorted().toList();
    }

    private static String key(Item item) {
        return item == null ? "-" : BuiltInRegistries.ITEM.getKey(item).toString();
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

    private static BlockPos lookedAt(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        HitResult hit = player.pick(player.blockInteractionRange(), 0.0F, false);
        return hit instanceof BlockHitResult blockHit && hit.getType() == HitResult.Type.BLOCK ? blockHit.getBlockPos() : null;
    }
}
