package com.errorC003C004.simply_optimized.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MyScreen extends Screen {

    public ButtonWidget immortalityButton;
    public ButtonWidget showHideImageButton;

    public MyScreen() {
        super(Text.literal("My UI"));
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;
       showHideImageButton = ButtonWidget.builder(
                Text.literal(getImageText()),
                b ->
                {
                    if (this.client == null || this.client.player == null) return;
                    UIFunctions.immagebutton(this.client);
                }
        ).dimensions(centerX - 50, centerY - 24, 100, 20).build();

        immortalityButton = ButtonWidget.builder(
                Text.literal(getImmortalityText()),
                b -> {
                    if (this.client == null || this.client.player == null) return;
                    UIFunctions.immortalitybutton(this.client);
                    immortalityButton.active = false;
                }
        ).dimensions(centerX - 50, centerY, 100, 20).build();

        ButtonWidget closeButton = ButtonWidget.builder(
                Text.literal("Close"),
                b -> this.client.setScreen(null)
        ).dimensions(centerX - 50, centerY + 24, 100, 20).build();



        this.addDrawableChild(showHideImageButton);
        this.addDrawableChild(immortalityButton);
        this.addDrawableChild(closeButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {

        ctx.fill(0, 0, this.width, this.height, 0x88000000);

        super.render(ctx, mouseX, mouseY, delta);

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

    private String getImmortalityText() {
        return "Immortality: " + UIFunctions.isImmortal;
    }

    public void refreshImmortalityText() {
        if (immortalityButton != null) {
            immortalityButton.setMessage(Text.literal(getImmortalityText()));
        }
    }

    public String getImageText() {
        if (!ConfigManagerClient.isShowImage()) {
            return "Show Visualizer";
        } else {
            return "Hide Visualizer";
        }
    }

    public void refreshgetImageText() {
        if (showHideImageButton != null) {
            showHideImageButton.setMessage(Text.literal(getImageText()));
        }
    }
}