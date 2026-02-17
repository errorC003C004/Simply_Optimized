package com.errorC003C004.simply_optimized.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PingPayload() implements CustomPayload {

    public static final CustomPayload.Id<PingPayload> ID =
            new CustomPayload.Id<>(Identifier.of("simply-optimized", "handshake_ping"));

    public static final PacketCodec<RegistryByteBuf, PingPayload> CODEC =
            PacketCodec.unit(new PingPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}