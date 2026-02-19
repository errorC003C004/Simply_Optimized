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

What to Fix:
* / Commands not loading when user loads in server.
* When toggling Immortality, detected clients are removed.


 */
public class Simply_optimized implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("simply_optimized");
    @Override
    public void onInitialize() {
        ConfigManager.loadConfig();
        ConfigManager.init();
        CommandInit.register();

        PayloadTypeRegistry.playC2S().register(
                PingPayload.ID,
                PingPayload.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
                ImmortalityStatusPayload.ID,
                ImmortalityStatusPayload.CODEC
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
