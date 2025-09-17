package com.tiviacz.pizzadelight.init;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class ModVanillaCompat {
    public static void setup() {
        registerCompostable(0.3F, ModItems.TOMATO_SLICE.get());
        registerCompostable(0.3F, ModItems.ONION_SLICE.get());
        registerCompostable(0.3F, ModItems.MUSHROOM_SLICE.get());
    }

    public static void registerCompostable(float chance, ItemLike itemIn) {
        ComposterBlock.COMPOSTABLES.put(itemIn.asItem(), chance);
    }
}