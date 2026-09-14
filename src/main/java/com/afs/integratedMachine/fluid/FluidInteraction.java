package com.afs.integratedMachine.fluid;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;

@EventBusSubscriber(modid = Meta.MODID)
public class FluidInteraction {
    @SubscribeEvent
    public static void registerInteraction(FMLCommonSetupEvent e){
        e.enqueueWork(() -> {
            FluidInteractionRegistry.addInteraction(
                    IMFluids.LIQUID_EXPERIENCE_TYPE.get(),
                    new FluidInteractionRegistry.InteractionInformation(
                            Fluids.WATER.getFluidType(), Blocks.GREEN_STAINED_GLASS.defaultBlockState()
                    )
            );
            FluidInteractionRegistry.addInteraction(
                    IMFluids.LIQUID_EXPERIENCE_TYPE.get(),
                    new FluidInteractionRegistry.InteractionInformation(
                            Fluids.LAVA.getFluidType(), IMBlocks.XP_LANTERN.get().defaultBlockState()
                    )
            );
        });
    }
}
