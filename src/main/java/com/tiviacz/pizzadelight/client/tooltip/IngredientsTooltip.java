package com.tiviacz.pizzadelight.client.tooltip;

import com.tiviacz.pizzadelight.tags.ModTags;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsTooltip implements TooltipComponent {
    protected ArrayList<ItemStack> doughs = new ArrayList<>();
    protected ArrayList<ItemStack> sauces = new ArrayList<>();
    protected ArrayList<ItemStack> ingredients = new ArrayList<>();

    public IngredientsTooltip() {
    }

    public ArrayList<ItemStack> getIngredients() {
        ingredients.addAll(Arrays.asList(Ingredient.of(ModTags.INGREDIENTS).getItems()));
        return ingredients;
    }

    public ArrayList<ItemStack> getDoughs() {
        doughs.addAll(Arrays.asList(Ingredient.of(ModTags.DOUGH).getItems()));
        return doughs;
    }

    public ArrayList<ItemStack> getSauces() {
        sauces.addAll(Arrays.asList(Ingredient.of(ModTags.SAUCE).getItems()));
        return sauces;
    }
}