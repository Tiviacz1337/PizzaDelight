package com.tiviacz.pizzadelight.items;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.client.renderer.PizzaWithoutLevelRenderer;
import com.tiviacz.pizzadelight.client.tooltip.PizzaTooltipComponent;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.util.NBTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RawPizzaBlockItem extends BlockItem {
    public RawPizzaBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);

        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new PizzaWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new PizzaBlockEntity(BlockPos.ZERO, ModBlocks.RAW_PIZZA.get().defaultBlockState()));
            }
        });
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        return Optional.of(new PizzaTooltipComponent(pStack));
    }

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity livingEntity) {
        FoodProperties.Builder builder = new FoodProperties.Builder();
        builder.nutrition(NBTUtils.getHunger(stack));
        builder.saturationMod(NBTUtils.getSaturation(stack));
        for(Pair<MobEffectInstance, Float> effect : NBTUtils.getEffects(stack)) {
            builder.effect(effect::getFirst, effect.getSecond());
        }
        List<ItemStack> foods = new ArrayList<>(NBTUtils.getIngredients(stack));

        for(ItemStack food : foods) {
            FoodProperties props = food.getFoodProperties(livingEntity);

            if(props != null) {
                if(props.isMeat()) {
                    builder.meat();
                }

                for(Pair<MobEffectInstance, Float> effect : props.getEffects()) {
                    builder.effect(effect::getFirst, effect.getSecond());
                }
            }

            if(food.getItem() instanceof PotionItem) {
                for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(food)) {
                    builder.effect(() -> new MobEffectInstance(mobeffectinstance), 1.0F);
                }
            }
        }
        return builder.build();
    }

    @Override
    public boolean isEdible() {
        return false;
    }
}