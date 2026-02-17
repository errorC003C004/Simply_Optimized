package com.errorC003C004.simply_optimized.client;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;

import net.minecraft.client.MinecraftClient;


public class CommandInitClient {

    public static void register() {

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> {

                    dispatcher.register(
                            ClientCommandManager.literal("e_ui")
                                    .requires(UIFunctions::WhitelistCheck)
                                    .executes(context -> {
                                        MinecraftClient client = MinecraftClient.getInstance();

                                        // MUST run on render thread
                                        client.execute(() -> client.setScreen(new MyScreen()));

                                        return 1;
                                    })
                    );
                    dispatcher.register(
                            ClientCommandManager.literal("e_immortality_toggle")
                                    .requires(UIFunctions::WhitelistCheck)
                                    .executes(context -> {
                                        UIFunctions.isgood = !UIFunctions.isgood;
                                        return 1;
                                    })
                    );

                }
        );
    }
}