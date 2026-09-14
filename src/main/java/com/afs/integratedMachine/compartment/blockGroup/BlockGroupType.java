package com.afs.integratedMachine.compartment.blockGroup;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

public class BlockGroupType {
    private final String id;

    private BlockGroupType(String id){
        this.id = id;
    }

    private static final Map<String, BlockGroupType> VALUES = new HashMap<>();

    public static BlockGroupType of(String id){
        return VALUES.computeIfAbsent(id, BlockGroupType::new);
    }

    public String getId(){
        return id;
    }

    public static final Codec<BlockGroupType> CODEC = Codec.stringResolver(
            BlockGroupType::getId, BlockGroupType::of
    );

    public static final StreamCodec<ByteBuf, BlockGroupType> STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(BlockGroupType::of, BlockGroupType::getId);
}
