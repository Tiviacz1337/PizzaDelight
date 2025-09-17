package com.tiviacz.pizzadelight.handlers;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.util.NBTUtils;
import com.tiviacz.pizzadelight.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = PizzaDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
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
            if(handler.getStackInSlot(tintIndex).getItem() instanceof PotionItem)
                color = PotionUtils.getColor(handler.getStackInSlot(tintIndex));
            if(handler.getStackInSlot(tintIndex).isEmpty()) return 14858625;
            return color;
        }, ModBlocks.RAW_PIZZA.get(), ModBlocks.PIZZA.get());

        itemColors.register((stack, tintIndex) ->
        {
            ItemStackHandler handler = NBTUtils.createHandlerFromStack(stack, 12);
            int color = RenderUtils.getDominantColor(Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(handler.getStackInSlot(tintIndex)).getParticleIcon(), stack.getItem() == ModBlocks.RAW_PIZZA.get().asItem());
            if(handler.getStackInSlot(tintIndex).getItem() instanceof PotionItem)
                color = PotionUtils.getColor(handler.getStackInSlot(tintIndex));
            if(handler.getStackInSlot(tintIndex).isEmpty()) return 14858625;
            return color;
        }, ModBlocks.RAW_PIZZA.get(), ModBlocks.PIZZA.get());
    }
}