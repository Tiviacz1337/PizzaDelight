package com.tiviacz.pizzadelight.common;

import com.tiviacz.pizzadelight.components.PizzaIngredients;
import com.tiviacz.pizzadelight.init.ModDataComponents;
import com.tiviacz.pizzadelight.tags.ModTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.registry.ModEffects;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PizzaCalculator {
    protected ItemStack base;
    protected ItemStack sauce;
    protected ItemStackHandler ingredients;

    protected NonNullList<ItemStack> processedFoods = NonNullList.create();
    protected List<FoodProperties.PossibleEffect> effects = new ArrayList<>();

    //Default
    protected int uniqueness = 0;
    protected int hunger = 4;
    protected float saturation = 0.6F;

    public PizzaCalculator(ItemStack base, ItemStack sauce, ItemStackHandler ingredients) {
        this.base = base;
        this.sauce = sauce;
        this.ingredients = ingredients;
    }

    public ItemStack getResultStackBlock(ItemStack stack) {
        resetStats();

        ingredients.setStackInSlot(9, sauce);
        stack.set(ModDataComponents.PIZZA_INGREDIENTS, PizzaIngredients.fromHandler(ingredients));

        return stack;
    }

    public ItemStack getResultSlice(ItemStack stack) {
        resetStats();

        ingredients.setStackInSlot(9, sauce);

        for(int i = 0; i < ingredients.getSlots(); i++) {
            processFood(ingredients.getStackInSlot(i).copyWithCount(1));
        }

        int allNutrition = (this.hunger + 3) / 4 * 4;

        FoodProperties.Builder foodProperties = new FoodProperties.Builder();
        foodProperties.nutrition(allNutrition / 4).saturationModifier(this.saturation);

        if(getEffect() != null) {
            foodProperties.effect(this::getEffect, 1.0F).alwaysEdible();
        }

        stack.set(DataComponents.FOOD, foodProperties.build());
        stack.set(ModDataComponents.PIZZA_INGREDIENTS, PizzaIngredients.fromHandler(ingredients));

        return stack;
    }

    @Nullable
    public MobEffectInstance getEffect() {

        int pointer = this.uniqueness - 9;

        if(pointer == 0) return new MobEffectInstance(ModEffects.NOURISHMENT, 6000);
        if(pointer >= -3 && pointer < 0) return new MobEffectInstance(ModEffects.NOURISHMENT, 3600);
        if(pointer >= -6 && pointer < -3) return new MobEffectInstance(ModEffects.COMFORT, 1200);
        else return null;
    }

    public void processFood(ItemStack stack) {
        if(!stack.has(DataComponents.FOOD)) return;

        FoodProperties food = stack.getItem().getFoodProperties(stack, null);

        int nutrition = food.nutrition();

        if(stack.is(ModTags.INGREDIENTS)) {
            nutrition += 2;
            saturation += 0.05F;
        }

        this.hunger += nutrition;

        if(!food.effects().isEmpty()) {
            List<FoodProperties.PossibleEffect> foodEffects = food.effects();

            for(FoodProperties.PossibleEffect possibleEffect : foodEffects) {
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

    public void resetStats() {
        this.uniqueness = 0;
        this.hunger = 4;
        this.saturation = 0.6F;
        this.effects.clear();
        processedFoods.clear();
    }
}