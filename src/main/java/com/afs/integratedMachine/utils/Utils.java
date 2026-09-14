package com.afs.integratedMachine.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static ResourceLocation modLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(Meta.MODID, path);
    }

    public static void addStack(List<ItemStack> stacks, ItemStack newStack){
        for(ItemStack stack:stacks){
            if(ItemStack.isSameItemSameComponents(stack, newStack)){
                stack.grow(newStack.getCount());
                return;
            }
        }
        stacks.add(newStack);
    }

    public static void applyItem(List<ItemStack> stacks, IItemHandler handler){
        for(ItemStack stack: stacks){
            for(int i = 0; i < handler.getSlots(); i++){
                stack = handler.insertItem(i, stack, false);
                if(stack.isEmpty()){
                    break;
                }
            }
            if(!stack.isEmpty()){
                Meta.LOGGER.warn("Item can not inject!Item:{}, Remain:{}", stack.getItem(), stack.getCount());
            }
        }
    }


    public static final Map<ResourceKey<Level>, Level> levelCache = new HashMap<>();

    public static Level getLevelByDimension(ResourceKey<Level> dimension){
        return levelCache.computeIfAbsent(dimension, dim -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                return server.getLevel(dim);
            }
            else{
                throw new IllegalArgumentException("can not run in client");
            }
        });
    }

    public static RegistryAccess getRegistry(Level level){
        if(level.isClientSide){
            var connection = Minecraft.getInstance().getConnection();
            if(connection == null){
                throw new IllegalArgumentException("can not link to server!");
            }
            return connection.registryAccess();
        }
        else{
            return level.registryAccess();
        }
    }

    public static boolean inServer(){
        if(FMLEnvironment.dist == Dist.DEDICATED_SERVER){
            return true;
        }
        return Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER;
    }

    public static int getXpInNextLevel(int l){
        if (l >= 30) {
            return 112 + (l - 30) * 9;
        } else {
            return l >= 15 ? 37 + (l - 15) * 5 : 7 + l * 2;
        }
    }

    public static int getTotalXpInLevel(int l){
        if (l <= 15) {
            return l * (l + 6);
        }
        else if (l <= 30){
            return l * (5 * l - 81) / 2 + 360;
        }
        else {
            return l * (9 * l - 325) / 2 + 2220;
        }
    }

    private static long totalXpForLevel(int level){
        if (level <= 0) {
            return 0L;
        }
        if (level <= 15) {
            return (long) level * (level + 6);
        }
        if (level <= 30) {
            return (long) level * (5L * level - 81) / 2 + 360;
        }
        return (long) level * (9L * level - 325) / 2 + 2220;
    }

    public static int getLevelForXp(int totalXp){
        if (totalXp <= 0) {
            return 0;
        }
        long xp = totalXp;
        int lo = 0;
        int hi = 1;
        while (totalXpForLevel(hi) <= xp) {
            lo = hi;
            hi <<= 1;
        }
        while (lo + 1 < hi) {
            int mid = (lo + hi) >>> 1;
            if (totalXpForLevel(mid) <= xp) {
                lo = mid;
            }
            else {
                hi = mid;
            }
        }
        return lo;
    }

    public static void setPlayerXp(Player player, int totalXp){
        totalXp = Mth.clamp(totalXp, 0, Integer.MAX_VALUE);
        int level = getLevelForXp(totalXp);
        int levelStart = (int) totalXpForLevel(level);
        int needed = getXpInNextLevel(level);
        player.increaseScore(totalXp - player.totalExperience);
        player.experienceLevel = level;
        player.totalExperience = totalXp;
        player.experienceProgress = needed <= 0 ? 0.0f : (float) (totalXp - levelStart) / needed;
    }

    public static Component getOnOffComponent(boolean on){
        return on?LangComps.ON.apply().withStyle(ChatFormatting.GREEN):
                LangComps.OFF.apply().withStyle(ChatFormatting.RED);
    }
}
