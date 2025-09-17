package com.tiviacz.pizzadelight.init;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    //Vegetables
    public static final FoodProperties ONION_SLICE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).fast().build();
    public static final FoodProperties TOMATO_SLICE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).fast().build();
    public static final FoodProperties MUSHROOM_SLICE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).fast().build();

    //Cheese
    public static final FoodProperties CHEESE = new FoodProperties.Builder().nutrition(4).saturationModifier(1.6F).build();
}