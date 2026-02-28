package com.errorC003C004.simply_optimized.items;

import net.minecraft.command.CommandRegistryAccess;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;

import net.minecraft.enchantment.Enchantments;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import net.minecraft.nbt.NbtCompound;

import net.minecraft.registry.RegistryKeys;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;

import java.util.LinkedHashSet;

public class ErrorPick {

    public static ItemStack createErrorPick(CommandRegistryAccess registryAccess) {

        ItemStack stack = new ItemStack(Items.NETHERITE_PICKAXE);

        stack.set(DataComponentTypes.CUSTOM_NAME,
                Text.literal("The One").formatted(Formatting.GOLD));

        stack.set(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE);

        NbtCompound custom = new NbtCompound();
        custom.putInt("error_pick", 1);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(custom));

        var enchantmentRegistry = registryAccess.getOrThrow(RegistryKeys.ENCHANTMENT);

        ItemEnchantmentsComponent.Builder enchBuilder =
                new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);

        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.EFFICIENCY), 255);
        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.FORTUNE), 255);
        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.MENDING), 1);
        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.UNBREAKING), 255);

        stack.set(DataComponentTypes.ENCHANTMENTS, enchBuilder.build());

        stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);

        LinkedHashSet<ComponentType<?>> hidden = new LinkedHashSet<>();
        hidden.add(DataComponentTypes.ENCHANTMENTS);
        hidden.add(DataComponentTypes.UNBREAKABLE);
        hidden.add(DataComponentTypes.ATTRIBUTE_MODIFIERS);

        stack.set(
                DataComponentTypes.TOOLTIP_DISPLAY,
                new TooltipDisplayComponent(false, hidden)
        );

        stack.set(DataComponentTypes.REPAIR_COST, 9999999);

        return stack;
    }
}