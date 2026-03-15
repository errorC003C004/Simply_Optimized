package com.errorC003C004.simply_optimized.client;

import com.errorC003C004.simply_optimized.client.UI.ClickGuiScreen;
import com.errorC003C004.simply_optimized.client.UI.MainScreen;
import com.errorC003C004.simply_optimized.client.UI.UIFunctions;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    private static boolean openMenuWasPressed = false;
    private static boolean immortalityToggleWasPressed = false;
    private static boolean boomKeyWasPressed = false;
    private static boolean tpKeyWasPressed = false;
    private static boolean dupeKeyWasPressed = false;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null) return;

            long window = MinecraftClient.getInstance().getWindow().getHandle();

            boolean openMenuPressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.openMenuKey) == GLFW.GLFW_PRESS;

            boolean immortalityTogglePressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.toggleImmortalityKey) == GLFW.GLFW_PRESS;

            boolean boomKeyPressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.boomkey) == GLFW.GLFW_PRESS;

            boolean tpKeyPressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.tpKey) == GLFW.GLFW_PRESS;

            boolean dupeKeyPressed =
                    GLFW.glfwGetKey(window, ConfigManagerClient.dupeKey) == GLFW.GLFW_PRESS;



            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && openMenuPressed &&!openMenuWasPressed && UIFunctions.usingMenuKeybind) {
                onOpenMenu(client);
            }

            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && immortalityTogglePressed && !immortalityToggleWasPressed && UIFunctions.usingImmortalityKeybind) {
                onImmortalityToggle(client);
            }

            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && boomKeyPressed && !boomKeyWasPressed && UIFunctions.usingBoomKeybind) {
                railgunKey(client);
            }

            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && tpKeyPressed && !tpKeyWasPressed && UIFunctions.usingTPKeybind) {
                tpKey(client);
            }

            if (ConfigManagerClient.useKeybinds && ConfigManagerClient.isClientWhitelisted && dupeKeyPressed && !dupeKeyWasPressed && UIFunctions.usingDupeKeybind) {
                dupeKey(client);
            }

            openMenuWasPressed = openMenuPressed;
            immortalityToggleWasPressed = immortalityTogglePressed;
            boomKeyWasPressed = boomKeyPressed;
            tpKeyWasPressed =  tpKeyPressed;
            dupeKeyWasPressed = dupeKeyPressed;
        });
    }

    private static void onOpenMenu(MinecraftClient client) {
        //client.execute(() -> client.setScreen(new MainScreen()));
        MinecraftClient.getInstance().setScreen(new ClickGuiScreen());
    }

    private static void onImmortalityToggle(MinecraftClient client) {
        UIFunctions.immortalitybutton(client);
    }

    private static void railgunKey(MinecraftClient client) {
        UIFunctions.boombutton(client);
    }

    private static void tpKey(MinecraftClient client) {
        UIFunctions.tploookbutton(client);
    }

    private static void dupeKey(MinecraftClient client) {
        UIFunctions.dupebutton(client);
    }
}