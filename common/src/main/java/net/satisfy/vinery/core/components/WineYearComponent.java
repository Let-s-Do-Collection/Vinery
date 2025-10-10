package net.satisfy.vinery.core.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record WineYearComponent(int year, int amplifier, int duration) {
    public static final Codec<WineYearComponent> CODEC = RecordCodecBuilder.create(inst->inst.group(
            Codec.INT.fieldOf("year").forGetter(WineYearComponent::year),
            Codec.INT.fieldOf("amplifier").forGetter(WineYearComponent::amplifier),
            Codec.INT.fieldOf("duration").forGetter(WineYearComponent::duration)
    ).apply(inst,WineYearComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WineYearComponent> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, WineYearComponent::year,
                    ByteBufCodecs.VAR_INT, WineYearComponent::amplifier,
                    ByteBufCodecs.VAR_INT, WineYearComponent::duration,
                    WineYearComponent::new
            );
}
