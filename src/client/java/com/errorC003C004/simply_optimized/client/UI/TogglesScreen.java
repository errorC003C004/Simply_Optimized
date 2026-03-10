package com.errorC003C004.simply_optimized.client.UI;

import com.errorC003C004.simply_optimized.client.ConfigManagerClient;
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

public class TogglesScreen extends BaseOwoScreen<FlowLayout> {

    public ButtonComponent visualizerButton;
    public ButtonComponent immortalityButton;
    public ButtonComponent noAggroButton;
    public ButtonComponent instakillButton;
    public ButtonComponent armorBypassButton;
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

        LabelComponent title = label(Text.literal("Toggles"));
        title.margins(Insets.bottom(4));


        visualizerButton = button(
                Text.literal(getVisualizerText()),
                b ->{
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.visualizerbutton(client);
                    refreshVisualizerText();
                }
        );
        visualizerButton.sizing(Sizing.fill(100), Sizing.content());

        immortalityButton = button(
                Text.literal(getImmortalityText()),
                b ->{
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.immortalitybutton(client);
                    immortalityButton.active(false);
                    refreshImmortalityText();
                }
        );
        immortalityButton.sizing(Sizing.fill(100), Sizing.content());

        noAggroButton = button(
                Text.literal(getNoAggroText()),
                b ->{
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.noaggrobutton(client);
                    noAggroButton.active(false);
                    refreshNoAggroText();
                }
        );
        noAggroButton.sizing(Sizing.fill(100), Sizing.content());

        instakillButton = button(
                Text.literal(getInstakillText()),
                b ->{
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.instakillbutton(client);
                    instakillButton.active(false);
                    refreshInstakillText();
                }
        );
        instakillButton.sizing(Sizing.fill(100), Sizing.content());

        armorBypassButton = button(
                Text.literal(getArmorBypassText()),
                b ->{
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null) return;

                    UIFunctions.armorbypassbutton(client);
                    armorBypassButton.active(false);
                    refreshArmorBypassText();
                }
        );
        armorBypassButton.sizing(Sizing.fill(100), Sizing.content());

        backButton = button(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(new MainScreen())
        );

        backButton.sizing(Sizing.fill(100), Sizing.content());

        panel.child(visualizerButton);
        panel.child(immortalityButton);
        panel.child(noAggroButton);
        panel.child(instakillButton);
        panel.child(armorBypassButton);
        panel.child(backButton);

        root.child(panel);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
    public String getVisualizerText() { return ConfigManagerClient.isShowVisualizer() ? "Hide Visualizer" : "Show Visualizer";}
    private String getImmortalityText() {return "Immortality: " + UIFunctions.isImmortal;}
    private String getNoAggroText() {return "No Aggro: " + UIFunctions.isNoAggro;}
    private String getInstakillText() {return "Instakill: " + UIFunctions.isInstakill;}
    private String getArmorBypassText() {return "Armor Bypass: " + UIFunctions.isArmorBypass;}

    public void refreshVisualizerText() {
        if (visualizerButton != null) {
            visualizerButton.setMessage(Text.literal(getVisualizerText()));
        }
    }

    public void refreshImmortalityText() {
        if (immortalityButton != null) {
            immortalityButton.setMessage(Text.literal(getImmortalityText()));
        }
    }

    public void refreshNoAggroText() {
        if (noAggroButton != null) {
            noAggroButton.setMessage(Text.literal(getNoAggroText()));
        }
    }

    public void refreshInstakillText() {
        if (instakillButton != null) {
            instakillButton.setMessage(Text.literal(getInstakillText()));
        }
    }

    public void refreshArmorBypassText() {
        if (armorBypassButton != null) {
            armorBypassButton.setMessage(Text.literal(getArmorBypassText()));
        }
    }
}