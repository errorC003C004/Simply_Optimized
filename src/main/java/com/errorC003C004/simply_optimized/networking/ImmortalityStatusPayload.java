package com.errorC003C004.simply_optimized.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ImmortalityStatusPayload(boolean immortal)
        implements CustomPayload {

    public static final Id<ImmortalityStatusPayload> ID =
            new Id<>(Identifier.of("simply_optimized", "immortality_status"));

    public static final PacketCodec<RegistryByteBuf, ImmortalityStatusPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.BOOLEAN,
                    ImmortalityStatusPayload::immortal,
                    ImmortalityStatusPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}