package com.afs.integratedMachine.common_registries;

import com.afs.integratedMachine.block.entity.IMBlockEntityTypes;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.item.utils.handles.XpGemFluidHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber
public class IMCapabilityProviders {
    @SubscribeEvent
    public static void RegisterCapabilities(RegisterCapabilitiesEvent e){
        e.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IMBlockEntityTypes.EXPERIENCE_CONVERTER.get(),
                (be, side) -> be.getFluidCapability());
        e.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IMBlockEntityTypes.EXPERIENCE_CONVERTER.get(),
                (be, side) -> be.getInventory());
        e.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> new XpGemFluidHandler(stack),
                IMItems.XP_GEM.get());
    }
}
