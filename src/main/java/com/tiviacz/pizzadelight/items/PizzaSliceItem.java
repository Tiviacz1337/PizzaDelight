package com.tiviacz.pizzadelight.items;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.pizzadelight.util.NBTUtils;
import com.tiviacz.pizzadelight.util.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PizzaSliceItem extends Item {
    public PizzaSliceItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        FoodProperties foodProperties = this.getFoodProperties(stack, null);
        if(foodProperties != null && !foodProperties.getEffects().isEmpty()) {
            TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
        }
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
        return builder.alwaysEat().build();
    }

    @Override
    public boolean isEdible() {
        return true;
    }
}