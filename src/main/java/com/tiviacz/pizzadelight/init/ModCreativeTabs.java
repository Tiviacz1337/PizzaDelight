package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.common.PizzaCalculator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PizzaDelight.MODID);

    public static RegistryObject<CreativeModeTab> PIZZA_DELIGHT = CREATIVE_MODE_TABS.register("pizzadelight", () -> CreativeModeTab.builder()
            .icon(ModCreativeTabs::createIcon)
            .title(Component.translatable("itemGroup.pizzadelight")).displayItems(ModCreativeTabs::displayItems).build());

    public static ItemStack createIcon() {
        ItemStack stack = ModItems.PIZZA_SLICE.get().getDefaultInstance();
        return stack;
    }

    public static void displayItems(CreativeModeTab.ItemDisplayParameters displayParameters, CreativeModeTab.Output output) {
        output.accept(ModBlocks.PIZZA_STATION.get());
        output.accept(ModBlocks.BASIN.get());

        output.accept(ModItems.CHEF_HAT.get());
        output.accept(ModItems.CHEF_SHIRT.get());
        output.accept(ModItems.CHEF_LEGGINGS.get());
        output.accept(ModItems.CHEF_BOOTS.get());

        output.accept(ModItems.STONE_PIZZA_PEEL.get());
        output.accept(ModItems.GOLDEN_PIZZA_PEEL.get());
        output.accept(ModItems.IRON_PIZZA_PEEL.get());
        output.accept(ModItems.DIAMOND_PIZZA_PEEL.get());
        output.accept(ModItems.NETHERITE_PIZZA_PEEL.get());

        output.accept(ModItems.ROLLING_PIN.get());

        output.accept(ModBlocks.RAW_PIZZA.get());
        output.accept(createPizza());
        output.accept(createMargherita());
        output.accept(createCapricciosa());
        output.accept(createMeatFeast());

        output.accept(createSlice());
        output.accept(createCapricciosaSlice());
        output.accept(createMeatFeastSlice());

        output.accept(ModBlocks.CHEESE_BLOCK.get());
        output.accept(ModItems.CHEESE.get());

        output.accept(ModItems.TOMATO_SLICE.get());
        output.accept(ModItems.ONION_SLICE.get());
        output.accept(ModItems.MUSHROOM_SLICE.get());
    }

    public static ItemStack createPizza() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        return calc.getResultStackBlock(ModItems.PIZZA.get().getDefaultInstance());
    }

    public static ItemStack createSlice() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        return calc.getResultSlice(ModItems.PIZZA_SLICE.get().getDefaultInstance());
    }

    public static ItemStack createCapricciosaSlice() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        handler.setStackInSlot(1, vectorwing.farmersdelight.common.registry.ModItems.TOMATO_SAUCE.get().getDefaultInstance());
        handler.setStackInSlot(2, ModItems.MUSHROOM_SLICE.get().getDefaultInstance());
        handler.setStackInSlot(3, vectorwing.farmersdelight.common.registry.ModItems.HAM.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        ItemStack stack = calc.getResultSlice(ModItems.PIZZA_SLICE.get().getDefaultInstance());
        stack.setHoverName(Component.literal("Capricciosa Slice"));
        return stack;
    }

    public static ItemStack createMeatFeastSlice() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        handler.setStackInSlot(1, vectorwing.farmersdelight.common.registry.ModItems.TOMATO_SAUCE.get().getDefaultInstance());
        handler.setStackInSlot(2, vectorwing.farmersdelight.common.registry.ModItems.HAM.get().getDefaultInstance());
        handler.setStackInSlot(3, vectorwing.farmersdelight.common.registry.ModItems.BACON.get().getDefaultInstance());
        handler.setStackInSlot(4, vectorwing.farmersdelight.common.registry.ModItems.MINCED_BEEF.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        ItemStack stack = calc.getResultSlice(ModItems.PIZZA_SLICE.get().getDefaultInstance());
        stack.setHoverName(Component.literal("Meat Feast Slice"));
        return stack;
    }

    public static ItemStack createMargherita() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        handler.setStackInSlot(1, vectorwing.farmersdelight.common.registry.ModItems.TOMATO_SAUCE.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        ItemStack stack = calc.getResultStackBlock(ModItems.PIZZA.get().getDefaultInstance());
        stack.setHoverName(Component.literal("Margherita"));
        return stack;
    }

    public static ItemStack createCapricciosa() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        handler.setStackInSlot(1, vectorwing.farmersdelight.common.registry.ModItems.TOMATO_SAUCE.get().getDefaultInstance());
        handler.setStackInSlot(2, ModItems.MUSHROOM_SLICE.get().getDefaultInstance());
        handler.setStackInSlot(3, vectorwing.farmersdelight.common.registry.ModItems.HAM.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        ItemStack stack = calc.getResultStackBlock(ModItems.PIZZA.get().getDefaultInstance());
        stack.setHoverName(Component.literal("Capricciosa"));
        return stack;
    }

    public static ItemStack createMeatFeast() {
        ItemStackHandler handler = new ItemStackHandler(10);
        handler.setStackInSlot(0, ModItems.CHEESE.get().getDefaultInstance());
        handler.setStackInSlot(1, vectorwing.farmersdelight.common.registry.ModItems.TOMATO_SAUCE.get().getDefaultInstance());
        handler.setStackInSlot(2, vectorwing.farmersdelight.common.registry.ModItems.HAM.get().getDefaultInstance());
        handler.setStackInSlot(3, vectorwing.farmersdelight.common.registry.ModItems.BACON.get().getDefaultInstance());
        handler.setStackInSlot(4, vectorwing.farmersdelight.common.registry.ModItems.MINCED_BEEF.get().getDefaultInstance());
        PizzaCalculator calc = new PizzaCalculator(vectorwing.farmersdelight.common.registry.ModItems.WHEAT_DOUGH.get().getDefaultInstance(), ItemStack.EMPTY, handler);
        ItemStack stack = calc.getResultStackBlock(ModItems.PIZZA.get().getDefaultInstance());
        stack.setHoverName(Component.literal("Meat Feast"));
        return stack;
    }
}