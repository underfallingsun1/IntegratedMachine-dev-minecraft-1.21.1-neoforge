package com.afs.integratedMachine.network.payload;

import com.afs.integratedMachine.utils.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ClientNotice implements CustomPacketPayload {
    LEFT_CLICK, RIGHT_CLICK;

    public static final CustomPacketPayload.Type<ClientNotice> TYPE =
            new CustomPacketPayload.Type<>(Utils.modLoc("client_notice"));

    public static final IntFunction<ClientNotice> BY_ID =
            ByIdMap.continuous(
                    ClientNotice::ordinal,
                    ClientNotice.values(),
                    ByIdMap.OutOfBoundsStrategy.ZERO
            );

    public static final StreamCodec<ByteBuf, ClientNotice> STREAM_CODEC =
            ByteBufCodecs.idMapper(ClientNotice.BY_ID, ClientNotice::ordinal);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
