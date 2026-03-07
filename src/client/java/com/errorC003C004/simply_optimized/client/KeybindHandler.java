package com.errorC003C004.simply_optimized.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeybindHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    private static boolean openMenuWasPressed = false;
    private static boolean toggleFeatureWasPressed = false;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null) return;

            long window = MinecraftClient.getInstance().getWindow().getHandle();

            boolean openMenuPressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.openMenuKey) == GLFW.GLFW_PRESS;

            boolean toggleFeaturePressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.toggleImmortalityKey) == GLFW.GLFW_PRESS;

            if (ConfigManagerClient.useKeybinds && openMenuPressed &&!openMenuWasPressed) {
                onOpenMenu(client);
            }

            if (ConfigManagerClient.useKeybinds && toggleFeaturePressed && !toggleFeatureWasPressed) {
                onImmortalityToggle(client);
            }

            openMenuWasPressed = openMenuPressed;
            toggleFeatureWasPressed = toggleFeaturePressed;
        });
    }

    private static void onOpenMenu(MinecraftClient client) {
        client.execute(() -> client.setScreen(new MyScreen()));
    }

    private static void onImmortalityToggle(MinecraftClient client) {
        UIFunctions.immortalitybutton(client);
    }
}