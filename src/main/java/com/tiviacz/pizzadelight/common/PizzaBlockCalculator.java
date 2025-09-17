package com.tiviacz.pizzadelight.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

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
            this.effects.add(Pair.of(getEffect(), 1.0F));
        }
    }

    public List<Pair<MobEffectInstance, Float>> findEffects() {
        this.effects.clear();
        this.processedFoods.clear();

        for(int i = 0; i < ingredients.getSlots(); i++) {
            findEffects(ingredients.getStackInSlot(i).copyWithCount(1));
        }

        if(getEffect() != null) {
            this.effects.add(Pair.of(getEffect(), 1.0F));
        }
        return this.effects;
    }

    public int getHunger() {
        return this.hunger;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public List<Pair<MobEffectInstance, Float>> getEffects() {
        return this.effects;
    }

    public void findEffects(ItemStack stack) {
        if(stack.getFoodProperties(null) == null) return;

        FoodProperties food = stack.getFoodProperties(null);

        if(!food.getEffects().isEmpty()) {
            for(Pair<MobEffectInstance, Float> possibleEffect : food.getEffects()) {
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
}