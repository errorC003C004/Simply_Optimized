package com.errorC003C004.simply_optimized.client;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import org.jetbrains.annotations.NotNull;

import static io.wispforest.owo.ui.component.UIComponents.button;
import static io.wispforest.owo.ui.component.UIComponents.label;

public class MyScreen extends BaseOwoScreen<FlowLayout> {

    public ButtonComponent immortalityButton;
    public ButtonComponent showHideImageButton;
    public ButtonComponent keybindTogglebutton;
    public ButtonComponent closeButton;

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, RootLayout::verticalFlow);
    }

    private static class RootLayout extends FlowLayout {

        public static FlowLayout verticalFlow(Sizing horizontalSizing, Sizing verticalSizing) {
            return new RootLayout(horizontalSizing, verticalSizing, Algorithm.VERTICAL);
        }

        public static FlowLayout horizontalFlow(Sizing horizontalSizing, Sizing verticalSizing) {
            return new RootLayout(horizontalSizing, verticalSizing, Algorithm.HORIZONTAL);
        }

        protected RootLayout(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
            super(horizontalSizing, verticalSizing, algorithm);
        }
    }

    @Override
    protected void build(FlowLayout root) {
        root.sizing(Sizing.fill(100), Sizing.fill(100));
        root.gap(6);
        root.horizontalAlignment(HorizontalAlignment.CENTER);
        root.verticalAlignment(VerticalAlignment.CENTER);

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(190), Sizing.content());
        panel.gap(8);
        panel.padding(Insets.of(10));
        panel.horizontalAlignment(HorizontalAlignment.CENTER);
        panel.surface(Surface.VANILLA_TRANSLUCENT);

        LabelComponent title = label(Text.literal("Error's Client Mod"));
        title.margins(Insets.bottom(4));

        showHideImageButton = button(
                Text.literal(getImageText()),
                button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.visualizerbutton(client);
                    refreshgetImageText();
                }
        );
        showHideImageButton.sizing(Sizing.fill(100), Sizing.content());

        immortalityButton = button(
                Text.literal(getImmortalityText()),
                button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.immortalitybutton(client);
                    immortalityButton.active(false);
                    refreshImmortalityText();
                }
        );
        immortalityButton.sizing(Sizing.fill(100), Sizing.content());

        keybindTogglebutton = button(
                Text.literal(getKeybindText()),
                button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.KeybindTogglebutton(client);
                    refreshKeybindText();
                }
        );
        keybindTogglebutton.sizing(Sizing.fill(100), Sizing.content());

        closeButton = button(
                Text.literal("Close"),
                button -> MinecraftClient.getInstance().setScreen(null)
        );
        closeButton.sizing(Sizing.fill(100), Sizing.content());
        closeButton.margins(Insets.top(4));

        panel.child(title);
        panel.child(showHideImageButton);
        panel.child(immortalityButton);
        panel.child(keybindTogglebutton);
        panel.child(closeButton);

        root.child(panel);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private String getImmortalityText() {
        return "Immortality: " + UIFunctions.isImmortal;
    }

    private String getKeybindText() {
        return "Keybinds: " + UIFunctions.usingKeybinds;
    }

    public void refreshImmortalityText() {
        if (immortalityButton != null) {
            immortalityButton.setMessage(Text.literal(getImmortalityText()));
        }
    }

    public void refreshKeybindText() {
        if (keybindTogglebutton != null) {
            keybindTogglebutton.setMessage(Text.literal(getKeybindText()));
        }
    }

    public String getImageText() {
        return ConfigManagerClient.isShowVisualizer() ? "Hide Visualizer" : "Show Visualizer";
    }

    public void refreshgetImageText() {
        if (showHideImageButton != null) {
            showHideImageButton.setMessage(Text.literal(getImageText()));
        }
    }
}