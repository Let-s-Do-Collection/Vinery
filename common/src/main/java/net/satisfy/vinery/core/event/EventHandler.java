package net.satisfy.vinery.core.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.satisfy.vinery.core.registry.MobEffectRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EventHandler {
    public static EventResult onAttack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (!(target instanceof LivingEntity entity) || !player.hasEffect(MobEffectRegistry.getHolder(MobEffectRegistry.PARTY_EFFECT))) {
            return EventResult.pass();
        }

        if (!level.isClientSide()) {
            int color = level.random.nextInt(0xFFFFFF);
            ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);
            fireworkStack.set(DataComponents.FIREWORKS, new Fireworks(0, List.of(
                    new FireworkExplosion(FireworkExplosion.Shape.SMALL_BALL, IntList.of(color), IntList.of(), false, false))));
            FireworkRocketEntity fireworkRocket = new FireworkRocketEntity(level, fireworkStack, entity);
            fireworkRocket.setAirSupply(0);
            level.addFreshEntity(fireworkRocket);
        }
        if (!(target instanceof Player || target instanceof Mob)) {
            return EventResult.interruptTrue();
        }
        return EventResult.pass();
    }

    public static void init() {
        PlayerEvent.ATTACK_ENTITY.register(EventHandler::onAttack);
    }
}
