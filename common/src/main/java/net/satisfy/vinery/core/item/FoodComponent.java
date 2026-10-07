package net.satisfy.vinery.core.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class FoodComponent {
	private final int nutrition;
	private final float saturationModifier;
	private final boolean canAlwaysEat;
	private final float eatSeconds;
	private final Optional<ItemStack> usingConvertsTo;
	private final List<FoodProperties.PossibleEffect> effects;
	private final FoodProperties foodProperties;

	public static final Codec<FoodComponent> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ExtraCodecs.NON_NEGATIVE_INT.fieldOf("nutrition").forGetter(FoodComponent::nutrition),
					Codec.FLOAT.fieldOf("saturation").forGetter(FoodComponent::saturationModifier),
					Codec.BOOL.optionalFieldOf("can_always_eat", false).forGetter(FoodComponent::canAlwaysEat),
					ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("eat_seconds", 1.6F).forGetter(FoodComponent::eatSeconds),
					ItemStack.SINGLE_ITEM_CODEC.optionalFieldOf("using_converts_to").forGetter(FoodComponent::usingConvertsTo),
					FoodProperties.PossibleEffect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FoodComponent::getEffects)
			).apply(instance, FoodComponent::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, FoodComponent> DIRECT_STREAM_CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT, FoodComponent::nutrition,
					ByteBufCodecs.FLOAT, FoodComponent::saturationModifier,
					ByteBufCodecs.BOOL, FoodComponent::canAlwaysEat,
					ByteBufCodecs.FLOAT, FoodComponent::eatSeconds,
					ItemStack.STREAM_CODEC.apply(ByteBufCodecs::optional), FoodComponent::usingConvertsTo,
					FoodProperties.PossibleEffect.STREAM_CODEC.apply(ByteBufCodecs.list()), FoodComponent::getEffects,
					FoodComponent::new
			);

	public FoodComponent(int nutrition, float saturationModifier, boolean canAlwaysEat,
						 float eatSeconds, Optional<ItemStack> usingConvertsTo,
						 List<FoodProperties.PossibleEffect> effects) {
		this.nutrition = nutrition;
		this.saturationModifier = saturationModifier;
		this.canAlwaysEat = canAlwaysEat;
		this.eatSeconds = eatSeconds;
		this.usingConvertsTo = usingConvertsTo;
		this.effects = effects;

		FoodProperties.Builder builder = new FoodProperties.Builder()
				.nutrition(nutrition)
				.saturationModifier(saturationModifier);

		if (canAlwaysEat) {
			builder.alwaysEdible();
		}

		for (FoodProperties.PossibleEffect effect : effects) {
			builder.effect(effect.effect(), effect.probability());
		}

		this.foodProperties = builder.build();
	}

	public FoodComponent(List<FoodProperties.PossibleEffect> statusEffects) {
		this(1, 0.0f, true, 1.6F, Optional.empty(), statusEffects);
	}

	public FoodComponent(int nutrition, float saturationModifier, boolean canAlwaysEat,
						 boolean fastFood, boolean meat, List<FoodProperties.PossibleEffect> statusEffects) {
		this.nutrition = nutrition;
		this.saturationModifier = saturationModifier;
		this.canAlwaysEat = canAlwaysEat;
		this.eatSeconds = fastFood ? 0.8F : 1.6F;
		this.usingConvertsTo = Optional.empty();
		this.effects = statusEffects;

		FoodProperties.Builder builder = new FoodProperties.Builder()
				.nutrition(nutrition)
				.saturationModifier(saturationModifier);

		if (canAlwaysEat) {
			builder.alwaysEdible();
		}
		if (fastFood) {
			builder.fast();
		}

		for (FoodProperties.PossibleEffect effect : statusEffects) {
			builder.effect(effect.effect(), effect.probability());
		}

		this.foodProperties = builder.build();
	}

	public List<FoodProperties.PossibleEffect> getEffects() {
		return effects;
	}

	public FoodProperties getFoodProperties() {
		return foodProperties;
	}

	public int nutrition() {
		return nutrition;
	}

	public int getNutrition() {
		return nutrition;
	}

	public float saturationModifier() {
		return saturationModifier;
	}

	public float getSaturationModifier() {
		return saturationModifier;
	}

	public boolean canAlwaysEat() {
		return canAlwaysEat;
	}

	public float eatSeconds() {
		return eatSeconds;
	}

	public Optional<ItemStack> usingConvertsTo() {
		return usingConvertsTo;
	}

	public boolean isFastFood() {
		return eatSeconds < 1.6F;
	}
}