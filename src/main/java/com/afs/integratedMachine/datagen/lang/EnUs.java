package com.afs.integratedMachine.datagen.lang;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import static com.afs.integratedMachine.utils.LangComps.*;

public class EnUs extends LanguageProvider {
    public EnUs(PackOutput output) {
        super(output, Meta.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(TAB_TITLE.key(), "Integrated Machine");

        add(XP_GEM_TOOLTIP.key(), "Xp: %s/%s");

        add(UTILS_GROUP.key(), "Utils");
        add(XP_GEM_CAPACITY.key(), "XP Gen Capacity");
        add(EXPERIENCE_CONVERTER_FLUID_CACHE.key(), "Experience Converter Fluid Cache");
        add(EXPERIENCE_CONVERTER_BUFFER.key(), "Experience Converter Buffer");
        add(EXPERIENCE_CONVERTER.key(), "Experience Converter");

        add(ON.key(), "ON");
        add(OFF.key(), "OFF");

        add(IMItems.POWERED_INGOT.get(), "Powered Ingot");
        add(IMItems.SHINING_INGOT.get(), "Shining Ingot");
        add(IMItems.END_INGOT.get(), "End Ingot");
        add(IMItems.CRYSTALLIZED_INGOT.get(), "Crystallized Ingot");
        add(IMItems.EXTREME_INGOT.get(), "Extreme Ingot");
        add(IMItems.STEEL_INGOT.get(), "Steel Ingot");

        add(IMItems.XP_GEM.get(), "XP Gen");
        add(IMItems.LIQUID_EXPERIENCE_BUCKET.get(), "Liquid Experience Bucket");
        add(LIQUID_EXPERIENCE_FLUID.key(), "Liquid Experience");

        add(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get(), "Basic Compartment Controller");
        add(IMBlocks.IRON_WALL.get(), "Iron Compartment Wall");
        add(IMBlocks.XP_LANTERN.get(), "Experient Lantern");
        add(IMBlocks.EXPERIENCE_CONVERTER.get(), "Experience Converter");

        add(XP_STEP_INFINITY.key(), "Infinity");
        add(XP_STEP_1L.key(), "1");
        add(XP_STEP_10L.key(), "10");
        add(XP_STEP_100L.key(), "100");
        add(XP_GEM_EXTRACT_MODE.key(), "Extract Mode: %s");
        add(XP_GEM_XP_STEP.key(), "Amount: %s");

        add(EXPERIENCE_CONVERTER_STORE.key(), "Store %s");
        add(EXPERIENCE_CONVERTER_TAKE.key(), "Take %s");
        add(EXPERIENCE_CONVERTER_ALL.key(), "All");
        add(EXPERIENCE_CONVERTER_FLUID.key(), "Liquid Experience: %s/%s mB");
        add(EXPERIENCE_CONVERTER_PRIORITY_FLUID.key(), "Fluid");
        add(EXPERIENCE_CONVERTER_PRIORITY_GEM.key(), "Gem");
        add(EXPERIENCE_CONVERTER_PRIORITY_TOOLTIP.key(), "Priority: fill fluid container then take from XP gem / fill XP gem then take from fluid container");
    }
}
