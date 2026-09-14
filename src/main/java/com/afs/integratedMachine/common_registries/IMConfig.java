package com.afs.integratedMachine.common_registries;

import com.afs.integratedMachine.block.entity.ExperienceConverterBlockEntity;
import com.afs.integratedMachine.item.XpGemItem;
import com.afs.integratedMachine.utils.LangComps;
import net.neoforged.neoforge.common.ModConfigSpec;

public class IMConfig {
    public static final ModConfigSpec CONFIG_SPEC_STARTUP = new ModConfigSpec.Builder().configure(
            builder -> new IMConfig(builder, bd -> {
                XpGemItem.XP_GEM_CAPACITY = bd
                        .translation(LangComps.XP_GEM_CAPACITY.key())
                        .comment("用来设置经验宝石的容量")
                        .defineInRange("utils.xp_gem_capacity", 65536, 1, Integer.MAX_VALUE);
            })
    ).getRight();

    public static final ModConfigSpec CONFIG_SPEC_SERVER = new ModConfigSpec.Builder().configure(
            builder -> new IMConfig(builder, bd -> {
                ExperienceConverterBlockEntity.ENABLE_FLUID_CACHE = bd
                        .translation(LangComps.EXPERIENCE_CONVERTER_FLUID_CACHE.key())
                        .comment("用来设置经验转换器是否拥有内部流体缓存")
                        .define("utils.experience_converter.fluid_cache", false);

                ExperienceConverterBlockEntity.BUFFER_CAPACITY = bd
                        .translation(LangComps.EXPERIENCE_CONVERTER_BUFFER.key())
                        .comment("用来设置经验转换器的内部流体缓存容量(mB), 仅在开启内部流体缓存时生效")
                        .defineInRange("utils.experience_converter.buffer", 100000, 1, Integer.MAX_VALUE);
            })
    ).getRight();


    private IMConfig(ModConfigSpec.Builder builder, ConfigBuilder configBuilder){
        configBuilder.buildConfig(builder);
    }

    @FunctionalInterface
    public interface ConfigBuilder{
        void buildConfig(ModConfigSpec.Builder builder);
    }
}
