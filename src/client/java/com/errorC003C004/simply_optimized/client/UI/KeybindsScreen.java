package com.errorC003C004.simply_optimized.client.UI;

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

public class KeybindsScreen extends BaseOwoScreen<FlowLayout> {

    public ButtonComponent backButton;


    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, RootLayout::verticalFlow);
    }

    private static class RootLayout extends FlowLayout {

        public static FlowLayout verticalFlow(Sizing horizontalSizing, Sizing verticalSizing) {
            return new RootLayout(horizontalSizing, verticalSizing, Algorithm.VERTICAL);
        }

        protected RootLayout(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
            super(horizontalSizing, verticalSizing, algorithm);
        }
    }

    @Override
    protected void build(FlowLayout root) {

        root.sizing(Sizing.fill(100), Sizing.fill(100));
        root.horizontalAlignment(HorizontalAlignment.CENTER);
        root.verticalAlignment(VerticalAlignment.CENTER);

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(190), Sizing.content());

        panel.padding(Insets.of(10));
        panel.gap(8);
        panel.horizontalAlignment(HorizontalAlignment.CENTER);
        panel.surface(Surface.VANILLA_TRANSLUCENT);

        LabelComponent title = label(Text.literal("Keybinds"));
        title.margins(Insets.bottom(4));

        backButton = button(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(new MainScreen())
        );

        backButton.sizing(Sizing.fill(100), Sizing.content());

        panel.child(backButton);

        root.child(panel);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}