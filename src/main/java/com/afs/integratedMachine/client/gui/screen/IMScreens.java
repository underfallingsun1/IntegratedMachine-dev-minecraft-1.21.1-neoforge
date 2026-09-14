package com.afs.integratedMachine.client.gui.screen;

import com.afs.integratedMachine.client.gui.menu.IMMenus;
import com.afs.integratedMachine.utils.Meta;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Meta.MODID, value = Dist.CLIENT)
public class IMScreens {
    @SubscribeEvent
    public static void registerMenuScreen(RegisterMenuScreensEvent e){
        e.register(IMMenus.EXPERIENCE_CONVERTER.get(), ExperienceConverterScreen::new);
    }
}
