package com.errorC003C004.simply_optimized.client;

import com.errorC003C004.simply_optimized.client.UI.TogglesScreen;
import com.errorC003C004.simply_optimized.client.UI.VisualizerClient;
import com.errorC003C004.simply_optimized.client.UI.UIFunctions;
import com.errorC003C004.simply_optimized.networking.ToggleStatusPayload;
import com.errorC003C004.simply_optimized.networking.PingPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class Simply_optimizedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManagerClient.loadConfig();
        KeybindHandler.register();
        CommandInitClient.register();
        VisualizerClient.init();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (ConfigManagerClient.isClientWhitelisted) {
                ClientPlayNetworking.send(new PingPayload());
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(
                ToggleStatusPayload.ID,
                (payload, context) -> context.client().execute(() -> {

                    var client = context.client();

                    switch (payload.action()) {

                        case IMMORTALITY -> {
                            UIFunctions.isImmortal = payload.enabled();

                            if (client.currentScreen instanceof TogglesScreen screen) {
                                screen.refreshImmortalityText();
                                screen.immortalityButton.active = true;
                            }
                        }

                        case NO_AGGRO -> {
                            UIFunctions.isNoAggro = payload.enabled();

                            if (client.currentScreen instanceof TogglesScreen screen) {
                                screen.refreshNoAggroText();
                                screen.noAggroButton.active = true;
                            }
                        }

                        case INSTAKILL -> {
                            UIFunctions.isInstakill = payload.enabled();

                            if (client.currentScreen instanceof TogglesScreen screen) {
                                screen.refreshInstakillText();
                                screen.instakillButton.active = true;
                            }
                        }

                        case ARMOR_BYPASS -> {
                            UIFunctions.isArmorBypass = payload.enabled();

                            if (client.currentScreen instanceof TogglesScreen screen) {
                                screen.refreshArmorBypassText();
                                screen.armorBypassButton.active = true;
                            }
                        }
                    }

                })
        );
    }
}