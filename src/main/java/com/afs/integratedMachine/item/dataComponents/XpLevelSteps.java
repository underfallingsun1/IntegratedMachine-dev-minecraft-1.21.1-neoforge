package com.afs.integratedMachine.item.dataComponents;

import com.afs.integratedMachine.utils.LangComps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum XpLevelSteps {
    INFINITY(0), XP_1L(1), XP_10L(10), XP_100L(100);
    XpLevelSteps(int value){
        this.value = value;
    }
    public final int value;
    public static XpLevelSteps of(int n){
        return switch (n){
            case -1, 0 -> INFINITY;
            case 1 -> XP_1L;
            case 2, 10 -> XP_10L;
            case 3, 100 -> XP_100L;
            default -> throw new IllegalArgumentException("undefined level:" + n);
        };
    }
    public int getValue(){
        return value;
    }
    public static XpLevelSteps of(String name){
        if(name.equals("infinity")){
            return INFINITY;
        }
        throw new IllegalArgumentException("undefined level:" + name);
    }
    public static final Codec<Either<Integer, String>> VALUE_CODEC =
            Codec.either(Codec.INT, Codec.STRING);

    public static final Codec<XpLevelSteps> CODEC = VALUE_CODEC.xmap(
            e -> e.map(XpLevelSteps::of, XpLevelSteps::of),
            step -> Either.left(step.value)
    );

    public static final StreamCodec<ByteBuf, XpLevelSteps> STREAM_CODEC =
            ByteBufCodecs.either(ByteBufCodecs.INT, ByteBufCodecs.STRING_UTF8).map(
                    e -> e.map(XpLevelSteps::of, XpLevelSteps::of),
                    step -> Either.left(step.value)
            );

    public XpLevelSteps roll(){
        return switch (this) {
            case INFINITY -> XP_1L;
            case XP_1L -> XP_10L;
            case XP_10L -> XP_100L;
            case XP_100L -> INFINITY;
        };
    }


    @Override
    public String toString() {
        return switch (this){
            case INFINITY -> LangComps.XP_STEP_INFINITY.key();
            case XP_1L -> LangComps.XP_STEP_1L.key();
            case XP_10L -> LangComps.XP_STEP_10L.key();
            case XP_100L -> LangComps.XP_STEP_100L.key();
        };
    }
}
