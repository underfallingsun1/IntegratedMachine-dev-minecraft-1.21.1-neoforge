package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.utils.Utils;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.Map;

public record CompartmentBlockProperty(Map<String, Integer> values) {
    public static final CompartmentBlockProperty EMPTY = new CompartmentBlockProperty(Map.of());

    public int getValue(String id){
        return values.getOrDefault(id, 0);
    }

    public boolean hasKey(String id){
        return values.containsKey(id);
    }

    public static final Codec<CompartmentBlockProperty> CODEC =
            Codec.unboundedMap(Codec.STRING, Codec.INT).xmap(CompartmentBlockProperty::new, CompartmentBlockProperty::values);

    public static final DataMapType<Block, CompartmentBlockProperty> COMPARTMENT_BLOCK_PROPERTIES =
            DataMapType.builder(
                    Utils.modLoc("compartment_block_properties"),
                    Registries.BLOCK,
                    CompartmentBlockProperty.CODEC
            ).build();
}
