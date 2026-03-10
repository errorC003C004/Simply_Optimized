package com.errorC003C004.simply_optimized.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ClientActionPayload(Action action) implements CustomPayload {

    public static final Id<ClientActionPayload> ID =
            new Id<>(Identifier.of("simply_optimized", "client_action"));

    public enum Action {
        IMMORTALITY_TOGGLE,
        NO_AGGRO_TOGGLE,
        INSTAKILL_TOGGLE,
        ARMOR_BYPASS_TOGGLE,
        TOGGLE_FEATURE,
        OPEN_MENU
    }

    public static final PacketCodec<RegistryByteBuf, ClientActionPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.indexed(
                            i -> Action.values()[i],
                            Action::ordinal
                    ),
                    ClientActionPayload::action,
                    ClientActionPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}