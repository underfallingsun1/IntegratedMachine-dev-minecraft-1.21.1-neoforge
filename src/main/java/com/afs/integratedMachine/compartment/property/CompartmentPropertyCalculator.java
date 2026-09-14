package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.compartment.blockGroup.BlockGroupType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record CompartmentPropertyCalculator(
    List<BlockGroupType> groups,
    String key,
    AggregationType aggregation,
    boolean onlyCalculateNonZeroValue
) implements CompartmentProperty{

    public static final MapCodec<CompartmentPropertyCalculator> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    BlockGroupType.CODEC.listOf().fieldOf("groups").forGetter(CompartmentPropertyCalculator::groups),
                    Codec.STRING.fieldOf("key").forGetter(CompartmentPropertyCalculator::key),
                    AggregationType.CODEC.fieldOf("aggregation").forGetter(CompartmentPropertyCalculator::aggregation),
                    Codec.BOOL.fieldOf("only_calculate_non_zero_value").forGetter(CompartmentPropertyCalculator::onlyCalculateNonZeroValue)
            ).apply(inst, CompartmentPropertyCalculator::new)
    );


    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        List<Integer> values = new ArrayList<>();
        for(BlockGroupType type: groups){
            Set<BlockPos> blockOfType = blocks.getBlocksIfPresent(type);
            var valueStream = blockOfType.stream().map(
                    pos -> Objects.requireNonNullElse(BuiltInRegistries.BLOCK.wrapAsHolder(level.getBlockState(pos).getBlock())
                                    .getData(CompartmentBlockProperty.COMPARTMENT_BLOCK_PROPERTIES), CompartmentBlockProperty.EMPTY)
                            .getValue(key));
            if(onlyCalculateNonZeroValue){
                valueStream = valueStream.filter(i -> i != 0);
            }
            values.addAll(valueStream.toList());
        }
        return aggregation.calculate(values);
    }
}
