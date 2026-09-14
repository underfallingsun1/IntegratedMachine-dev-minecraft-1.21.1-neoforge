package com.afs.integratedMachine.client.gui.menu;

import com.afs.integratedMachine.utils.Meta;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IMMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(
            Registries.MENU, Meta.MODID
    );

    public static final DeferredHolder<MenuType<?>, MenuType<ExperienceConverterMenu>> EXPERIENCE_CONVERTER =
            MENUS.register("experience_converter", () -> IMenuTypeExtension.create(ExperienceConverterMenu::new));

}
