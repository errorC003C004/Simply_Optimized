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
    public ButtonComponent MenuKeyTog;
    public ButtonComponent ImmortalityKeyTog;
    public ButtonComponent BoomKeyTog;
    public ButtonComponent TPKeyTog;
    public ButtonComponent DupeKeyTog;



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


        MenuKeyTog = button(
                Text.literal(getText("Menu")),
                button -> {
                    UIFunctions.setKeybindStatus("Menu");
                    refreshText("Menu");
                }
        );
        MenuKeyTog.sizing(Sizing.fill(100), Sizing.content());

        ImmortalityKeyTog = button(
                Text.literal(getText("Immortality")),
                button -> {
                    UIFunctions.setKeybindStatus("Immortality");
                    refreshText("Immortality");
                }
        );
        ImmortalityKeyTog.sizing(Sizing.fill(100), Sizing.content());

        BoomKeyTog = button(
                Text.literal(getText("Boom")),
                button -> {
                    UIFunctions.setKeybindStatus("Boom");
                    refreshText("Boom");
                }
        );
        BoomKeyTog.sizing(Sizing.fill(100), Sizing.content());

        TPKeyTog = button(
                Text.literal(getText("TP")),
                button -> {
                    UIFunctions.setKeybindStatus("TP");
                    refreshText("TP");
                }
        );
        TPKeyTog.sizing(Sizing.fill(100), Sizing.content());

        DupeKeyTog = button(
                Text.literal(getText("Dupe")),
                button -> {
                    UIFunctions.setKeybindStatus("Dupe");
                    refreshText("Dupe");
                }
        );
        DupeKeyTog.sizing(Sizing.fill(100), Sizing.content());

        backButton = button(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(new MainScreen())
        );
        backButton.sizing(Sizing.fill(100), Sizing.content());

        panel.child(title);
        panel.child(MenuKeyTog);
        panel.child(ImmortalityKeyTog);
        panel.child(BoomKeyTog);
        panel.child(TPKeyTog);
        panel.child(DupeKeyTog);
        panel.child(backButton);

        root.child(panel);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private String getText(String key) {
        switch (key) {
            case "Menu":
                return "Menu: " + UIFunctions.usingMenuKeybind;
            case "Immortality":
                return "Immortality: " + UIFunctions.usingImmortalityKeybind;
            case "Boom":
                return "Boom: " + UIFunctions.usingBoomKeybind;
            case "TP":
                return "TP: " + UIFunctions.usingTPKeybind;
            case "Dupe":
                return "Dupe: " + UIFunctions.usingDupeKeybind;
            default:
                return "Unknown";
        }
    }

    public void refreshText(String key) {
        ButtonComponent target = null;

        switch (key) {
            case "Menu":
                target = MenuKeyTog;
                break;
            case "Immortality":
                target = ImmortalityKeyTog;
                break;
            case "Boom":
                target = BoomKeyTog;
                break;
            case "TP":
                target = TPKeyTog;
                break;
            case "Dupe":
                target = DupeKeyTog;
                break;
        }

        if (target != null) {
            target.setMessage(Text.literal(getText(key)));
        }
    }
}