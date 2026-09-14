package com.afs.integratedMachine;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.block.entity.IMBlockEntityTypes;
import com.afs.integratedMachine.common_registries.IMConfig;
import com.afs.integratedMachine.common_registries.IMDataAttachments;
import com.afs.integratedMachine.compartment.property.CompartmentProperty;
import com.afs.integratedMachine.compartment.property.CompartmentPropertyTypes;
import com.afs.integratedMachine.fluid.IMFluids;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.client.gui.menu.IMMenus;
import com.afs.integratedMachine.item.dataComponents.IMDataComponents;
import com.afs.integratedMachine.recipe.IMRecipes;
import com.afs.integratedMachine.utils.Meta;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@Mod(Meta.MODID)
@EventBusSubscriber(modid = Meta.MODID)
public class IntegratedMachine {
    public IntegratedMachine(IEventBus bus, ModContainer container){
        container.registerConfig(ModConfig.Type.STARTUP, IMConfig.CONFIG_SPEC_STARTUP);
        container.registerConfig(ModConfig.Type.SERVER, IMConfig.CONFIG_SPEC_SERVER);

        IMFluids.FLUID_TYPES.register(bus);
        IMFluids.FLUIDS.register(bus);

        IMItems.ITEMS.register(bus);
        IMItems.TABS.register(bus);
        IMBlocks.BLOCKS.register(bus);
        IMBlocks.TYPES.register(bus);
        IMBlockEntityTypes.BE_TYPES.register(bus);
        IMMenus.MENUS.register(bus);
        IMRecipes.RECIPES.register(bus);
        IMRecipes.SERIALIZERS.register(bus);
        IMDataAttachments.ATTACHMENT_TYPES.register(bus);
        IMDataComponents.COMPONENTS.register(bus);

        CompartmentPropertyTypes.COMPARTMENT_PROPERTY_TYPES.register(bus);

        Meta.LOGGER.info("mod integrated machine is loaded!");
    }

    @SubscribeEvent
    public static void addNewRegistries(NewRegistryEvent e){
        e.register(CompartmentProperty.COMPARTMENT_PROPERTY_TYPE_REGISTRY);
    }

    @SubscribeEvent
    public static void addNewDatapackRegistry(DataPackRegistryEvent.NewRegistry e){
        e.dataPackRegistry(CompartmentProperty.COMPARTMENT_PROPERTY_KEY, CompartmentProperty.CODEC, CompartmentProperty.CODEC);
    }
}
