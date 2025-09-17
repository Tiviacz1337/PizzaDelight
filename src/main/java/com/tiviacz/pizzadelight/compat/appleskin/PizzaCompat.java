package com.tiviacz.pizzadelight.compat.appleskin;

import com.tiviacz.pizzadelight.items.PizzaBlockItem;
import com.tiviacz.pizzadelight.items.PizzaSliceItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.common.MinecraftForge;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

public class PizzaCompat {
    public static void load() {
        MinecraftForge.EVENT_BUS.addListener(PizzaCompat::foodValuesEvent);
    }

    public static void foodValuesEvent(FoodValuesEvent event) {
        if(event.itemStack.getItem() instanceof PizzaSliceItem) {
            FoodProperties properties = event.itemStack.getItem().getFoodProperties(event.itemStack, event.player);
            event.defaultFoodValues = new FoodValues(properties.getNutrition(), properties.getSaturationModifier());
            event.modifiedFoodValues = event.defaultFoodValues;
        }

        if(event.itemStack.getItem() instanceof PizzaBlockItem) {
            FoodProperties properties = event.itemStack.getItem().getFoodProperties(event.itemStack, event.player);
            event.defaultFoodValues = new FoodValues(properties.getNutrition(), properties.getSaturationModifier());
            event.modifiedFoodValues = event.defaultFoodValues;
        }
    }
}