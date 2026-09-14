package com.afs.integratedMachine.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LangComps {
    public static final LangComp TAB_TITLE = new LangComp("tab.integrated_machine.title");

    //configurations
    public static final LangComp XP_GEM_CAPACITY = new LangComp("config.name.xp_gem_capacity");
    public static final LangComp EXPERIENCE_CONVERTER = new LangComp("integrated_machine.configuration.experience_converter");
    public static final LangComp EXPERIENCE_CONVERTER_FLUID_CACHE = new LangComp("config.name.experience_converter_fluid_cache");
    public static final LangComp EXPERIENCE_CONVERTER_BUFFER = new LangComp("config.name.experience_converter_buffer");
    public static final LangComp UTILS_GROUP = new LangComp("integrated_machine.configuration.utils");

    //xp level steps and xp gen
    public static final LangComp XP_STEP_INFINITY = new LangComp("xp_step.infinity");
    public static final LangComp XP_STEP_1L = new LangComp("xp_step.1l");
    public static final LangComp XP_STEP_10L = new LangComp("xp_step.10l");
    public static final LangComp XP_STEP_100L = new LangComp("xp_step.100l");
    public static final LangComp XP_GEM_EXTRACT_MODE = new LangComp("integrated_machine.xp_gem.modify.extract_mode");
    public static final LangComp XP_GEM_XP_STEP = new LangComp("integrated_machine.xp_gem.modify.xp_step");
    public static final LangComp XP_GEM_TOOLTIP = new LangComp("integrated_machine.xp_gem.tooltip");

    //fluids
    public static final LangComp LIQUID_EXPERIENCE_FLUID = new LangComp("fluid_type.integrated_machine.liquid_experience");

    //experience converter
    public static final LangComp EXPERIENCE_CONVERTER_STORE = new LangComp("gui.integrated_machine.experience_converter.store");
    public static final LangComp EXPERIENCE_CONVERTER_TAKE = new LangComp("gui.integrated_machine.experience_converter.take");
    public static final LangComp EXPERIENCE_CONVERTER_ALL = new LangComp("gui.integrated_machine.experience_converter.all");
    public static final LangComp EXPERIENCE_CONVERTER_FLUID = new LangComp("gui.integrated_machine.experience_converter.fluid");
    public static final LangComp EXPERIENCE_CONVERTER_PRIORITY_FLUID = new LangComp("gui.integrated_machine.experience_converter.priority.fluid");
    public static final LangComp EXPERIENCE_CONVERTER_PRIORITY_GEM = new LangComp("gui.integrated_machine.experience_converter.priority.gem");
    public static final LangComp EXPERIENCE_CONVERTER_PRIORITY_TOOLTIP = new LangComp("gui.integrated_machine.experience_converter.priority.tooltip");

    //on and off
    public static final LangComp ON = new LangComp("integrated_machine.modify.on");
    public static final LangComp OFF = new LangComp("integrated_machine.modify.off");

    public record LangComp(String key){
        public MutableComponent apply(){
            return Component.translatable(key);
        }

        public MutableComponent apply(Object... args){
            return Component.translatable(key, args);
        }
    }
}
