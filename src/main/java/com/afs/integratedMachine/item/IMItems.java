package com.afs.integratedMachine.item;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.fluid.IMFluids;
import com.afs.integratedMachine.utils.LangComps;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class IMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Meta.MODID);

    public static final DeferredItem<Item> POWERED_INGOT = ITEMS.registerSimpleItem("powered_ingot");
    public static final DeferredItem<Item> SHINING_INGOT = ITEMS.registerSimpleItem("shining_ingot");
    public static final DeferredItem<Item> END_INGOT = ITEMS.registerSimpleItem("end_ingot");
    public static final DeferredItem<Item> CRYSTALLIZED_INGOT = ITEMS.registerSimpleItem("crystallized_ingot");
    public static final DeferredItem<Item> EXTREME_INGOT = ITEMS.registerSimpleItem("extreme_ingot");
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.registerSimpleItem("steel_ingot");

    public static final DeferredItem<BlockItem> BASIC_COMPARTMENT_CONTROLLER = ITEMS.registerSimpleBlockItem(IMBlocks.BASIC_COMPARTMENT_CONTROLLER);
    public static final DeferredItem<BlockItem> IRON_WALL = ITEMS.registerSimpleBlockItem(IMBlocks.IRON_WALL);
    public static final DeferredItem<BlockItem> XP_LANTERN = ITEMS.registerSimpleBlockItem(IMBlocks.XP_LANTERN);
    public static final DeferredItem<BlockItem> EXPERIENCE_CONVERTER = ITEMS.registerSimpleBlockItem(IMBlocks.EXPERIENCE_CONVERTER);

    public static final DeferredItem<BlockItem> TEST_BLOCK = ITEMS.registerSimpleBlockItem(IMBlocks.TEST_BLOCK);

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Meta.MODID);

    public static final DeferredItem<XpGemItem> XP_GEM = ITEMS.registerItem("xp_gem", XpGemItem::new);

    public static final DeferredItem<BucketItem> LIQUID_EXPERIENCE_BUCKET = ITEMS.registerItem(
            "liquid_experience_bucket",
            p -> new BucketItem(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(),
                    p.craftRemainder(Items.BUCKET).stacksTo(1))
    );

    public static final List<DeferredHolder<Item, ?>> HIDDEN_ITEMS = List.of(
            TEST_BLOCK
    );

    public static final Supplier<CreativeModeTab> IM_TAB = TABS.register("integrated_machine", () ->
            CreativeModeTab.builder()
                    .displayItems(
                        (param, output) -> ITEMS.getEntries().forEach(
                                holder -> {
                                    if(!HIDDEN_ITEMS.contains(holder)){
                                        output.accept(holder.get());
                                    }
                                }
                        )
                    )
                    .title(LangComps.TAB_TITLE.apply())
                    .icon(() -> new ItemStack(POWERED_INGOT.get()))
            .build());
}
