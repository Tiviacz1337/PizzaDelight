package com.tiviacz.pizzadelight.init;

import com.google.common.collect.Maps;
import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.tags.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PizzaLayers {
    public static final Map<TagKey<Item>, ResourceLocation> TAG_TO_LAYER = Maps.newHashMap();
    public static final Map<TagKey<Item>, ResourceLocation> TAG_TO_RAW_LAYER = Maps.newHashMap();
    public static final Map<TagKey<Item>, ResourceLocation> TAG_TO_ITEM_LAYER = Maps.newHashMap();

    //Layers
    //Base
    public static final ResourceLocation PIZZA_SLICE = createItem("pizza_slice");

    public static final ResourceLocation CHEESE_LAYER = createBlock("cheese");
    public static final ResourceLocation RAW_CHEESE_LAYER = createRawBlock("cheese");

    //Vegetables
    public static final ResourceLocation BROCCOLI_LAYER = createBlock("broccoli");
    public static final ResourceLocation CORN_LAYER = createBlock("corn");
    public static final ResourceLocation CUCUMBER_LAYER = createBlock("cucumber");
    public static final ResourceLocation ONION_LAYER = createBlock("onion");
    public static final ResourceLocation PEPPER_LAYER = createBlock("pepper");
    public static final ResourceLocation TOMATO_LAYER = createBlock("tomato");

    public static final ResourceLocation RAW_BROCCOLI_LAYER = createRawBlock("broccoli");
    public static final ResourceLocation RAW_CORN_LAYER = createRawBlock("corn");
    public static final ResourceLocation RAW_CUCUMBER_LAYER = createRawBlock("cucumber");
    public static final ResourceLocation RAW_ONION_LAYER = createRawBlock("onion");
    public static final ResourceLocation RAW_PEPPER_LAYER = createRawBlock("pepper");
    public static final ResourceLocation RAW_TOMATO_LAYER = createRawBlock("tomato");

    public static final ResourceLocation BROCCOLI_ITEM_LAYER = createItem("broccoli");
    public static final ResourceLocation CORN_ITEM_LAYER = createItem("corn");
    public static final ResourceLocation CUCUMBER_ITEM_LAYER = createItem("cucumber");
    public static final ResourceLocation ONION_ITEM_LAYER = createItem("onion");
    public static final ResourceLocation PEPPER_ITEM_LAYER = createItem("pepper");
    public static final ResourceLocation TOMATO_ITEM_LAYER = createItem("tomato");

    //Fruits
    public static final ResourceLocation PINEAPPLE_LAYER = createBlock("pineapple");
    public static final ResourceLocation OLIVE_LAYER = createBlock("olive");

    public static final ResourceLocation RAW_PINEAPPLE_LAYER = createRawBlock("pineapple");
    public static final ResourceLocation RAW_OLIVE_LAYER = createRawBlock("olive");

    public static final ResourceLocation PINEAPPLE_ITEM_LAYER = createItem("pineapple");
    public static final ResourceLocation OLIVE_ITEM_LAYER = createItem("olive");

    //Mushrooms
    public static final ResourceLocation MUSHROOM_LAYER = createBlock("mushroom");
    public static final ResourceLocation VEGETABLE_LAYER = createBlock("vegetable");

    public static final ResourceLocation RAW_MUSHROOM_LAYER = createRawBlock("mushroom");
    public static final ResourceLocation RAW_VEGETABLE_LAYER = createRawBlock("vegetable");

    public static final ResourceLocation MUSHROOM_ITEM_LAYER = createItem("mushroom");
    public static final ResourceLocation VEGETABLE_ITEM_LAYER = createItem("vegetable");

    //Meats
    public static final ResourceLocation HAM_LAYER = createBlock("ham");
    public static final ResourceLocation BEEF_LAYER = createBlock("beef");
    public static final ResourceLocation CHICKEN_LAYER = createBlock("chicken");
    public static final ResourceLocation FISH_LAYER = createBlock("fish");

    public static final ResourceLocation RAW_HAM_LAYER = createRawBlock("ham");
    public static final ResourceLocation RAW_BEEF_LAYER = createRawBlock("beef");
    public static final ResourceLocation RAW_CHICKEN_LAYER = createRawBlock("chicken");
    public static final ResourceLocation RAW_FISH_LAYER = createRawBlock("fish");

    public static final ResourceLocation HAM_ITEM_LAYER = createItem("ham");
    public static final ResourceLocation BEEF_ITEM_LAYER = createItem("beef");
    public static final ResourceLocation CHICKEN_ITEM_LAYER = createItem("chicken");
    public static final ResourceLocation FISH_ITEM_LAYER = createItem("fish");

    //Sauces
    public static final ResourceLocation TOMATO_SAUCE_LAYER = createBlock("tomato_sauce");
    public static final ResourceLocation HOT_SAUCE_LAYER = createBlock("hot_sauce");

    public static final ResourceLocation TOMATO_SAUCE_ITEM_LAYER = createItem("tomato_sauce");
    public static final ResourceLocation HOT_SAUCE_ITEM_LAYER = createItem("hot_sauce");

    public static ResourceLocation createItem(String name) {
        return new ResourceLocation(PizzaDelight.MODID, "item/layer/" + name);
    }

    public static ResourceLocation createBlock(String name) {
        return new ResourceLocation(PizzaDelight.MODID, "block/layer/" + name);
    }

    public static ResourceLocation createRawBlock(String name) {
        return new ResourceLocation(PizzaDelight.MODID, "block/layer/raw/" + name);
    }

    public static Map<TagKey<Item>, ResourceLocation> getTagToLayer() {
        return TAG_TO_LAYER;
    }

    public static Map<TagKey<Item>, ResourceLocation> getTagToRawLayer() {
        return TAG_TO_RAW_LAYER;
    }

    public static Map<TagKey<Item>, ResourceLocation> getTagToItemLayer() {
        return TAG_TO_ITEM_LAYER;
    }

    public static void setMaps() {
        setTagToLayerMap();
        setTagToRawLayerMap();
        setTagToItemLayer();
    }

    public static void setTagToLayerMap() {
        TAG_TO_LAYER.put(ModTags.CHEESE_LAYER, CHEESE_LAYER);

        TAG_TO_LAYER.put(ModTags.BROCCOLI_LAYER, BROCCOLI_LAYER);
        TAG_TO_LAYER.put(ModTags.CORN_LAYER, CORN_LAYER);
        TAG_TO_LAYER.put(ModTags.CUCUMBER_LAYER, CUCUMBER_LAYER);
        TAG_TO_LAYER.put(ModTags.ONION_LAYER, ONION_LAYER);
        TAG_TO_LAYER.put(ModTags.PEPPER_LAYER, PEPPER_LAYER);
        TAG_TO_LAYER.put(ModTags.TOMATO_LAYER, TOMATO_LAYER);

        TAG_TO_LAYER.put(ModTags.PINEAPPLE_LAYER, PINEAPPLE_LAYER);
        TAG_TO_LAYER.put(ModTags.OLIVE_LAYER, OLIVE_LAYER);

        TAG_TO_LAYER.put(ModTags.MUSHROOM_LAYER, MUSHROOM_LAYER);
        TAG_TO_LAYER.put(ModTags.VEGETABLE_LAYER, VEGETABLE_LAYER);

        TAG_TO_LAYER.put(ModTags.HAM_LAYER, HAM_LAYER);
        TAG_TO_LAYER.put(ModTags.CHICKEN_LAYER, CHICKEN_LAYER);
        TAG_TO_LAYER.put(ModTags.FISH_LAYER, FISH_LAYER);

        TAG_TO_LAYER.put(ModTags.TOMATO_SAUCE, TOMATO_SAUCE_LAYER);
        TAG_TO_LAYER.put(ModTags.HOT_SAUCE, HOT_SAUCE_LAYER);
    }

    public static void setTagToRawLayerMap() {
        TAG_TO_RAW_LAYER.put(ModTags.CHEESE_LAYER, RAW_CHEESE_LAYER);

        TAG_TO_RAW_LAYER.put(ModTags.BROCCOLI_LAYER, RAW_BROCCOLI_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.CORN_LAYER, RAW_CORN_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.CUCUMBER_LAYER, RAW_CUCUMBER_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.ONION_LAYER, RAW_ONION_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.PEPPER_LAYER, RAW_PEPPER_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.TOMATO_LAYER, RAW_TOMATO_LAYER);

        TAG_TO_RAW_LAYER.put(ModTags.PINEAPPLE_LAYER, RAW_PINEAPPLE_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.OLIVE_LAYER, RAW_OLIVE_LAYER);

        TAG_TO_RAW_LAYER.put(ModTags.MUSHROOM_LAYER, RAW_MUSHROOM_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.VEGETABLE_LAYER, RAW_VEGETABLE_LAYER);

        TAG_TO_RAW_LAYER.put(ModTags.HAM_LAYER, RAW_HAM_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.CHICKEN_LAYER, RAW_CHICKEN_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.FISH_LAYER, RAW_FISH_LAYER);

        TAG_TO_RAW_LAYER.put(ModTags.TOMATO_SAUCE, TOMATO_SAUCE_LAYER);
        TAG_TO_RAW_LAYER.put(ModTags.HOT_SAUCE, HOT_SAUCE_LAYER);
    }

    public static void setTagToItemLayer() {
        TAG_TO_ITEM_LAYER.put(ModTags.BROCCOLI_LAYER, BROCCOLI_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.CORN_LAYER, CORN_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.CUCUMBER_LAYER, CUCUMBER_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.ONION_LAYER, ONION_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.PEPPER_LAYER, PEPPER_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.TOMATO_LAYER, TOMATO_ITEM_LAYER);

        TAG_TO_ITEM_LAYER.put(ModTags.PINEAPPLE_LAYER, PINEAPPLE_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.OLIVE_LAYER, OLIVE_ITEM_LAYER);

        TAG_TO_ITEM_LAYER.put(ModTags.MUSHROOM_LAYER, MUSHROOM_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.VEGETABLE_LAYER, VEGETABLE_ITEM_LAYER);

        TAG_TO_ITEM_LAYER.put(ModTags.HAM_LAYER, HAM_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.CHICKEN_LAYER, CHICKEN_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.FISH_LAYER, FISH_ITEM_LAYER);

        TAG_TO_ITEM_LAYER.put(ModTags.TOMATO_SAUCE, TOMATO_SAUCE_ITEM_LAYER);
        TAG_TO_ITEM_LAYER.put(ModTags.HOT_SAUCE, HOT_SAUCE_ITEM_LAYER);
    }

    public static ResourceLocation createResourceLocation(String tagName) {
        return new ResourceLocation(PizzaDelight.MODID, tagName);
    }

    public static final List<TagKey<Item>> VALID_TAGS = Arrays.asList
            (
                    ModTags.CHEESE_LAYER,

                    ModTags.BROCCOLI_LAYER,
                    ModTags.CORN_LAYER,
                    ModTags.CUCUMBER_LAYER,
                    ModTags.ONION_LAYER,
                    ModTags.PEPPER_LAYER,
                    ModTags.TOMATO_LAYER,

                    ModTags.PINEAPPLE_LAYER,
                    ModTags.OLIVE_LAYER,

                    ModTags.MUSHROOM_LAYER,
                    ModTags.VEGETABLE_LAYER,

                    ModTags.HAM_LAYER,
                    ModTags.CHICKEN_LAYER,
                    ModTags.FISH_LAYER,

                    ModTags.TOMATO_SAUCE,
                    ModTags.HOT_SAUCE
            );

    public static final List<TagKey<Item>> VALID_ITEM_TAGS = Arrays.asList
            (
                    ModTags.BROCCOLI_LAYER,
                    ModTags.CORN_LAYER,
                    ModTags.CUCUMBER_LAYER,
                    ModTags.ONION_LAYER,
                    ModTags.PEPPER_LAYER,
                    ModTags.TOMATO_LAYER,

                    ModTags.PINEAPPLE_LAYER,
                    ModTags.OLIVE_LAYER,

                    ModTags.MUSHROOM_LAYER,
                    ModTags.VEGETABLE_LAYER,

                    ModTags.HAM_LAYER,
                    ModTags.CHICKEN_LAYER,
                    ModTags.FISH_LAYER,

                    ModTags.TOMATO_SAUCE,
                    ModTags.HOT_SAUCE
            );
}
