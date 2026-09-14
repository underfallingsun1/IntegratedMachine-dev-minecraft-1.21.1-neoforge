package com.afs.integratedMachine.compartment.handle;

import com.afs.integratedMachine.compartment.Compartment;
import com.afs.integratedMachine.utils.Meta;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Meta.MODID, value = Dist.CLIENT)
public class ClientCompartmentHandle {
    private final Map<CompartmentList.Reference, Compartment> cache;
    public static ClientCompartmentHandle INSTANCE = null;

    private ClientCompartmentHandle(){
        cache = new HashMap<>();
    }

    public Compartment get(CompartmentList.Reference ref){
        if(!cache.containsKey(ref)){
            return Compartment.CLIENT_WAITING;
        }
        return cache.get(ref);
    }

    @SubscribeEvent
    public static void onConnect(ClientPlayerNetworkEvent.LoggingIn event){
        INSTANCE = new ClientCompartmentHandle();
    }
}
