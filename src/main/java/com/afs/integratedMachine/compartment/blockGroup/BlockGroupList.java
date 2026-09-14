package com.afs.integratedMachine.compartment.blockGroup;

import com.afs.integratedMachine.utils.Meta;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BlockGroupList {
    private final Map<BlockGroupType, Set<BlockPos>> structure;

    public static final BlockGroupList EMPTY = new BlockGroupList(Map.of()){
        @Override
        public boolean isEmpty() {
            return true;
        }
    };

    public BlockGroupList(Map<BlockGroupType, Set<BlockPos>> structure) {
        this.structure = structure;
    }

    public BlockGroupList(){
        this(new HashMap<>());
    }

    public void setBlocks(BlockGroupType type, Set<BlockPos> pos){
        structure.put(type, pos);
    }

    public Set<BlockPos> getBlocks(BlockGroupType type){
        if(type == BlockGroupTypes.ANY){
            Set<BlockPos> res = new HashSet<>();
            for(var sets: structure.values()){
                res.addAll(sets);
            }
            return res;
        }
        if(type == BlockGroupTypes.EXTERNAL){
            Meta.LOGGER.warn("warn: add pos to external set!");
            return new HashSet<>();
        }
        return structure.computeIfAbsent(type, t -> new HashSet<>());
    }

    public Set<BlockPos> getBlocksIfPresent(BlockGroupType type){
        if(type == BlockGroupTypes.ANY){
            Set<BlockPos> res = new HashSet<>();
            for(var sets: structure.values()){
                res.addAll(sets);
            }
            return res;
        }
        return structure.getOrDefault(type, Set.of());
    }

    public void forEach(BlockGroupType type, Consumer<BlockPos> run){
        if(type == BlockGroupTypes.ANY) structure.values().forEach(posSet -> posSet.forEach(run));
        structure.getOrDefault(type, Set.of()).forEach(run);
    }

    public void removeBlockPosFromAllSet(BlockPos pos){
        structure.forEach((t, s) -> s.remove(pos));
    }

    public boolean isEmpty(){
        return structure.values().stream().allMatch(Set::isEmpty);
    }

    public boolean contains(BlockPos pos){
        return structure.values().stream().anyMatch(s -> s.contains(pos));
    }

    public boolean contains(BlockGroupType type, BlockPos pos){
        if(type == BlockGroupTypes.EXTERNAL){
            return !contains(pos);
        }
        if(type == BlockGroupTypes.ANY){
            return contains(pos);
        }
        if(!structure.containsKey(type)){
            return false;
        }
        return structure.get(type).contains(pos);
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        structure.forEach((t, s) -> {
            if(!s.isEmpty()) {
                tag.putLongArray(t.getId(), s.stream().map(BlockPos::asLong).toList());
            }
        });
        return tag;
    }

    public static BlockGroupList load(CompoundTag tag){
        Map<BlockGroupType, Set<BlockPos>> structure = new HashMap<>();
        for(String key: tag.getAllKeys()){
            structure.put(BlockGroupType.of(key),
                    Arrays.stream(tag.getLongArray(key)).mapToObj(BlockPos::of).collect(Collectors.toSet()));
        }
        return new BlockGroupList(structure);
    }

    public Set<BlockGroupType> getGroupOfBlock(BlockPos pos){
        Set<BlockGroupType> res = new HashSet<>();
        boolean allContains = true;
        for(Map.Entry<BlockGroupType, Set<BlockPos>> setPair: structure.entrySet()){
            if(setPair.getValue().contains(pos)){
                res.add(setPair.getKey());
            }
            else {
                allContains = false;
            }
        }
        if(!res.isEmpty()){
            if(allContains){
                res.add(BlockGroupTypes.ANY);
            }
            return res;
        }
        return Set.of(BlockGroupTypes.EXTERNAL);
    }

    //should be used only when the block only in one type.
    //or judge whether in a group that not cross with other groups.
    public BlockGroupType getSingleGroupOfBlock(BlockPos pos){
        for(var group: structure.entrySet()){
            if(group.getValue().contains(pos)){
                return group.getKey();
            }
        }
        return BlockGroupTypes.EXTERNAL;
    }



    public static final Codec<BlockGroupList> CODEC = Codec.pair(BlockGroupType.CODEC, BlockPos.CODEC.listOf())
            .listOf().xmap(pairs -> {
                Map<BlockGroupType, Set<BlockPos>> map = new HashMap<>();
                for(var p : pairs){
                    map.put(p.getFirst(), new HashSet<>(p.getSecond()));
                }
                return new BlockGroupList(map);
            }, m -> {
                List<Pair<BlockGroupType, List<BlockPos>>> pairs = new ArrayList<>();
                m.structure.forEach((k, v) -> pairs.add(new Pair<>(k, List.copyOf(v))));
                return pairs;
            });

    public static final StreamCodec<ByteBuf, BlockGroupList> STREAM_CODEC =
            new StreamCodec<>() {
                private static final StreamCodec<ByteBuf, Set<BlockPos>> POS_CODEC =
                        BlockPos.STREAM_CODEC.apply(ByteBufCodecs.collection(
                                HashSet::new
                        ));

                @Override
                public BlockGroupList decode(ByteBuf buffer) {
                    int n = ByteBufCodecs.INT.decode(buffer);
                    Map<BlockGroupType, Set<BlockPos>> map = new HashMap<>(n * 4 / 3 + 1);
                    for (int i = 0; i < n; i++) {
                        BlockGroupType type = BlockGroupType.STREAM_CODEC.decode(buffer);
                        Set<BlockPos> posList = POS_CODEC.decode(buffer);
                        map.put(type, posList);
                    }
                    return new BlockGroupList(map);
                }

                @Override
                public void encode(ByteBuf buffer, BlockGroupList value) {
                    ByteBufCodecs.INT.encode(buffer, value.structure.size());
                    value.structure.forEach((k, v) -> {
                        BlockGroupType.STREAM_CODEC.encode(buffer, k);
                        POS_CODEC.encode(buffer, v);
                    });
                }
            };
}
