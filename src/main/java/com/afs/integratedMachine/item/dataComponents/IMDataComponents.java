package com.afs.integratedMachine.item.dataComponents;

import com.afs.integratedMachine.utils.Meta;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class IMDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(
            Registries.DATA_COMPONENT_TYPE, Meta.MODID
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ClampedValue>> XP_GEM_CAPACITY =
            COMPONENTS.registerComponentType("xp_gem_capacity", b -> b.persistent(ClampedValue.CODEC)
                    .networkSynchronized(ClampedValue.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> XP_GEM_EXTRACT_MODE =
            COMPONENTS.registerComponentType("xp_gem_extract_mode", b -> b.persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<XpLevelSteps>> XP_LEVEL_STEPS =
            COMPONENTS.registerComponentType("xp_level_steps", b -> b.persistent(XpLevelSteps.CODEC)
                    .networkSynchronized(XpLevelSteps.STREAM_CODEC));
}
