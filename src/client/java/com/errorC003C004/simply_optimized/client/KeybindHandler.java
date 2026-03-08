package com.errorC003C004.simply_optimized.client;

import com.errorC003C004.simply_optimized.client.UI.MainScreen;
import com.errorC003C004.simply_optimized.client.UI.UIFunctions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
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

            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && openMenuPressed &&!openMenuWasPressed) {
                onOpenMenu(client);
            }

            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && toggleFeaturePressed && !toggleFeatureWasPressed) {
                onImmortalityToggle(client);
            }

            openMenuWasPressed = openMenuPressed;
            toggleFeatureWasPressed = toggleFeaturePressed;
        });
    }

    private static void onOpenMenu(MinecraftClient client) {
        client.execute(() -> client.setScreen(new MainScreen()));
    }

    private static void onImmortalityToggle(MinecraftClient client) {
        UIFunctions.immortalitybutton(client);
    }
}