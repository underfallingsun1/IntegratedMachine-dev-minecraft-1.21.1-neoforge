package com.afs.integratedMachine.block.entity;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings({"unused", "null"})
public class IMBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BE_TYPES = DeferredRegister.create(
            Registries.BLOCK_ENTITY_TYPE, Meta.MODID
    );

    public static final Supplier<BlockEntityType<CompartmentControllerBlockEntity>> COMPARTMENT_CONTROLLER =
            BE_TYPES.register("compartment_controller", ()->
                    BlockEntityType.Builder.of(
                            CompartmentControllerBlockEntity::new,
                            IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get()
                            )
                            .build(null));

    public static final Supplier<BlockEntityType<ExperienceConverterBlockEntity>> EXPERIENCE_CONVERTER =
            BE_TYPES.register("experience_converter", ()->
                    BlockEntityType.Builder.of(
                            ExperienceConverterBlockEntity::new,
                            IMBlocks.EXPERIENCE_CONVERTER.get()
                            )
                            .build(null));
}
