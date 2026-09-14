package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record CompartmentPropertyMapper(
        CompartmentProperty property,
        int defaultValue,
        Int2IntArrayMap mapping
        ) implements CompartmentProperty{

    public static final Codec<Pair<List<Integer>, Integer>> PAIR_CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                    Codec.INT.listOf().fieldOf("from").forGetter(Pair::getFirst),
                    Codec.INT.fieldOf("to").forGetter(Pair::getSecond)
            ).apply(inst, Pair::new));

    public static final Codec<Int2IntArrayMap> MAPPING_CODEC = PAIR_CODEC.listOf().xmap(
            ls -> {
                Int2IntArrayMap res = new Int2IntArrayMap();
                for(Pair<List<Integer>, Integer> entry: ls){
                    int j = entry.getSecond();
                    for(int i : entry.getFirst()){
                        res.put(i, j);
                    }
                }
                return res;
            },
            mp -> {
                Int2ObjectOpenHashMap<List<Integer>> reversed = new Int2ObjectOpenHashMap<>();
                for(int k: mp.keySet()){
                    List<Integer> from = reversed.computeIfAbsent(mp.get(k), n -> new ArrayList<>());
                    from.add(k);
                }
                List<Pair<List<Integer>, Integer>> values = new ArrayList<>();
                for(int j:reversed.keySet()){
                    values.add(new Pair<>(reversed.get(j), j));
                }
                return values;
            }
    );

    public static final MapCodec<CompartmentPropertyMapper> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    Codec.lazyInitialized(() -> CompartmentProperty.CODEC).fieldOf("property").forGetter(CompartmentPropertyMapper::property),
                    Codec.INT.optionalFieldOf("default", 0).forGetter(CompartmentPropertyMapper::defaultValue),
                    MAPPING_CODEC.fieldOf("mapping").forGetter(CompartmentPropertyMapper::mapping)
            ).apply(inst, CompartmentPropertyMapper::new)
    );


    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        return mapping.getOrDefault(property.getPropertyValue(level, blocks), defaultValue);
    }
}
