package com.tiviacz.pizzadelight.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TextUtils {
    private static final MutableComponent NO_EFFECTS;

    public static void addFoodEffectTooltip(ItemStack itemIn, List<Component> lores, float durationFactor) {
        FoodProperties foodStats = itemIn.getItem().getFoodProperties(itemIn, null);
        if(foodStats != null) {
            List<Pair<MobEffectInstance, Float>> effectList = foodStats.getEffects();
            List<Pair<Attribute, AttributeModifier>> attributeList = Lists.newArrayList();
            Iterator var6;
            Pair pair;
            MutableComponent iformattabletextcomponent;
            MobEffect effect;
            if(effectList.isEmpty()) {
                lores.add(NO_EFFECTS);
            } else {
                for(var6 = effectList.iterator(); var6.hasNext(); lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()))) {
                    pair = (Pair)var6.next();
                    MobEffectInstance instance = (MobEffectInstance)pair.getFirst();
                    iformattabletextcomponent = Component.translatable(instance.getDescriptionId());
                    effect = instance.getEffect();
                    Map<Attribute, AttributeModifier> attributeMap = effect.getAttributeModifiers();
                    if(!attributeMap.isEmpty()) {
                        Iterator var12 = attributeMap.entrySet().iterator();

                        while(var12.hasNext()) {
                            Map.Entry<Attribute, AttributeModifier> entry = (Map.Entry)var12.next();
                            AttributeModifier rawModifier = entry.getValue();
                            AttributeModifier modifier = new AttributeModifier(rawModifier.getName(), effect.getAttributeModifierValue(instance.getAmplifier(), rawModifier), rawModifier.getOperation());
                            attributeList.add(new Pair(entry.getKey(), modifier));
                        }
                    }

                    if(instance.getAmplifier() > 0) {
                        iformattabletextcomponent = Component.translatable("potion.withAmplifier", iformattabletextcomponent, Component.translatable("potion.potency." + instance.getAmplifier()));
                    }

                    if(instance.getDuration() > 20) {
                        iformattabletextcomponent = Component.translatable("potion.withDuration", iformattabletextcomponent, MobEffectUtil.formatDuration(instance, durationFactor));
                    }
                }
            }

            if(!attributeList.isEmpty()) {
                lores.add(CommonComponents.EMPTY);
                lores.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
                var6 = attributeList.iterator();

                while(var6.hasNext()) {
                    pair = (Pair)var6.next();
                    AttributeModifier modifier = (AttributeModifier)pair.getSecond();
                    double amount = modifier.getAmount();
                    double formattedAmount;
                    if(modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                        formattedAmount = modifier.getAmount();
                    } else {
                        formattedAmount = modifier.getAmount() * 100.0;
                    }

                    if(amount > 0.0) {
                        lores.add(Component.translatable("attribute.modifier.plus." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute)pair.getFirst()).getDescriptionId())).withStyle(ChatFormatting.BLUE));
                    } else if(amount < 0.0) {
                        formattedAmount *= -1.0;
                        lores.add(Component.translatable("attribute.modifier.take." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute)pair.getFirst()).getDescriptionId())).withStyle(ChatFormatting.RED));
                    }
                }
            }

        }
    }

    static {
        NO_EFFECTS = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);
    }
}

