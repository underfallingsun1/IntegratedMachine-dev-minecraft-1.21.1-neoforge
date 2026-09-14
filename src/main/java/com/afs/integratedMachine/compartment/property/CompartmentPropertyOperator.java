package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.Level;

import java.util.List;

public record CompartmentPropertyOperator(AggregationType aggregation, List<CompartmentProperty> properties)
        implements CompartmentProperty {
    public static final MapCodec<CompartmentPropertyOperator> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    AggregationType.CODEC.fieldOf("aggregation").forGetter(CompartmentPropertyOperator::aggregation),
                    Codec.lazyInitialized(() -> CompartmentProperty.CODEC).listOf().fieldOf("properties")
                            .forGetter(CompartmentPropertyOperator::properties)
            ).apply(inst, CompartmentPropertyOperator::new)
    );

    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        return aggregation.calculate(properties.stream().map(p -> p.getPropertyValue(level, blocks)).toList());
    }
}
