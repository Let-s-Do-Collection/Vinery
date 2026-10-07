package net.satisfy.vinery.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record WineYearComponent(int brewedDay, int daysPerYear, int yearsPerEffectLevel, int startDuration, int durationPerYear, int maxDuration, int maxLevel, int storedDays, int storedSince, int bonusDays, int storageRate) {
    public static final Codec<WineYearComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("brewedDay", -1).forGetter(WineYearComponent::brewedDay),
            Codec.INT.optionalFieldOf("year", -1).forGetter(value -> 0),
            Codec.INT.optionalFieldOf("daysPerYear", 24).forGetter(WineYearComponent::daysPerYear),
            Codec.INT.optionalFieldOf("yearsPerEffectLevel", 6).forGetter(WineYearComponent::yearsPerEffectLevel),
            Codec.INT.optionalFieldOf("startDuration", 1800).forGetter(WineYearComponent::startDuration),
            Codec.INT.optionalFieldOf("durationPerYear", 200).forGetter(WineYearComponent::durationPerYear),
            Codec.INT.optionalFieldOf("maxDuration", 15000).forGetter(WineYearComponent::maxDuration),
            Codec.INT.optionalFieldOf("maxLevel", 5).forGetter(WineYearComponent::maxLevel),
            Codec.INT.optionalFieldOf("storedDays", 0).forGetter(WineYearComponent::storedDays),
            Codec.INT.optionalFieldOf("storedSince", -1).forGetter(WineYearComponent::storedSince),
            Codec.INT.optionalFieldOf("bonusDays", 0).forGetter(WineYearComponent::bonusDays),
            Codec.INT.optionalFieldOf("storageRate", 100).forGetter(WineYearComponent::storageRate)
    ).apply(instance, (brewedDayValue, legacyYearValue, daysPerYearValue, yearsPerEffectLevelValue, startDurationValue, durationPerYearValue, maxDurationValue, maxLevelValue, storedDaysValue, storedSinceValue, bonusDaysValue, storageRateValue) -> {
        int resolvedBrewedDay = brewedDayValue >= 0 ? brewedDayValue : Math.max(0, legacyYearValue) * Math.max(1, daysPerYearValue);
        return new WineYearComponent(resolvedBrewedDay, daysPerYearValue, yearsPerEffectLevelValue, startDurationValue, durationPerYearValue, maxDurationValue, maxLevelValue, storedDaysValue, storedSinceValue, bonusDaysValue, storageRateValue);
    }));

    public static final StreamCodec<RegistryFriendlyByteBuf, WineYearComponent> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeInt(value.brewedDay());
                buf.writeInt(value.daysPerYear());
                buf.writeInt(value.yearsPerEffectLevel());
                buf.writeInt(value.startDuration());
                buf.writeInt(value.durationPerYear());
                buf.writeInt(value.maxDuration());
                buf.writeInt(value.maxLevel());
                buf.writeInt(value.storedDays());
                buf.writeInt(value.storedSince());
                buf.writeInt(value.bonusDays());
                buf.writeInt(value.storageRate());
            },
            buf -> new WineYearComponent(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt())
    );

    public WineYearComponent withBrewedDay(int day) {
        return new WineYearComponent(day, daysPerYear, yearsPerEffectLevel, startDuration, durationPerYear, maxDuration, maxLevel, storedDays, storedSince, bonusDays, storageRate);
    }

    public WineYearComponent withStorage(int days, int bonus, int since, int rate) {
        return new WineYearComponent(brewedDay, daysPerYear, yearsPerEffectLevel, startDuration, durationPerYear, maxDuration, maxLevel, days, since, bonus, rate);
    }
}
