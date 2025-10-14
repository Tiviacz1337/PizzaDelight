package com.tiviacz.pizzadelight.handlers;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.components.PizzaIngredients;
import com.tiviacz.pizzadelight.init.ModBlockEntityTypes;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.init.ModDataComponents;
import com.tiviacz.pizzadelight.init.ModNetwork;
import com.tiviacz.pizzadelight.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = PizzaDelight.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        ModNetwork.register(event.registrar(PizzaDelight.MODID));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerBlockItemColors(RegisterColorHandlersEvent.Item event) {
        BlockColors blockColors = event.getBlockColors();
        ItemColors itemColors = event.getItemColors();

        blockColors.register((state, world, pos, tintIndex) ->
        {
            ItemStackHandler handler = new ItemStackHandler(12);
            boolean isRaw = true;

            if(world != null) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if(blockEntity instanceof PizzaBlockEntity be) {
                    handler = be.inventory;
                    isRaw = be.isRaw();
                }
            }
            int color = RenderUtils.getDominantColor(Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(handler.getStackInSlot(tintIndex)).getParticleIcon(), isRaw);
            if(handler.getStackInSlot(tintIndex).has(DataComponents.POTION_CONTENTS)) {
                PotionContents contents = handler.getStackInSlot(tintIndex).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                color = contents.getColor();
            }
            if(handler.getStackInSlot(tintIndex).isEmpty()) return 14858625;
            return color;
        }, ModBlocks.RAW_PIZZA.get(), ModBlocks.PIZZA.get());

        itemColors.register((stack, tintIndex) ->
        {
            NonNullList<ItemStack> ingredients = stack.getOrDefault(ModDataComponents.PIZZA_INGREDIENTS, PizzaIngredients.EMPTY).getIngredients();
            int color = RenderUtils.getDominantColor(Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(ingredients.get(tintIndex)).getParticleIcon(), stack.getItem() == ModBlocks.RAW_PIZZA.get().asItem());
            if(ingredients.get(tintIndex).has(DataComponents.POTION_CONTENTS)) {
                PotionContents contents = ingredients.get(tintIndex).getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                color = contents.getColor();
            }
            if(ingredients.get(tintIndex).isEmpty()) return 14858625;
            return color;
        }, ModBlocks.RAW_PIZZA.get(), ModBlocks.PIZZA.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.PIZZA.get(), (blockEntity, side) -> {
            if(blockEntity instanceof PizzaBlockEntity pizzaBlockEntity) {
                return pizzaBlockEntity.inventory;
            }
            return new ItemStackHandler(0);
        });
    }
}