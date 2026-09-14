package com.afs.integratedMachine.network.payload;

import com.afs.integratedMachine.network.server.HandleClientNotice;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class IMPayloads {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent e){
        PayloadRegistrar registrar = e.registrar("1");
        registrar.playToServer(
                ClientNotice.TYPE,
                ClientNotice.STREAM_CODEC,
                HandleClientNotice.INSTANCE
        );
    }
}
