package com.afs.integratedMachine.datagen.lootTables;

import com.afs.integratedMachine.block.IMBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class BlockLootTables extends BlockLootSubProvider {
    public BlockLootTables(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return IMBlocks.BLOCKS.getEntries().stream().map(v -> (Block)v.get()).toList();
    }

    @Override
    protected void generate() {
        dropSelf(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get());
        dropSelf(IMBlocks.IRON_WALL.get());
        dropSelf(IMBlocks.EXPERIENCE_CONVERTER.get());
        add(IMBlocks.XP_LANTERN.get(), createSilkTouchOnlyTable(IMBlocks.XP_LANTERN));
    }
}
