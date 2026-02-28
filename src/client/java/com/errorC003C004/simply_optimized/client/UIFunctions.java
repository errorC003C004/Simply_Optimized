package com.errorC003C004.simply_optimized.client;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.errorC003C004.simply_optimized.networking.ClientActionPayload;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIFunctions {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");

    public static boolean isImmortal = false;

    public static boolean WhitelistCheck(FabricClientCommandSource source) {
        return ConfigManagerClient.isClientWhitelisted;
    }

    //Buttons
    public static void immagebutton(MinecraftClient client) {
        LOGGER.info("Button pressed, Image");
        if (ConfigManagerClient.showImage) {
            ConfigManagerClient.showImageFalse();
        } else {
            ConfigManagerClient.showImageTrue();
        }
        if (client.currentScreen instanceof MyScreen screen) {
            screen.refreshgetImageText();
        }
    }

    public static void immortalitybutton(MinecraftClient client) {
        LOGGER.info("Button pressed, Immortality");
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.IMMORTALITY_TOGGLE));

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
}
