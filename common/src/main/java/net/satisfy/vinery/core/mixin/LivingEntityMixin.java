package net.satisfy.vinery.core.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.core.registry.MobEffectRegistry;
import net.satisfy.vinery.core.item.FoodComponent;
import net.satisfy.vinery.core.wine.WineYears;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow public abstract boolean addEffect(MobEffectInstance mobEffectInstance);

	protected LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"))
	private void applyFoodEffects(Level level, ItemStack stack, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> ci) {
		if (stack.has(DataComponentRegistry.CUSTOM_FOOD.get())) {
			FoodComponent foodComponent = stack.get(DataComponentRegistry.CUSTOM_FOOD.get());
			if (foodComponent != null) {
				List<FoodProperties.PossibleEffect> list = foodComponent.getEffects();
				for (FoodProperties.PossibleEffect effect : list) {
					if (level.isClientSide || effect.effect() == null || !(level.random.nextFloat() < effect.probability())) continue;
					MobEffectInstance statusEffectInstance = new MobEffectInstance(effect.effect());
					statusEffectInstance.amplifier = WineYears.getEffectLevel(stack, level);
					if(statusEffectInstance.getEffect().equals(MobEffects.HEAL) || statusEffectInstance.getEffect().equals(MobEffects.HARM)){
						statusEffectInstance.duration = 1;
					}
					this.addEffect(statusEffectInstance);
				}
			}
		}
	}

	@Inject(method = "calculateFallDamage", at = @At("RETURN"), cancellable = true)
	public void modifyJumpBoostFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.hasEffect(MobEffectRegistry.getHolder(MobEffectRegistry.IMPROVED_JUMP_BOOST)) && !entity.hasEffect(MobEffects.JUMP)) {
			cir.setReturnValue(Math.max(0, cir.getReturnValue() - 1));
		}
	}

	@Inject(method = "getJumpBoostPower", at = @At(value = "HEAD"), cancellable = true)
	private void improvedJumpBoost(CallbackInfoReturnable<Float> cir) {
		MobEffectInstance effect = ((LivingEntity) (Object) this).getEffect(MobEffectRegistry.getHolder(MobEffectRegistry.IMPROVED_JUMP_BOOST));
		if (effect != null) {
			cir.setReturnValue(0.1F * (float)(effect.getAmplifier() + 1));
		}
	}
}