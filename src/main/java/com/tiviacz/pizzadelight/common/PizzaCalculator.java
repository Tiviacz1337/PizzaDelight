package com.tiviacz.pizzadelight.common;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.pizzadelight.tags.ModTags;
import com.tiviacz.pizzadelight.util.NBTUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.registry.ModEffects;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PizzaCalculator {
    protected ItemStack base;
    protected ItemStack sauce;
    protected ItemStackHandler ingredients;

    protected NonNullList<ItemStack> processedFoods = NonNullList.create();
    protected List<Pair<MobEffectInstance, Float>> effects = new ArrayList<>();

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
        NBTUtils.saveInventoryToStack(stack, this.ingredients);

        return stack;
    }

    public ItemStack getResultSlice(ItemStack stack) {
        resetStats();

        ingredients.setStackInSlot(9, sauce);

        for(int i = 0; i < ingredients.getSlots(); i++) {
            processFood(ingredients.getStackInSlot(i).copyWithCount(1));
        }

        int allNutrition = (this.hunger + 3) / 4 * 4;

        //FoodProperties.Builder foodProperties = new FoodProperties.Builder();
        //foodProperties.nutrition(allNutrition / 4).saturationModifier(this.saturation);
        NBTUtils.setHunger(stack, allNutrition / 4);
        NBTUtils.setSaturation(stack, this.saturation);

        if(getEffect() != null) {
            NBTUtils.setEffects(stack, List.of(Pair.of(getEffect(), 1.0F)));
            //foodProperties.effect(this::getEffect, 1.0F).alwaysEdible();
        }

        NBTUtils.saveInventoryToStack(stack, this.ingredients);
        //stack.set(DataComponents.FOOD, foodProperties.build());
        //stack.set(ModDataComponents.PIZZA_INGREDIENTS, PizzaIngredients.fromHandler(ingredients));

        return stack;
    }

    /*public ItemStack getResultSliceStack(ItemStack stack) {
        resetStats();
        ingredients.setStackInSlot(9, sauce);

        //Base
        this.hunger += 7;

        for(int i = 0; i < ingredients.getSlots(); i++) {
            processFood(ingredients.getStackInSlot(i).copyWithCount(1));
        }

        NBTUtils.saveInventoryToStack(stack, this.ingredients);
        NBTUtils.setUniqueness(stack, this.uniqueness);
        NBTUtils.setHunger(stack, this.hunger / 6);
        NBTUtils.setSaturation(stack, 0.6F);
        if(getEffect() != null) {
            NBTUtils.setEffects(stack, List.of(Pair.of(getEffect(), 1.0F)));
        }

        return stack;
    }*/

    @Nullable
    public MobEffectInstance getEffect() {

        int pointer = this.uniqueness - 9;

        if(pointer == 0) return new MobEffectInstance(ModEffects.NOURISHMENT.get(), 6000);
        if(pointer >= -3 && pointer < 0) return new MobEffectInstance(ModEffects.NOURISHMENT.get(), 3600);
        if(pointer >= -6 && pointer < -3) return new MobEffectInstance(ModEffects.COMFORT.get(), 1200);
        else return null;
    }

    public void processFood(ItemStack stack) {
        if(stack.getFoodProperties(null) == null) return;

        FoodProperties food = stack.getItem().getFoodProperties(stack, null);

        int nutrition = food.getNutrition();

        if(stack.is(ModTags.INGREDIENTS)) {
            nutrition += 2;
            saturation += 0.05F;
        }

        this.hunger += nutrition;

        if(!food.getEffects().isEmpty()) {
            List<Pair<MobEffectInstance, Float>> foodEffects = food.getEffects();

            for(Pair<MobEffectInstance, Float> possibleEffect : foodEffects) {
                if(!effects.contains(possibleEffect)) {
                    effects.add(possibleEffect);
                }
            }
        }

        if(processedFoods.stream().noneMatch(s -> ItemStack.isSameItemSameTags(s, stack))) {
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