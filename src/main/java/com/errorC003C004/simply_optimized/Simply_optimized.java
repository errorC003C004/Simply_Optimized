package com.errorC003C004.simply_optimized;

import com.errorC003C004.simply_optimized.networking.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.EnvType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*


What to Add:
*


nvm:
* I think I been sending chat logs to actual chat and not client's chat box.

What to Fix:
* Detected Clients UUIDs not getting the IsAuthorized Commands
* When Player Rejoins while Immortal, taking off immortal doesnt work i think

For Client OnlineOptionsMixin:
* Make it send a handshake so server knows to show command instead of just showing it to anywhere w/ client

Make isAuthorized check the button


 */
public class Simply_optimized implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    @Override
    public void onInitialize() {
        ConfigManager.load();
        CommandInit.loadConfig();
        CommandInit.init();
        CommandInit.register();

        PayloadTypeRegistry.playC2S().register(
                PingPayload.ID,
                PingPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                ClientActionPayload.ID,
                ClientActionPayload.CODEC
        );

        HandshakeServer.init();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            LOGGER.info("[SimplyOptimised] Hey there, your on a client, uhh replace me");
        }
        else {
            LOGGER.info("[SimplyOptimised] Hey there, your on a server, uhh replace me");
        }
    }
}
