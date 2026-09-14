package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.compartment.blockGroup.BlockGroupType;
import com.afs.integratedMachine.utils.MathUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public record CompartmentPropertyRequirement(
        List<BlockGroupType> groups,
        List<Either<TagKey<Block>, Block>> whiteList,
        List<Either<TagKey<Block>, Block>> blackList,
        Map<String, Either<Integer, List<Integer>>> valueRange,
        int outputValue
)
        implements CompartmentProperty{

    public static final Codec<Either<TagKey<Block>, Block>> BLOCK_CODEC =
            Codec.either(TagKey.hashedCodec(Registries.BLOCK), BuiltInRegistries.BLOCK.byNameCodec());

    public static final Codec<Either<Integer, List<Integer>>> RANGE_CODEC =
            Codec.either(Codec.INT, Codec.INT.listOf()).xmap(
                    CompartmentPropertyRequirement::filterRange,
                    CompartmentPropertyRequirement::filterRange
            );

    public static final MapCodec<CompartmentPropertyRequirement> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    BlockGroupType.CODEC.listOf().fieldOf("groups").forGetter(CompartmentPropertyRequirement::groups),
                    BLOCK_CODEC.listOf().fieldOf("white_list").forGetter(CompartmentPropertyRequirement::whiteList),
                    BLOCK_CODEC.listOf().fieldOf("black_list").forGetter(CompartmentPropertyRequirement::blackList),
                    Codec.unboundedMap(Codec.STRING, RANGE_CODEC).fieldOf("value_ranges").forGetter(CompartmentPropertyRequirement::valueRange),
                    Codec.INT.fieldOf("output_value").forGetter(CompartmentPropertyRequirement::outputValue)
            ).apply(inst, CompartmentPropertyRequirement::new)
    );

    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        boolean whiteFlag = !whiteList.isEmpty();
        boolean blackFlag = !blackList.isEmpty();
        boolean valueFlag = !valueRange.isEmpty();
        Set<BlockState> states = new HashSet<>();
        for(BlockGroupType type: groups){
            Set<BlockPos> part = blocks.getBlocksIfPresent(type);
            Set<BlockState> partInLevel = part.stream().map(level::getBlockState).collect(Collectors.toSet());
            states.addAll(partInLevel);
        }
        if(whiteFlag){
            if(!states.stream().allMatch(
                    state -> whiteList.stream().anyMatch(
                            i -> i.map(state::is, state::is)
                    )
            )){
                return 0;
            }
        }
        if(blackFlag){
            if(states.stream().anyMatch(
                    state -> blackList.stream().anyMatch(
                            i -> i.map(state::is, state::is)
                    )
            )){
                return 0;
            }
        }
        if(valueFlag){
            if(!states.stream().allMatch(
                    state -> {
                        Holder<Block> block = BuiltInRegistries.BLOCK.wrapAsHolder(state.getBlock());
                        CompartmentBlockProperty property = block.getData(CompartmentBlockProperty.COMPARTMENT_BLOCK_PROPERTIES);
                        if (property == null) {
                            property = CompartmentBlockProperty.EMPTY;
                        }
                        for (String key : valueRange.keySet()) {
                            int n = property.getValue(key);
                            var range = valueRange.get(key);
                            if (!range.map(k -> k == n, l -> MathUtils.inRange(n, l.getFirst(), l.get(1)))) {
                                return false;
                            }
                        }
                        return true;
                    })
            ) return 0;
        }
        return outputValue;
    }

    public static Either<Integer, List<Integer>> filterRange(Either<Integer, List<Integer>> e) {
        if (e.right().isPresent()) {
            List<Integer> values = e.right().get();
            if (values.size() == 1) return Either.left(values.getFirst());
            else if (values.size() >= 3) return Either.right(values.subList(0, 2));
        }
        return e;
    }
}
