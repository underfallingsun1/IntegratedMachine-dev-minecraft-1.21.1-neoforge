package com.afs.integratedMachine.item;

import com.afs.integratedMachine.item.dataComponents.ClampedValue;
import com.afs.integratedMachine.item.dataComponents.IMDataComponents;
import com.afs.integratedMachine.item.dataComponents.XpLevelSteps;
import com.afs.integratedMachine.item.utils.SpecialLeftClickItem;
import com.afs.integratedMachine.utils.LangComps;
import com.afs.integratedMachine.utils.Meta;
import com.afs.integratedMachine.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

import java.util.List;

@EventBusSubscriber(modid = Meta.MODID)
public class XpGemItem extends Item implements SpecialLeftClickItem {
    public static ModConfigSpec.IntValue XP_GEM_CAPACITY;

    public static final ResourceLocation XP_GEM_PERCENTAGE = Utils.modLoc("percentage");

    public XpGemItem(Properties properties) {
        super(properties
                .component(IMDataComponents.XP_GEM_CAPACITY, new ClampedValue(0, XP_GEM_CAPACITY.get(), 0))
                .component(IMDataComponents.XP_GEM_EXTRACT_MODE, false)
                .component(IMDataComponents.XP_LEVEL_STEPS, XpLevelSteps.INFINITY)
                .stacksTo(1));

    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if(!level.isClientSide){
            if(player.isSteppingCarefully()){
                XpLevelSteps step = stack.get(IMDataComponents.XP_LEVEL_STEPS);
                if(step == null) step = XpLevelSteps.INFINITY;
                else step = step.roll();
                stack.set(IMDataComponents.XP_LEVEL_STEPS, step);
                player.displayClientMessage(LangComps.XP_GEM_XP_STEP.apply(getStepComponent(step)),true);
            }
            else {
                ClampedValue xp = stack.get(IMDataComponents.XP_GEM_CAPACITY);
                if(xp == null){
                    xp = new ClampedValue(0, XP_GEM_CAPACITY.get(), 0);
                }
                XpLevelSteps step = stack.get(IMDataComponents.XP_LEVEL_STEPS);
                if(step == null) step = XpLevelSteps.INFINITY;
                int extracted = step.value;
                int maxAccept = xp.max() - xp.value();
                if(extracted == 0){
                    int taken = Math.min(maxAccept, player.totalExperience);
                    xp = xp.add(taken);
                    Utils.setPlayerXp(player, player.totalExperience - taken);
                    stack.set(IMDataComponents.XP_GEM_CAPACITY, xp);
                }
                else {
                    int l = player.experienceLevel;
                    int amount = extracted >= l
                            ? player.totalExperience
                            : player.totalExperience - Utils.getTotalXpInLevel(l - extracted);
                    int taken = Math.min(Math.min(amount, maxAccept), player.totalExperience);
                    if(taken > 0){
                        xp = xp.add(taken);
                        Utils.setPlayerXp(player, player.totalExperience - taken);
                        stack.set(IMDataComponents.XP_GEM_CAPACITY, xp);
                    }
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private Component getStepComponent(XpLevelSteps step){
        return Component.translatable(step.toString()).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x22ff10;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        ClampedValue xp = stack.get(IMDataComponents.XP_GEM_CAPACITY);
        if(xp == null){
            return 0;
        }
        float k = ((float) xp.value()) / xp.max();
        return (int) (k * 13);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return Boolean.TRUE.equals(stack.get(IMDataComponents.XP_GEM_EXTRACT_MODE));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ClampedValue xp = stack.get(IMDataComponents.XP_GEM_CAPACITY);
        if(xp == null){
            xp = new ClampedValue(0, XP_GEM_CAPACITY.get(), 0);
        }
        XpLevelSteps steps = stack.get(IMDataComponents.XP_LEVEL_STEPS);
        if(steps == null){
            steps = XpLevelSteps.INFINITY;
        }
        boolean extractedMode = Boolean.TRUE.equals(stack.get(IMDataComponents.XP_GEM_EXTRACT_MODE));
        tooltipComponents.add(1, LangComps.XP_GEM_TOOLTIP.apply(xp.value(), xp.max()));
        tooltipComponents.add(2, LangComps.XP_GEM_XP_STEP.apply(getStepComponent(steps)));
        tooltipComponents.add(3, LangComps.XP_GEM_EXTRACT_MODE.apply(Utils.getOnOffComponent(extractedMode)));
    }

    @SubscribeEvent
    public static void extractXp(PlayerXpEvent.PickupXp e){
        ExperienceOrb orb = e.getOrb();
        int n = orb.getValue();
        Player player = e.getEntity();
        for(ItemStack stack: player.inventoryMenu.getItems()){
            if(stack.is(IMItems.XP_GEM)){
                if(Boolean.TRUE.equals(stack.get(IMDataComponents.XP_GEM_EXTRACT_MODE))){
                    ClampedValue xp = stack.get(IMDataComponents.XP_GEM_CAPACITY);
                    if(xp == null){
                        xp = new ClampedValue(0, XP_GEM_CAPACITY.get(), 0);
                    }
                    int maxAccepted = xp.max() - xp.value();
                    int taken = Math.min(n, maxAccepted);
                    xp = xp.add(taken);
                    n -= taken;
                    stack.set(IMDataComponents.XP_GEM_CAPACITY, xp);
                }
                if(n <= 0){
                    e.setCanceled(true);
                    orb.discard();
                }
            }
        }
        orb.value = n;
    }

    @Override
    public void onLeftClick(Level level, Player player, ItemStack stack) {
        if(!level.isClientSide){
            if(player.isSteppingCarefully()){
                Boolean extract = stack.get(IMDataComponents.XP_GEM_EXTRACT_MODE);
                if(extract == null) {
                    extract = Boolean.FALSE;
                }
                else {
                    extract = !extract;
                }
                stack.set(IMDataComponents.XP_GEM_EXTRACT_MODE, extract);
                player.displayClientMessage(LangComps.XP_GEM_EXTRACT_MODE.apply(Utils.getOnOffComponent(extract)), true);
            }
            else {
                ClampedValue xp = stack.get(IMDataComponents.XP_GEM_CAPACITY);
                if(xp == null){
                    xp = new ClampedValue(0, XP_GEM_CAPACITY.get(), 0);
                }
                XpLevelSteps step = stack.get(IMDataComponents.XP_LEVEL_STEPS);
                if(step == null) step = XpLevelSteps.INFINITY;
                int inserted = step.value;
                int maxExtracted = xp.value();
                if(inserted == 0){
                    xp = xp.empty();
                    Utils.setPlayerXp(player, player.totalExperience + maxExtracted);
                    stack.set(IMDataComponents.XP_GEM_CAPACITY, xp);
                }
                else {
                    int l = player.experienceLevel;
                    int amount = Utils.getTotalXpInLevel(l + inserted) - player.totalExperience;
                    int taken = Math.min(xp.value(), amount);
                    if(taken > 0){
                        xp = xp.add(-taken);
                        Utils.setPlayerXp(player, player.totalExperience + taken);
                        stack.set(IMDataComponents.XP_GEM_CAPACITY, xp);
                    }
                }
            }
        }
    }
}
