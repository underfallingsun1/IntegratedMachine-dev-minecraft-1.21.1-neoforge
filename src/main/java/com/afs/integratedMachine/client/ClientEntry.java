package com.afs.integratedMachine.client;

import com.afs.integratedMachine.utils.Meta;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Meta.MODID, dist = Dist.CLIENT)
public class ClientEntry {
    public ClientEntry(ModContainer container){
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
