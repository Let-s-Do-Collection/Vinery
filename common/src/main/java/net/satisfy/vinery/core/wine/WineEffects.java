package net.satisfy.vinery.core.wine;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.satisfy.vinery.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineEffects {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final List<String> DEFAULTS = List.of(
            "vinery:apple_cider|minecraft:strength|0|1800",
            "vinery:apple_wine|minecraft:resistance|0|1800",
            "vinery:mead|minecraft:haste|0|1800",
            "vinery:glowing_wine|minecraft:glowing|0|1800",
            "vinery:solaris_wine|minecraft:health_boost|0|1800",
            "vinery:kelp_cider|vinery:water_walker|0|1800",
            "vinery:eiswein|vinery:frosty_armor|0|1800",
            "vinery:aegis_wine|vinery:armor_effect|0|1800",
            "vinery:villagers_fright|minecraft:bad_omen|0|1800",
            "vinery:clark_wine|vinery:double_jump|0|1800",
            "vinery:jellie_wine|vinery:jellie|0|1800",
            "vinery:noir_wine|minecraft:jump_boost|0|1800",
            "vinery:red_wine|minecraft:slow_falling|0|1800",
            "vinery:strad_wine|minecraft:night_vision|0|1800",
            "vinery:cherry_wine|minecraft:invisibility|0|1800",
            "vinery:cristel_wine|minecraft:water_breathing|0|1800",
            "vinery:lilitu_wine|vinery:party_effect|0|1800",
            "vinery:jo_special_mixture|vinery:climbing_effect|0|1800",
            "vinery:bolvar_wine|vinery:lava_walker|0|1800",
            "vinery:magnetic_wine|vinery:magnet|0|1800",
            "vinery:stal_wine|vinery:health_effect|0|1800",
            "vinery:chenet_wine|vinery:climbing_effect|0|1800",
            "vinery:bottle_mojang_noir|vinery:experience_effect|0|1800",
            "vinery:chorus_wine|vinery:teleport|0|10",
            "vinery:creepers_crush|vinery:creeper_effect|0|100",
            "vinery:mellohi_wine|minecraft:instant_health|0|1"
    );

    public record WineEffect(Holder<MobEffect> effect, int amplifier, int duration) {
    }

    private static List<? extends String> cachedSource;
    private static Map<ResourceLocation, WineEffect> cache = Map.of();

    public static @Nullable WineEffect get(Item item) {
        List<? extends String> source = PlatformHelper.getWineEffects();
        if (source != cachedSource) {
            cache = parse(source);
            cachedSource = source;
        }
        return cache.get(BuiltInRegistries.ITEM.getKey(item));
    }

    private static Map<ResourceLocation, WineEffect> parse(List<? extends String> entries) {
        Map<ResourceLocation, WineEffect> result = new HashMap<>();
        for (String entry : entries) {
            String[] parts = entry.split("\\|");
            ResourceLocation wine = parts.length == 4 ? ResourceLocation.tryParse(parts[0].trim()) : null;
            ResourceLocation effectId = parts.length == 4 ? ResourceLocation.tryParse(parts[1].trim()) : null;
            Holder<MobEffect> effect = effectId == null ? null : BuiltInRegistries.MOB_EFFECT.getHolder(effectId).orElse(null);
            if (wine == null || effect == null) {
                LOGGER.warn("Ignoring invalid wine effect entry '{}', expected wine|effect|amplifier|durationTicks", entry);
                continue;
            }
            try {
                result.put(wine, new WineEffect(effect, Math.max(0, Integer.parseInt(parts[2].trim())), Math.max(1, Integer.parseInt(parts[3].trim()))));
            } catch (NumberFormatException e) {
                LOGGER.warn("Ignoring wine effect entry '{}': amplifier and duration must be numbers", entry);
            }
        }
        return result;
    }
}
