package com.afs.integratedMachine.dataMap;

import com.afs.integratedMachine.compartment.property.CompartmentBlockProperty;
import com.afs.integratedMachine.utils.Meta;
import com.afs.integratedMachine.utils.Utils;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = Meta.MODID)
public class IMDataMaps {

    @SubscribeEvent
    public static void register(RegisterDataMapTypesEvent e){
        e.register(CompartmentBlockProperty.COMPARTMENT_BLOCK_PROPERTIES);
    }
}
