package com.afs.integratedMachine.item.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public interface SpecialLeftClickItem {
    void onLeftClick(Level level, Player player, ItemStack stack);
}
