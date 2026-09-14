package com.afs.integratedMachine.client.model;

import com.afs.integratedMachine.client.model.connectModel.ConnectedModelLoader;
import com.afs.integratedMachine.utils.Meta;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = Meta.MODID)
public class Registry {
    @SubscribeEvent
    public static void registerLoader(ModelEvent.RegisterGeometryLoaders e){
        e.register(ConnectedModelLoader.ID, ConnectedModelLoader.INSTANCE);
    }
}
