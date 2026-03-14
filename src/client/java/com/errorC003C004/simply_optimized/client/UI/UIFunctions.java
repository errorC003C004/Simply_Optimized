package com.errorC003C004.simply_optimized.client.UI;

import com.errorC003C004.simply_optimized.client.ConfigManagerClient;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.errorC003C004.simply_optimized.networking.ClientActionPayload;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class UIFunctions {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");

    public static boolean isImmortal = false;
    public static boolean isNoAggro = false;
    public static boolean isInstakill = false;
    public static boolean isArmorBypass = false;

    public static boolean usingKeybinds = false;

    public static boolean usingMenuKeybind = true;
    public static boolean usingImmortalityKeybind = true;
    public static boolean usingBoomKeybind = true;
    public static boolean usingTPKeybind = true;
    public static boolean usingDupeKeybind = true;

    public static boolean WhitelistCheck(FabricClientCommandSource source) {
        return ConfigManagerClient.isClientWhitelisted;
    }

    //Buttons
    public static void visualizerbutton(MinecraftClient client) {
        if (ConfigManagerClient.showVisualizer) {
            ConfigManagerClient.showVisualizerFalse();
        } else {
            ConfigManagerClient.showVisualizerTrue();
        }
        if (client.currentScreen instanceof TogglesScreen screen) {
            screen.refreshVisualizerText();
        }
    }

    public static void immortalitybutton(MinecraftClient client) {
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.IMMORTALITY_TOGGLE));
    }

    public static void noaggrobutton(MinecraftClient client) {
        if (client == null ||  client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.NO_AGGRO_TOGGLE));
    }

    public static void instakillbutton(MinecraftClient client) {
        if (client == null ||  client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.INSTAKILL_TOGGLE));
    }

    public static void armorbypassbutton(MinecraftClient client) {
        if (client == null ||  client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.ARMOR_BYPASS_TOGGLE));
    }

    public static void boombutton(MinecraftClient client) {
        if (client == null ||  client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.RAILGUN_PLAYER));
    }

    public static void tploookbutton(MinecraftClient client) {
        if (client == null ||  client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.TP_PLAYER));
    }

    public static void dupebutton(MinecraftClient client) {
        if (client == null ||  client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.DUPE));
    }




    public static void keybindTogglebutton(MinecraftClient client) {
        ConfigManagerClient.toggleKeybinds();
        if (client.currentScreen instanceof TogglesScreen screen) {
            screen.refreshVisualizerText();
        }
    }




    public static void clientWhitelist(ButtonWidget button) {
        if (ConfigManagerClient.isClientWhitelisted) {
            ConfigManagerClient.removeClientWhitelist();
        }
        else {
            ConfigManagerClient.addClientWhitelist();
        }
        button.setMessage(Text.literal(getWhitelistText()));

        LOGGER.info("Client whitelist status: {}", ConfigManagerClient.isClientWhitelisted);
    }

    //Helpers
    public static String getWhitelistText() {
        return ConfigManagerClient.isClientWhitelisted
                ? "Whitelist Client: On"
                : "Whitelist Client: Off";
    }

    public static void setKeybindStatus(String type)
    {
        switch (type) {
            case "Menu":
                usingMenuKeybind = !usingMenuKeybind;
                break;

            case "Immortality":
                usingImmortalityKeybind = !usingImmortalityKeybind;
                break;

            case "Boom":
                usingBoomKeybind = !usingBoomKeybind;
                break;

            case "TP":
                usingTPKeybind = !usingTPKeybind;
                break;

            case "Dupe":
                usingDupeKeybind = !usingDupeKeybind;
                break;
        }
    }
}
