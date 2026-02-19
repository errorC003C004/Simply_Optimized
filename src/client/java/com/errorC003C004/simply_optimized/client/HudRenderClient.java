package com.errorC003C004.simply_optimized.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class HudRenderClient {

    private static final Identifier BAD =
            Identifier.of("simply-optimized", "textures/gui/test_bad.png");
    private static final Identifier GOOD =
            Identifier.of("simply-optimized", "textures/gui/test_good.png");

    public static void init() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.of("simply_optimised", "my_hud"),
                HudRenderClient::render
        );
    }

    private static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        if (!UIFunctions.isClientWhitelisted)
        {
            return;
        }
        if (!UIFunctions.showImage) return;

        int x = 5;
        int y = 5;

        Identifier texture = UIFunctions.isgood ? GOOD : BAD;

        ctx.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x, y,
                0f, 0f,
                64, 64,
                512, 512
        );
    }
}
