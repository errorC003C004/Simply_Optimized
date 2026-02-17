// Simply_optimizedClient.java
package com.errorC003C004.simply_optimized.client;

import com.errorC003C004.simply_optimized.networking.PingPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Simply_optimizedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        CommandInitClient.register();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientPlayNetworking.send(new PingPayload());
        });
        HudRenderClient.init();
    }
}