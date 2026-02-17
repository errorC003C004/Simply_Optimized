package com.errorC003C004.simply_optimized.client.mixins.client;

import com.errorC003C004.simply_optimized.client.UIFunctions;
import net.minecraft.client.gui.screen.option.OnlineOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.Screen;

@Mixin(OnlineOptionsScreen.class)
public abstract class OnlineOptionsScreenMixin extends Screen {

    protected OnlineOptionsScreenMixin(Text title) {
        super(title);
    }
    @Inject(method = "init", at = @At("TAIL"))
    private void addMyButton(CallbackInfo ci) {

        ButtonWidget button = ButtonWidget.builder(
                Text.literal(UIFunctions.getWhitelistText()),
                b -> UIFunctions.clientWhitelist(b)
        ).build();
        // Add using normal helper
        this.addDrawableChild(button);

        // Let vanilla reposition everything
        this.refreshWidgetPositions();
    }
}