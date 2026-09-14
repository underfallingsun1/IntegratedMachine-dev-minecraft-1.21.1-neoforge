package com.afs.integratedMachine.item.dataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record ClampedValue(
        int min, int max, int value
) {
    public ClampedValue add(int n){
        return new ClampedValue(min, max, Mth.clamp(value + n, min, max));
    }

    public ClampedValue add(ClampedValue v){
        return add(v.value);
    }

    public ClampedValue empty(){
        return new ClampedValue(min, max, min);
    }

    public ClampedValue full(){
        return new ClampedValue(min, max, max);
    }

    public static final Codec<ClampedValue> CODEC = RecordCodecBuilder.create(
            inst -> inst.group(
                    Codec.INT.fieldOf("min").forGetter(ClampedValue::min),
                    Codec.INT.fieldOf("max").forGetter(ClampedValue::max),
                    Codec.INT.fieldOf("value").forGetter(ClampedValue::value)
            ).apply(inst, ClampedValue::new)
    );

    public static final StreamCodec<ByteBuf, ClampedValue> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ClampedValue::min,
            ByteBufCodecs.INT, ClampedValue::max,
            ByteBufCodecs.INT, ClampedValue::value,
            ClampedValue::new
    );
}
