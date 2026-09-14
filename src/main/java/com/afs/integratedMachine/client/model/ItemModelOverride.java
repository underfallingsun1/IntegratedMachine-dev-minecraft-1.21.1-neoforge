package com.afs.integratedMachine.client.model;

import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.item.XpGemItem;
import com.afs.integratedMachine.item.dataComponents.ClampedValue;
import com.afs.integratedMachine.item.dataComponents.IMDataComponents;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber
public class ItemModelOverride {
    @SubscribeEvent
    public static void registerItemOverride(FMLClientSetupEvent e){
        e.enqueueWork(() -> {
            ItemProperties.register(
                    IMItems.XP_GEM.get(),
                    XpGemItem.XP_GEM_PERCENTAGE,
                    (stack, level, player, seed) -> {
                        ClampedValue xp = stack.get(IMDataComponents.XP_GEM_CAPACITY);
                        if(xp == null){
                            return 0;
                        }
                        return ((float) xp.value()) / xp.max();
                    }
            );
        });
    }
}
