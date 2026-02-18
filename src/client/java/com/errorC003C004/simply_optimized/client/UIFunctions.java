package com.errorC003C004.simply_optimized.client;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.errorC003C004.simply_optimized.networking.ClientActionPayload;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class UIFunctions {
    //Bools
    public static boolean isgood = true;
    public static boolean showImage = true;

    //OnlineThingCheck
    public static boolean isClientWhitelisted = false;
    public static boolean isImmortal = false;

    public static boolean WhitelistCheck(FabricClientCommandSource source) {
        return isClientWhitelisted;
    }

    //Buttons
    public static void immortalitybutton(MinecraftClient client) {
        System.out.println("Button pressed, Immortality");
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        ClientPlayNetworking.send(new ClientActionPayload(ClientActionPayload.Action.IMMORTALITY_TOGGLE));

        //client.player.sendMessage(Text.literal("Immortality (Client)"), false);
    }


    public static void clientWhitelist(ButtonWidget button) {
        isClientWhitelisted = !isClientWhitelisted;

        button.setMessage(Text.literal(getWhitelistText()));

        System.out.println(
                "Is Client Whitelisted is now " + isClientWhitelisted
        );
    }



    //Helpers
    public static String getWhitelistText() {
        return isClientWhitelisted
                ? "Whitelist Client: On"
                : "Whitelist Client: Off";
    }
}
