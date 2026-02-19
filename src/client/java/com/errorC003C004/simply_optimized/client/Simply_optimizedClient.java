package com.errorC003C004.simply_optimized.client;

import com.errorC003C004.simply_optimized.networking.ImmortalityStatusPayload;
import com.errorC003C004.simply_optimized.networking.PingPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class Simply_optimizedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        CommandInitClient.register();
        HudRenderClient.init();

        // Existing handshake ping
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (UIFunctions.isClientWhitelisted) {
                ClientPlayNetworking.send(new PingPayload());
            }
        });

        // Receive immortality updates
        ClientPlayNetworking.registerGlobalReceiver(
                ImmortalityStatusPayload.ID,
                (payload, context) -> {

                    context.client().execute(() -> {
                        UIFunctions.isImmortal = payload.immortal();

                        if (context.client().currentScreen instanceof MyScreen screen) {
                            screen.refreshImmortalityText();
                            screen.immortalityButton.active = true;
                        }
                    });
                }
        );
    }
}