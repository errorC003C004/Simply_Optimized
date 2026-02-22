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
import net.minecraft.registry.RegistryWrapper;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;

import java.util.LinkedHashSet;

public class ErrorPick {

    public static ItemStack createErrorPick(CommandRegistryAccess registryAccess) {

        RegistryWrapper.WrapperLookup lookup = registryAccess;

        ItemStack stack = new ItemStack(Items.NETHERITE_PICKAXE);

        // Custom Name
        stack.set(DataComponentTypes.CUSTOM_NAME,
                Text.literal("The One").formatted(Formatting.GOLD));

        // Unbreakable
        stack.set(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE);

        // Custom Data
        NbtCompound custom = new NbtCompound();
        custom.putInt("error_pick", 1);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(custom));

        // Enchantments (1.21 system)
        var enchantmentRegistry = lookup.getOrThrow(RegistryKeys.ENCHANTMENT);

        ItemEnchantmentsComponent.Builder enchBuilder =
                new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);

        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.EFFICIENCY), 255);
        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.FORTUNE), 255);
        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.MENDING), 1);
        enchBuilder.add(enchantmentRegistry.getOrThrow(Enchantments.UNBREAKING), 255);

        stack.set(DataComponentTypes.ENCHANTMENTS, enchBuilder.build());

        // Force glint
        stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);

        // Hide enchantments + unbreakable + attributes in tooltip
        LinkedHashSet<ComponentType<?>> hidden = new LinkedHashSet<>();
        hidden.add(DataComponentTypes.ENCHANTMENTS);
        hidden.add(DataComponentTypes.UNBREAKABLE);
        hidden.add(DataComponentTypes.ATTRIBUTE_MODIFIERS);

        stack.set(
                DataComponentTypes.TOOLTIP_DISPLAY,
                new TooltipDisplayComponent(false, hidden)
        );

        // Repair cost
        stack.set(DataComponentTypes.REPAIR_COST, 9999999);

        return stack;
    }
}