package com.afs.integratedMachine.block;

import com.afs.integratedMachine.fluid.IMFluids;
import com.afs.integratedMachine.utils.Meta;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class IMBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Meta.MODID);

    public static final DeferredBlock<Block> TEST_BLOCK = BLOCKS.registerBlock(
            "test_block", TestBlock::new, BlockBehaviour.Properties.of().noLootTable()
    );

    public static final BlockBehaviour.Properties MACHINE_BLOCK_PROPERTY =
            BlockBehaviour.Properties.of().strength(4.0f, 8.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .instrument(NoteBlockInstrument.BASS)
                    .isValidSpawn((state, level, pos, type) -> false);

    public static final DeferredBlock<CompartmentControllerBlock> BASIC_COMPARTMENT_CONTROLLER =
            BLOCKS.registerBlock("basic_compartment_controller", p -> new CompartmentControllerBlock(p, 64, 8), MACHINE_BLOCK_PROPERTY);

    public static final DeferredBlock<Block> IRON_WALL = BLOCKS.registerBlock(
            "iron_wall", Block::new, MACHINE_BLOCK_PROPERTY
    );

    public static final DeferredBlock<ExperienceConverterBlock> EXPERIENCE_CONVERTER =
            BLOCKS.registerBlock("experience_converter", ExperienceConverterBlock::new, MACHINE_BLOCK_PROPERTY);

    public static final DeferredBlock<LiquidBlock> LIQUID_EXPERIENCE_BLOCK = BLOCKS.registerBlock(
            "liquid_experience",
            p -> new LiquidBlock(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(), p),
            BlockBehaviour.Properties.of()
                    .replaceable()
                    .noCollission()
                    .lightLevel(s -> 12)
                    .strength(100.0f)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
    );

    public static final DeferredBlock<Block> XP_LANTERN = BLOCKS.registerSimpleBlock(
            "experience_lantern", BlockBehaviour.Properties.of()
                    .strength(2.0f, 6.0f)
                    .mapColor(MapColor.COLOR_GREEN)
                    .instrument(NoteBlockInstrument.HAT)
                    .sound(SoundType.GLASS)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isValidSpawn(((state, level, pos, type) -> false))
                    .lightLevel(state -> 15)
    );

    public static final DeferredRegister<MapCodec<? extends Block>> TYPES = DeferredRegister.create(
            Registries.BLOCK_TYPE, Meta.MODID
    );

    public static final Supplier<MapCodec<CompartmentControllerBlock>> COMPARTMENT_CONTROLLER_BLOCK_TYPE =
            TYPES.register("compartment_controller", () -> CompartmentControllerBlock.CODEC);

    public static final Supplier<MapCodec<TestBlock>> TEST_BLOCK_TYPE = TYPES.register(
            "test_block", () -> Block.simpleCodec(TestBlock::new)
    );

    public static final Supplier<MapCodec<ExperienceConverterBlock>> EXPERIENCE_CONVERTER_BLOCK_TYPE =
            TYPES.register("experience_converter", () -> ExperienceConverterBlock.CODEC);
}
