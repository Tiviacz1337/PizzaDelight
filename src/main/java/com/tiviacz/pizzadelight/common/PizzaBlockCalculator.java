package com.tiviacz.pizzadelight.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class PizzaBlockCalculator extends PizzaCalculator {
    public PizzaBlockCalculator(ItemStackHandler ingredients) {
        super(ItemStack.EMPTY, ingredients.getStackInSlot(9), ingredients);
        resetStats();
    }

    public void process() {
        resetStats();

        for(int i = 0; i < ingredients.getSlots(); i++) {
            processFood(ingredients.getStackInSlot(i).copyWithCount(1));
        }

        if(getEffect() != null) {
            this.effects.add(new FoodProperties.PossibleEffect(this::getEffect, 1.0F));
        }
    }

    public List<FoodProperties.PossibleEffect> findEffects() {
        this.effects.clear();
        this.processedFoods.clear();

        for(int i = 0; i < ingredients.getSlots(); i++) {
            findEffects(ingredients.getStackInSlot(i).copyWithCount(1));
        }

        if(getEffect() != null) {
            this.effects.add(new FoodProperties.PossibleEffect(this::getEffect, 1.0F));
        }
        return this.effects;
    }

    public int getHunger() {
        return this.hunger;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public List<FoodProperties.PossibleEffect> getEffects() {
        return this.effects;
    }

    public void findEffects(ItemStack stack) {
        if(!stack.has(DataComponents.FOOD)) return;

        FoodProperties food = stack.getItem().getFoodProperties(stack, null);

        if(!food.effects().isEmpty()) {
            for(FoodProperties.PossibleEffect possibleEffect : food.effects()) {
                if(!effects.contains(possibleEffect)) {
                    effects.add(possibleEffect);
                }
            }
        }

        if(processedFoods.stream().noneMatch(s -> ItemStack.isSameItemSameComponents(s, stack))) {
            this.uniqueness += 1;
        }
        processedFoods.add(stack);
    }
}