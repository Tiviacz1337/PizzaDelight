package com.tiviacz.pizzadelight.client;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.tags.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class LayerSelector {
    //Block
    public static final ResourceLocation RAW_BROCCOLI_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/broccoli");
    public static final ResourceLocation RAW_CORN_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/corn");
    public static final ResourceLocation RAW_CUCUMBER_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/cucumber");
    public static final ResourceLocation RAW_ONION_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/onion");
    public static final ResourceLocation RAW_PEPPER_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/pepper");
    public static final ResourceLocation RAW_TOMATO_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/tomato");

    public static final ResourceLocation RAW_PINEAPPLE_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/pineapple");
    public static final ResourceLocation RAW_OLIVE_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/olive");

    public static final ResourceLocation RAW_MUSHROOM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/mushroom");

    public static final ResourceLocation RAW_HAM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/ham");
    public static final ResourceLocation RAW_BEEF_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/beef");
    public static final ResourceLocation RAW_CHICKEN_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "block/layer/universal/raw/chicken");

    //Item
    public static final ResourceLocation BROCCOLI_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/broccoli");
    public static final ResourceLocation CORN_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/corn");
    public static final ResourceLocation CUCUMBER_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/cucumber");
    public static final ResourceLocation ONION_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/onion");
    public static final ResourceLocation PEPPER_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/pepper");
    public static final ResourceLocation TOMATO_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/tomato");

    public static final ResourceLocation PINEAPPLE_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/pineapple");
    public static final ResourceLocation OLIVE_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/olive");

    public static final ResourceLocation MUSHROOM_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/mushroom");

    public static final ResourceLocation HAM_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/ham");
    public static final ResourceLocation BEEF_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/beef");
    public static final ResourceLocation CHICKEN_ITEM_UNIVERSAL_LAYER = new ResourceLocation(PizzaDelight.MODID, "item/layer/universal/chicken");

    public List<ResourceLocation> layers = new ArrayList<>();
    public List<ResourceLocation> itemLayers = new ArrayList<>();

    public LayerSelector(boolean isItem) {
        if(isItem) {
            prepareItemLayers();
        } else {
            prepareLayers();
        }
    }

    public ResourceLocation selectLayer() {
        ResourceLocation resourceLocation = layers.get(0);
        layers.remove(0);
        return resourceLocation;
    }

    public ResourceLocation selectItemLayer() {
        ResourceLocation resourceLocation = itemLayers.get(0);
        itemLayers.remove(0);
        return resourceLocation;
    }

    public void prepareLayers() {
        layers.add(RAW_BROCCOLI_UNIVERSAL_LAYER);
        layers.add(RAW_CORN_UNIVERSAL_LAYER);
        layers.add(RAW_CUCUMBER_UNIVERSAL_LAYER);
        layers.add(RAW_ONION_UNIVERSAL_LAYER);
        layers.add(RAW_PEPPER_UNIVERSAL_LAYER);
        layers.add(RAW_TOMATO_UNIVERSAL_LAYER);

        layers.add(RAW_PINEAPPLE_UNIVERSAL_LAYER);
        layers.add(RAW_OLIVE_UNIVERSAL_LAYER);

        layers.add(RAW_MUSHROOM_UNIVERSAL_LAYER);

        layers.add(RAW_HAM_UNIVERSAL_LAYER);
        layers.add(RAW_BEEF_UNIVERSAL_LAYER);
        layers.add(RAW_CHICKEN_UNIVERSAL_LAYER);
    }

    public void prepareItemLayers() {
        itemLayers.add(BROCCOLI_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(CORN_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(CUCUMBER_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(ONION_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(PEPPER_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(TOMATO_ITEM_UNIVERSAL_LAYER);

        itemLayers.add(PINEAPPLE_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(OLIVE_ITEM_UNIVERSAL_LAYER);

        itemLayers.add(MUSHROOM_ITEM_UNIVERSAL_LAYER);

        itemLayers.add(HAM_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(BEEF_ITEM_UNIVERSAL_LAYER);
        itemLayers.add(CHICKEN_ITEM_UNIVERSAL_LAYER);
    }

    public void processLayer(TagKey<Item> tagKey) {
        if(tagKey == ModTags.BROCCOLI_LAYER) remove(RAW_BROCCOLI_UNIVERSAL_LAYER);

        if(tagKey == ModTags.CORN_LAYER) remove(RAW_CORN_UNIVERSAL_LAYER);

        if(tagKey == ModTags.CUCUMBER_LAYER) remove(RAW_CUCUMBER_UNIVERSAL_LAYER);

        if(tagKey == ModTags.ONION_LAYER) remove(RAW_ONION_UNIVERSAL_LAYER);

        if(tagKey == ModTags.PEPPER_LAYER) remove(RAW_PEPPER_UNIVERSAL_LAYER);

        if(tagKey == ModTags.TOMATO_LAYER) remove(RAW_TOMATO_UNIVERSAL_LAYER);

        if(tagKey == ModTags.PINEAPPLE_LAYER) remove(RAW_PINEAPPLE_UNIVERSAL_LAYER);

        if(tagKey == ModTags.OLIVE_LAYER) remove(RAW_OLIVE_UNIVERSAL_LAYER);

        if(tagKey == ModTags.MUSHROOM_LAYER) remove(RAW_MUSHROOM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.HAM_LAYER) remove(RAW_HAM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.CHICKEN_LAYER) remove(RAW_CHICKEN_UNIVERSAL_LAYER);
    }

    public void processItemLayer(TagKey<Item> tagKey) {
        if(tagKey == ModTags.BROCCOLI_LAYER) remove(BROCCOLI_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.CORN_LAYER) remove(CORN_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.CUCUMBER_LAYER) remove(CUCUMBER_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.ONION_LAYER) remove(ONION_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.PEPPER_LAYER) remove(PEPPER_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.TOMATO_LAYER) remove(TOMATO_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.PINEAPPLE_LAYER) remove(PINEAPPLE_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.OLIVE_LAYER) remove(OLIVE_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.MUSHROOM_LAYER) remove(MUSHROOM_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.HAM_LAYER) remove(HAM_ITEM_UNIVERSAL_LAYER);

        if(tagKey == ModTags.CHICKEN_LAYER) remove(CHICKEN_ITEM_UNIVERSAL_LAYER);
    }

    public void remove(ResourceLocation location) {
        layers.remove(location);
    }

    public void removeItem(ResourceLocation location) {
        itemLayers.remove(location);
    }
}