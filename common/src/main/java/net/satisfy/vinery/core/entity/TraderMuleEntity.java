package net.satisfy.vinery.core.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TraderMuleEntity extends AbstractChestedHorse {
	public TraderMuleEntity(EntityType<? extends TraderMuleEntity> entityType, Level level) {
		super(entityType, level);
	}
	private int despawnDelay = 47999;

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.0));
		this.goalSelector.addGoal(3, new PanicGoal(this, 1.0));
		this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
		this.goalSelector.addGoal(5, new TemptGoal(this, 1.0, Ingredient.of(Items.HAY_BLOCK), false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide) {
			this.maybeDespawn();
		}

	}

	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putInt("DespawnDelay", this.despawnDelay);
	}
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		if (compoundTag.contains("DespawnDelay", 99)) {
			this.despawnDelay = compoundTag.getInt("DespawnDelay");
		}
	}

	private void maybeDespawn() {
		if (this.canDespawn()) {
			this.despawnDelay = this.isLeashedToWanderingTrader() ? ((WanderingTrader) Objects.requireNonNull(this.getLeashHolder())).getDespawnDelay() - 1 : this.despawnDelay - 1;
			if (this.despawnDelay <= 0) {
				this.dropLeash(true, false);
				this.discard();
			}
		}
	}
	private boolean canDespawn() {
		return !this.isTamed() && !this.isLeashedToSomethingOtherThanTheWanderingTrader() && !this.hasExactlyOnePlayerPassenger();
	}
	private boolean isLeashedToWanderingTrader() {
		return this.getLeashHolder() instanceof WanderingWinemakerEntity;
	}
	private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
		return this.isLeashed() && !this.isLeashedToWanderingTrader();
	}

	@Override
	public TraderMuleEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
		return EntityTypeRegistry.MULE.get().create(this.level());
	}

	@Override
	protected SoundEvent getAmbientSound() {
		super.getAmbientSound();
		return SoundEvents.DONKEY_AMBIENT;
	}

	@Override
	protected SoundEvent getAngrySound() {
		super.getAngrySound();
		return SoundEvents.DONKEY_ANGRY;
	}

	@Override
	protected SoundEvent getDeathSound() {
		super.getDeathSound();
		return SoundEvents.DONKEY_DEATH;
	}

	@Override
	@Nullable
	protected SoundEvent getEatingSound() {
		return SoundEvents.DONKEY_EAT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		super.getHurtSound(source);
		return SoundEvents.DONKEY_HURT;
	}
}