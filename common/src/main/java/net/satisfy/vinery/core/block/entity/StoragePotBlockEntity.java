package net.satisfy.vinery.core.block.entity;

import net.satisfy.foundation.block.CabinetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.registry.EntityTypeRegistry;

public class StoragePotBlockEntity extends CabinetBlockEntity {

    public StoragePotBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.STORAGE_POT_ENTITY.get(),pos, state);
    }

}
