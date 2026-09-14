package com.afs.integratedMachine.fluid;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.utils.LangComps;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@SuppressWarnings("unused")
public class IMFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
            NeoForgeRegistries.Keys.FLUID_TYPES, Meta.MODID
    );

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Meta.MODID);

    public static final DeferredHolder<FluidType, FluidType> LIQUID_EXPERIENCE_TYPE = FLUID_TYPES.register(
            "liquid_experience", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId(LangComps.LIQUID_EXPERIENCE_FLUID.key())
                    .density(200)
                    .viscosity(200)
                    .motionScale(0.007)
                    .canConvertToSource(false)
                    .canDrown(false)
                    .canSwim(true)
                    .canPushEntity(true)
                    .supportsBoating(true)
                    .lightLevel(12))
    );

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIQUID_EXPERIENCE_SOURCE = FLUIDS.register(
            "liquid_experience", () -> new BaseFlowingFluid.Source(FluidXpProperties())
    );

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> LIQUID_EXPERIENCE_FLOWING = FLUIDS.register(
            "flowing_liquid_experience", () -> new BaseFlowingFluid.Flowing(FluidXpProperties())
    );

    private static BaseFlowingFluid.Properties FluidXpProperties(){
        return new BaseFlowingFluid.Properties(
                LIQUID_EXPERIENCE_TYPE, LIQUID_EXPERIENCE_SOURCE, LIQUID_EXPERIENCE_FLOWING
        )
                .block(IMBlocks.LIQUID_EXPERIENCE_BLOCK)
                .bucket(IMItems.LIQUID_EXPERIENCE_BUCKET)
                .slopeFindDistance(1)
                .levelDecreasePerBlock(1)
                .tickRate(2)
                .explosionResistance(100.0f);
    }
}
