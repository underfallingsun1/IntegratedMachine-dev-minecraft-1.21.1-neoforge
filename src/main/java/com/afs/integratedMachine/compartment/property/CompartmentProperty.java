package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.utils.Utils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public interface CompartmentProperty {
    MapCodec<? extends CompartmentProperty> codec();

    int getPropertyValue(Level level, BlockGroupList blocks);

    ResourceKey<Registry<MapCodec<? extends CompartmentProperty>>> COMPARTMENT_PROPERTY_TYPE_KEY =
            ResourceKey.createRegistryKey(Utils.modLoc("compartment_property_type"));

    Registry<MapCodec<? extends CompartmentProperty>> COMPARTMENT_PROPERTY_TYPE_REGISTRY =
            new RegistryBuilder<>(COMPARTMENT_PROPERTY_TYPE_KEY)
                    .sync(false)
                    .create();

    Codec<CompartmentProperty> CODEC = COMPARTMENT_PROPERTY_TYPE_REGISTRY.byNameCodec()
            .dispatch(CompartmentProperty::codec, Function.identity());

    ResourceKey<Registry<CompartmentProperty>> COMPARTMENT_PROPERTY_KEY =
            ResourceKey.createRegistryKey(Utils.modLoc("compartment_property"));
}
