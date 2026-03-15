package com.errorC003C004.simply_optimized.client.UI;

import com.errorC003C004.simply_optimized.client.ConfigManagerClient;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.core.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import org.jetbrains.annotations.NotNull;

import static io.wispforest.owo.ui.component.UIComponents.*;

public class ClickGuiScreen extends BaseOwoScreen<FlowLayout> {

    public ButtonComponent immortalityButton;
    public ButtonComponent instakillButton;
    public ButtonComponent noAggroButton;
    public ButtonComponent armorBypassButton;
    public ButtonComponent visualizerButton;

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, RootLayout::horizontalFlow);
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

        immortalityButton = button(
                Text.literal(getImmortalityText()),
                b -> {
                    UIFunctions.immortalitybutton(MinecraftClient.getInstance());
                    immortalityButton.active(false);
                    refreshImmortalityText();
                }
        );

        instakillButton = button(
                Text.literal(getInstakillText()),
                b -> {
                    UIFunctions.instakillbutton(MinecraftClient.getInstance());
                    instakillButton.active(false);
                    refreshInstakillText();
                }
        );

        noAggroButton =  button(
                Text.literal(getNoAggroText()),
                b -> {
                    UIFunctions.noaggrobutton(MinecraftClient.getInstance());
                    noAggroButton.active(false);
                    refreshNoAggroText();
                }
        );

        armorBypassButton = button(
                Text.literal(getArmorBypassText()),
                b -> {
                    UIFunctions.armorbypassbutton(MinecraftClient.getInstance());
                    armorBypassButton.active(false);
                    refreshArmorBypassText();
                }
        );

        panel.child(immortalityButton);
        panel.child(instakillButton);
        panel.child(noAggroButton);
        panel.child(armorBypassButton);

        return panel;
    }

    private FlowLayout createRenderPanel() {

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(140), Sizing.content());

        panel.surface(Surface.VANILLA_TRANSLUCENT);
        panel.padding(Insets.of(6));
        panel.gap(4);

        panel.child(label(Text.literal("Render")));

        visualizerButton = button(
                Text.literal(getVisualizerText()),
                b -> {
                    UIFunctions.visualizerbutton(MinecraftClient.getInstance());
                    refreshVisualizerText();
                }
        );

        panel.child(visualizerButton);

        return panel;
    }

    private FlowLayout createMiscPanel() {

        FlowLayout panel = RootLayout.verticalFlow(Sizing.fixed(140), Sizing.content());

        panel.surface(Surface.VANILLA_TRANSLUCENT);
        panel.padding(Insets.of(6));
        panel.gap(4);

        panel.child(label(Text.literal("Misc")));

        panel.child(button(Text.literal("TP"), b ->
                UIFunctions.tploookbutton(MinecraftClient.getInstance())
        ));

        panel.child(button(Text.literal("Boom"), b ->
                UIFunctions.boombutton(MinecraftClient.getInstance())
        ));

        panel.child(button(Text.literal("Dupe"), b ->
                UIFunctions.dupebutton(MinecraftClient.getInstance())
        ));

        return panel;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    /* ---------- TEXT METHODS ---------- */

    public String getImmortalityText() {
        return "Immortality: " + UIFunctions.isImmortal;
    }

    public String getInstakillText() {
        return "Instakill: " + UIFunctions.isInstakill;
    }

    public String getNoAggroText() {
        return "No Aggro: " + UIFunctions.isNoAggro;
    }

    public String getArmorBypassText() {
        return "Armor Bypass: " + UIFunctions.isArmorBypass;
    }

    public String getVisualizerText() {
        return ConfigManagerClient.isShowVisualizer() ? "Hide Visualizer" : "Show Visualizer";
    }

    public void refreshImmortalityText() {
        if (immortalityButton != null) {
            immortalityButton.setMessage(Text.literal(getImmortalityText()));
        }
    }

    public void refreshInstakillText() {
        if (instakillButton != null) {
            instakillButton.setMessage(Text.literal(getInstakillText()));
        }
    }

    public void refreshNoAggroText() {
        if (noAggroButton != null) {
            noAggroButton.setMessage(Text.literal(getNoAggroText()));
        }
    }

    public void refreshArmorBypassText() {
        if (armorBypassButton != null) {
            armorBypassButton.setMessage(Text.literal(getArmorBypassText()));
        }
    }

    public void refreshVisualizerText() {
        if (visualizerButton != null) {
            visualizerButton.setMessage(Text.literal(getVisualizerText()));
        }
    }
}