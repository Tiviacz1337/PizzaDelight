package com.tiviacz.pizzadelight.items;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.init.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ChefArmor extends ArmorItem {
    public ChefArmor(Holder<ArmorMaterial> materialIn, ArmorItem.Type pType, Properties builderIn) {
        super(materialIn, pType, builderIn);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pEntity instanceof Player player) {
            if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.CHEF_HAT.get() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.CHEF_SHIRT.get()
                    && player.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.CHEF_LEGGINGS.get() && player.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.CHEF_BOOTS.get()) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1, 0, false, false));
            }
        }
    }

    @Nullable
    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        if(slot == EquipmentSlot.HEAD) {
            return ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "textures/models/armor/chef_hat.png");
        }
        return null;
    }
}
