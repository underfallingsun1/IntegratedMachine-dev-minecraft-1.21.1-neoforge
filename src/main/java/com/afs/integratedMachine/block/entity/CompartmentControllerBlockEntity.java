package com.afs.integratedMachine.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CompartmentControllerBlockEntity extends BlockEntity {
    @Override
    public void onLoad() {
    }

    public CompartmentControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(IMBlockEntityTypes.COMPARTMENT_CONTROLLER.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    }
}
