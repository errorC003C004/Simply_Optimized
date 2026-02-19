package com.errorC003C004.simply_optimized.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MyScreen extends Screen {

    public MyScreen() {
        super(Text.literal("My UI"));
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.addDrawableChild(
                ButtonWidget.builder(
                                Text.literal("Show/Hide Image"),
                                b -> UIFunctions.showImage = !UIFunctions.showImage
                        )
                        .dimensions(centerX - 50, centerY - 24, 100, 20)
                        .build()
        );
        this.addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Immortality:" + UIFunctions.isImmortal),
                        button -> {
                            if (this.client == null || this.client.player == null) return;
                            UIFunctions.immortalitybutton(this.client);
                        }
                ).dimensions(centerX - 50, centerY, 100, 20).build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Close"),
                        b -> this.client.setScreen(null)
                ).dimensions(centerX - 50, centerY + 24, 100, 20).build()
        );
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {

        // background
        ctx.fill(0, 0, this.width, this.height, 0x88000000);

        // buttons + widgets
        super.render(ctx, mouseX, mouseY, delta);

        // title text
        ctx.drawCenteredTextWithShadow(
                this.textRenderer,
                "SCREEN OPENED",
                this.width / 2,
                this.height / 2 - 50,
                0xFFFFFF
        );
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}