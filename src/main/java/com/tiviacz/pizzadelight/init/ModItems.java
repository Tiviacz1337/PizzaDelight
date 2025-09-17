package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.items.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PizzaDelight.MODID);

    public static final RegistryObject<Item> PIZZA = ITEMS.register("pizza", () -> new PizzaBlockItem(ModBlocks.PIZZA.get(), pizzaProperties().stacksTo(1)));
    public static final RegistryObject<Item> RAW_PIZZA = ITEMS.register("raw_pizza", () -> new RawPizzaBlockItem(ModBlocks.RAW_PIZZA.get(), pizzaProperties().stacksTo(1)));
    public static final RegistryObject<Item> PIZZA_STATION = registerBlockItem("pizza_station", ModBlocks.PIZZA_STATION, pizzaProperties());
    public static final RegistryObject<Item> BASIN = registerBlockItem("basin", ModBlocks.BASIN, pizzaProperties());

    //Outfits
    public static final RegistryObject<Item> CHEF_HAT = ITEMS.register("chef_hat", () -> new ChefArmor(ArmorMaterials.CHEF, ArmorItem.Type.HELMET, pizzaProperties()));
    public static final RegistryObject<Item> CHEF_SHIRT = ITEMS.register("chef_shirt", () -> new ChefArmor(ArmorMaterials.CHEF, ArmorItem.Type.CHESTPLATE, pizzaProperties()));
    public static final RegistryObject<Item> CHEF_LEGGINGS = ITEMS.register("chef_leggings", () -> new ChefArmor(ArmorMaterials.CHEF, ArmorItem.Type.LEGGINGS, pizzaProperties()));
    public static final RegistryObject<Item> CHEF_BOOTS = ITEMS.register("chef_boots", () -> new ChefArmor(ArmorMaterials.CHEF, ArmorItem.Type.BOOTS, pizzaProperties()));

    //Tools
    public static final RegistryObject<Item> ROLLING_PIN = ITEMS.register("rolling_pin", () -> new Item(pizzaProperties().stacksTo(1).durability(59)));
    public static final RegistryObject<Item> STONE_PIZZA_PEEL = ITEMS.register("stone_pizza_peel", () -> new PizzaPeelItem(Tiers.STONE, 1.5F, -3.0F, pizzaProperties().stacksTo(1)));
    public static final RegistryObject<Item> GOLDEN_PIZZA_PEEL = ITEMS.register("golden_pizza_peel", () -> new PizzaPeelItem(Tiers.GOLD, 1.5F, -3.0F, pizzaProperties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_PIZZA_PEEL = ITEMS.register("iron_pizza_peel", () -> new PizzaPeelItem(Tiers.IRON, 1.5F, -3.0F, pizzaProperties().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_PIZZA_PEEL = ITEMS.register("diamond_pizza_peel", () -> new PizzaPeelItem(Tiers.DIAMOND, 1.5F, -3.0F, pizzaProperties().stacksTo(1)));
    public static final RegistryObject<Item> NETHERITE_PIZZA_PEEL = ITEMS.register("netherite_pizza_peel", () -> new PizzaPeelItem(Tiers.NETHERITE, 1.5F, -3.0F, pizzaProperties().stacksTo(1)));

    //Pizza Slice
    public static final RegistryObject<Item> PIZZA_SLICE = ITEMS.register("pizza_slice", () -> new PizzaSliceItem(pizzaProperties()));

    public static final RegistryObject<Item> ONION_SLICE = ITEMS.register("onion_slice", () -> new Item(pizzaProperties().food(ModFoods.ONION_SLICE)));
    public static final RegistryObject<Item> TOMATO_SLICE = ITEMS.register("tomato_slice", () -> new Item(pizzaProperties().food(ModFoods.TOMATO_SLICE)));
    public static final RegistryObject<Item> MUSHROOM_SLICE = ITEMS.register("mushroom_slice", () -> new Item(pizzaProperties().food(ModFoods.MUSHROOM_SLICE)));

    //Cheese
    public static final RegistryObject<Item> CHEESE_BLOCK = registerBlockItem("cheese_block", ModBlocks.CHEESE_BLOCK, pizzaProperties());
    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese", () -> new Item(pizzaProperties().food(ModFoods.CHEESE)));

    public static RegistryObject<Item> registerBlockItem(final String name, RegistryObject<Block> block, Item.Properties properties) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), properties));
    }

    public static Item.Properties pizzaProperties() {
        return new Item.Properties();
    }
}