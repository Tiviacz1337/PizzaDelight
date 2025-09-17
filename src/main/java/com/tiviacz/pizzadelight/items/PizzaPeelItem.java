package com.tiviacz.pizzadelight.items;

import com.tiviacz.pizzadelight.PizzaDelight;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class PizzaPeelItem extends ShovelItem {
    public PizzaPeelItem(Tier tier, Item.Properties properties) {
        super(tier, properties.component(DataComponents.ATTRIBUTE_MODIFIERS, new ItemAttributeModifiers(List.of(new ItemAttributeModifiers.Entry(Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "peel_knockback"), 0.25D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)), true)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }
}