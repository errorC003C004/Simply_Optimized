package com.errorC003C004.simply_optimized.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ToggleStatusPayload(Action action, boolean enabled)
        implements CustomPayload {

    public enum Action {
        IMMORTALITY,
        NO_AGGRO,
        INSTAKILL,
        ARMOR_BYPASS
    }

    public static final Id<ToggleStatusPayload> ID =
            new Id<>(Identifier.of("simply_optimized", "toggle_status"));

    public static final PacketCodec<RegistryByteBuf, ToggleStatusPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING.xmap(Action::valueOf, Action::name),
                    ToggleStatusPayload::action,
                    PacketCodecs.BOOLEAN,
                    ToggleStatusPayload::enabled,
                    ToggleStatusPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}