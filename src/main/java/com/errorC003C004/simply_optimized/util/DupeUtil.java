package com.errorC003C004.simply_optimized.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class DupeUtil {

    public static void duplicateHeldItem(PlayerEntity player) {
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        if (!stack.isEmpty()) {
            stack.setCount(stack.getCount() * 2);
        }
    }
}