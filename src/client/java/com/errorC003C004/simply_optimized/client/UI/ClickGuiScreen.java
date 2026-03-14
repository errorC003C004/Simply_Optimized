package com.errorC003C004.simply_optimized.client.UI;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import org.jetbrains.annotations.NotNull;

import static io.wispforest.owo.ui.component.UIComponents.*;

public class ClickGuiScreen extends BaseOwoScreen<FlowLayout> {

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, RootLayout::horizontalFlow);
    }

    private static class RootLayout extends FlowLayout {

        public static FlowLayout verticalFlow(Sizing horizontalSizing, Sizing verticalSizing) {
            return new ClickGuiScreen.RootLayout(horizontalSizing, verticalSizing, Algorithm.VERTICAL);
        }

        public static FlowLayout horizontalFlow(Sizing horizontalSizing, Sizing verticalSizing) {
            return new ClickGuiScreen.RootLayout(horizontalSizing, verticalSizing, Algorithm.HORIZONTAL);
        }

        protected RootLayout(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
            super(horizontalSizing, verticalSizing, algorithm);
        }
    }

    @Override
    protected void build(FlowLayout root) {

        root.sizing(Sizing.fill(100), Sizing.fill(100));
        root.gap(10);
        root.padding(Insets.of(10));

        root.child(createCombatPanel());
        root.child(createRenderPanel());
        root.child(createMiscPanel());
    }

    private FlowLayout createCombatPanel() {

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(140), Sizing.content());

        panel.surface(Surface.VANILLA_TRANSLUCENT);
        panel.padding(Insets.of(6));
        panel.gap(4);

        panel.child(label(Text.literal("Combat")));

        panel.child(button(Text.literal("Immortality: " + UIFunctions.isImmortal), b -> {
            UIFunctions.immortalitybutton(MinecraftClient.getInstance());
        }));

        panel.child(button(Text.literal("Instakill: " + UIFunctions.isInstakill), b -> {
            UIFunctions.instakillbutton(MinecraftClient.getInstance());
        }));

        panel.child(button(Text.literal("Armor Bypass: " + UIFunctions.isArmorBypass), b -> {
            UIFunctions.armorbypassbutton(MinecraftClient.getInstance());
        }));

        return panel;
    }

    private FlowLayout createRenderPanel() {

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(140), Sizing.content());

        panel.surface(Surface.VANILLA_TRANSLUCENT);
        panel.padding(Insets.of(6));
        panel.gap(4);

        panel.child(label(Text.literal("Render")));

        panel.child(button(Text.literal("Visualizer"), b -> {
            UIFunctions.visualizerbutton(MinecraftClient.getInstance());
        }));

        return panel;
    }

    private FlowLayout createMiscPanel() {

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(140), Sizing.content());

        panel.surface(Surface.VANILLA_TRANSLUCENT);
        panel.padding(Insets.of(6));
        panel.gap(4);

        panel.child(label(Text.literal("Misc")));

        panel.child(button(Text.literal("TP"), b -> {
            UIFunctions.tploookbutton(MinecraftClient.getInstance());
        }));

        panel.child(button(Text.literal("Boom"), b -> {
            UIFunctions.boombutton(MinecraftClient.getInstance());
        }));

        panel.child(button(Text.literal("Dupe"), b -> {
            UIFunctions.dupebutton(MinecraftClient.getInstance());
        }));

        return panel;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}