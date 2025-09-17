package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.items.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PizzaDelight.MODID);

    public static final DeferredItem<Item> PIZZA = ITEMS.register("pizza", () -> new PizzaBlockItem(ModBlocks.PIZZA.get(), pizzaProperties().stacksTo(1)));
    public static final DeferredItem<Item> RAW_PIZZA = ITEMS.register("raw_pizza", () -> new RawPizzaBlockItem(ModBlocks.RAW_PIZZA.get(), pizzaProperties().stacksTo(1)));
    public static final DeferredItem<Item> PIZZA_STATION = registerBlockItem("pizza_station", ModBlocks.PIZZA_STATION, pizzaProperties());
    public static final DeferredItem<Item> BASIN = registerBlockItem("basin", ModBlocks.BASIN, pizzaProperties());

    //Outfits
    public static final DeferredItem<Item> CHEF_HAT = ITEMS.register("chef_hat", () -> new ChefArmor(ModArmorMaterials.CHEF_ARMOR_MATERIAL, ArmorItem.Type.HELMET, pizzaProperties().durability(ArmorItem.Type.HELMET.getDurability(5))));
    public static final DeferredItem<Item> CHEF_SHIRT = ITEMS.register("chef_shirt", () -> new ChefArmor(ModArmorMaterials.CHEF_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, pizzaProperties().durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
    public static final DeferredItem<Item> CHEF_LEGGINGS = ITEMS.register("chef_leggings", () -> new ChefArmor(ModArmorMaterials.CHEF_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, pizzaProperties().durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
    public static final DeferredItem<Item> CHEF_BOOTS = ITEMS.register("chef_boots", () -> new ChefArmor(ModArmorMaterials.CHEF_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, pizzaProperties().durability(ArmorItem.Type.BOOTS.getDurability(5))));

    //Tools
    public static final DeferredItem<Item> ROLLING_PIN = ITEMS.register("rolling_pin", () -> new Item(pizzaProperties().stacksTo(1).durability(59)));
    public static final DeferredItem<Item> STONE_PIZZA_PEEL = ITEMS.register("stone_pizza_peel", () -> new PizzaPeelItem(Tiers.STONE, pizzaProperties().stacksTo(1)));
    public static final DeferredItem<Item> GOLDEN_PIZZA_PEEL = ITEMS.register("golden_pizza_peel", () -> new PizzaPeelItem(Tiers.GOLD, pizzaProperties().stacksTo(1)));
    public static final DeferredItem<Item> IRON_PIZZA_PEEL = ITEMS.register("iron_pizza_peel", () -> new PizzaPeelItem(Tiers.IRON, pizzaProperties().stacksTo(1)));
    public static final DeferredItem<Item> DIAMOND_PIZZA_PEEL = ITEMS.register("diamond_pizza_peel", () -> new PizzaPeelItem(Tiers.DIAMOND, pizzaProperties().stacksTo(1)));
    public static final DeferredItem<Item> NETHERITE_PIZZA_PEEL = ITEMS.register("netherite_pizza_peel", () -> new PizzaPeelItem(Tiers.NETHERITE, pizzaProperties().stacksTo(1)));

    //Pizza Slice
    public static final DeferredItem<Item> PIZZA_SLICE = ITEMS.register("pizza_slice", () -> new PizzaSliceItem(pizzaProperties()));

    public static final DeferredItem<Item> ONION_SLICE = ITEMS.register("onion_slice", () -> new Item(pizzaProperties().food(ModFoods.ONION_SLICE)));
    public static final DeferredItem<Item> TOMATO_SLICE = ITEMS.register("tomato_slice", () -> new Item(pizzaProperties().food(ModFoods.TOMATO_SLICE)));
    public static final DeferredItem<Item> MUSHROOM_SLICE = ITEMS.register("mushroom_slice", () -> new Item(pizzaProperties().food(ModFoods.MUSHROOM_SLICE)));

    //Cheese
    public static final DeferredItem<Item> CHEESE_BLOCK = registerBlockItem("cheese_block", ModBlocks.CHEESE_BLOCK, pizzaProperties());
    public static final DeferredItem<Item> CHEESE = ITEMS.register("cheese", () -> new Item(pizzaProperties().food(ModFoods.CHEESE)));

    public static DeferredItem<Item> registerBlockItem(final String name, DeferredBlock<Block> block, Item.Properties properties) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), properties));
    }

    public static Item.Properties pizzaProperties() {
        return new Item.Properties();
    }
}