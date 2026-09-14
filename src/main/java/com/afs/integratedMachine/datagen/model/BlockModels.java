package com.afs.integratedMachine.datagen.model;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.block.utils.IMBlockStateProperties;
import com.afs.integratedMachine.client.model.connectModel.ConnectPredicate;
import com.afs.integratedMachine.datagen.model.builder.ConnectedModelBuilder;
import com.afs.integratedMachine.utils.Meta;
import com.afs.integratedMachine.utils.tags.IMBlockTags;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

public class BlockModels extends BlockStateProvider {
    public BlockModels(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Meta.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get(),
                cubeAll(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get()));
        simpleBlockWithItem(IMBlocks.TEST_BLOCK.get(), cubeAll(IMBlocks.TEST_BLOCK.get()));
        simpleBlockWithItem(IMBlocks.XP_LANTERN.get(), cubeAll(IMBlocks.XP_LANTERN.get()));

        simpleBlockItem(IMBlocks.IRON_WALL.get(), models().cubeAll("iron_wall_all", modBlockLoc("iron_wall_all")));
        simpleBlock(IMBlocks.IRON_WALL.get(), models().getBuilder("iron_wall")
                .texture("particle", modBlockLoc("iron_wall_all"))
                .texture("set", modBlockLoc("iron_wall"))
                .ao(true)
                .customLoader(ConnectedModelBuilder::new)
                .addPredicates(ConnectPredicate.ofBlock(IMBlocks.IRON_WALL.get(), Map.of()))
                .addPredicates(ConnectPredicate.ofTag(IMBlockTags.COMPARTMENT_INTERFACE, Map.of(IMBlockStateProperties.ACTIVE, true)))
                .setTextureSet("set")
                .setCornerCut(true)
                .end()
        );

        ModelFile converterModel = new ModelFile.UncheckedModelFile(modLoc("block/experience_converter"));
        directionalBlock(IMBlocks.EXPERIENCE_CONVERTER.get(), converterModel);
        simpleBlockItem(IMBlocks.EXPERIENCE_CONVERTER.get(), converterModel);
    }

    private ResourceLocation modBlockLoc(String path){
        return modLoc("block/" + path);
    }
}
