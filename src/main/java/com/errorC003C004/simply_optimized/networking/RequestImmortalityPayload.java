package com.errorC003C004.simply_optimized.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestImmortalityPayload() implements CustomPayload {

    public static final Id<RequestImmortalityPayload> ID =
            new Id<>(Identifier.of("simply-optimized", "request_immortality"));

    public static final PacketCodec<RegistryByteBuf, RequestImmortalityPayload> CODEC =
            PacketCodec.unit(new RequestImmortalityPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}