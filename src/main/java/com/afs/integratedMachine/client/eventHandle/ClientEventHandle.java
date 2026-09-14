package com.afs.integratedMachine.client.eventHandle;

import com.afs.integratedMachine.item.utils.SpecialLeftClickItem;
import com.afs.integratedMachine.network.payload.ClientNotice;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Meta.MODID, value = Dist.CLIENT)
public class ClientEventHandle {
    @SubscribeEvent
    public static void onInteract(InputEvent.InteractionKeyMappingTriggered e){
        if(e.isAttack() && Minecraft.getInstance().player.getItemInHand(e.getHand()).getItem() instanceof SpecialLeftClickItem){
            Minecraft.getInstance().getConnection().send(ClientNotice.LEFT_CLICK);
            e.setCanceled(true);
        }
    }
}
