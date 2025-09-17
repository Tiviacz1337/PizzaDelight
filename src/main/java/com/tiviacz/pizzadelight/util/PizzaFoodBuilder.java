package com.tiviacz.pizzadelight.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Supplier;

public class PizzaFoodBuilder {
    private int nutrition;
    private float saturationModifier;
    private boolean canAlwaysEat;
    private float eatSeconds = 1.6F;
    private Optional<ItemStack> usingConvertsTo = Optional.empty();
    private final ImmutableList.Builder<FoodProperties.PossibleEffect> effects = ImmutableList.builder();

    public PizzaFoodBuilder nutrition(int nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public PizzaFoodBuilder saturationModifier(float saturationModifier) {
        this.saturationModifier = saturationModifier;
        return this;
    }

    public PizzaFoodBuilder alwaysEdible() {
        this.canAlwaysEat = true;
        return this;
    }

    public PizzaFoodBuilder fast() {
        this.eatSeconds = 0.8F;
        return this;
    }

    public PizzaFoodBuilder effect(Supplier<MobEffectInstance> effectIn, float probability) {
        this.effects.add(new FoodProperties.PossibleEffect(effectIn, probability));
        return this;
    }

    public PizzaFoodBuilder usingConvertsTo(ItemLike item) {
        this.usingConvertsTo = Optional.of(new ItemStack(item));
        return this;
    }

    public FoodProperties build() {
        return new FoodProperties(this.nutrition, this.saturationModifier, this.canAlwaysEat, this.eatSeconds, this.usingConvertsTo, this.effects.build());
    }
}