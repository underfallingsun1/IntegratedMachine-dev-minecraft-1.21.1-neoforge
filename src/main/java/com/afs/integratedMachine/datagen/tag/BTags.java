package com.afs.integratedMachine.datagen.tag;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.utils.Meta;
import com.afs.integratedMachine.utils.tags.IMBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BTags extends BlockTagsProvider {
    public BTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Meta.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(HolderLookup.Provider provider) {
        tag(IMBlockTags.COMPARTMENT_WALL).add(
                IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get(),
                IMBlocks.IRON_WALL.get());
        tag(IMBlockTags.COMPARTMENT_CONTROLLER).add(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IMBlocks.EXPERIENCE_CONVERTER.get());
        tag(IMBlockTags.COMPARTMENT_INTERFACE).addTags(IMBlockTags.COMPARTMENT_CONTROLLER);
    }
}
