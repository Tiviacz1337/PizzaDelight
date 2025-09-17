package com.tiviacz.pizzadelight.client.tooltip;

import com.tiviacz.pizzadelight.components.PizzaIngredients;
import com.tiviacz.pizzadelight.init.ModDataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PizzaTooltipComponent implements TooltipComponent {
    protected List<ItemStack> ingredients;

    public PizzaTooltipComponent(ItemStack stack) {
        this.ingredients = stack.getOrDefault(ModDataComponents.PIZZA_INGREDIENTS, PizzaIngredients.EMPTY).getIngredients();
    }
}
