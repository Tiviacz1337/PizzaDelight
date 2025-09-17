package com.tiviacz.pizzadelight.items;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.client.renderer.ChefHatModel;
import com.tiviacz.pizzadelight.init.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ChefArmor extends ArmorItem {
    public ChefArmor(ArmorMaterial materialIn, ArmorItem.Type pType, Properties builderIn) {
        super(materialIn, pType, builderIn);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.CHEF_HAT.get() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == ModItems.CHEF_SHIRT.get()
                && player.getItemBySlot(EquipmentSlot.LEGS).getItem() == ModItems.CHEF_LEGGINGS.get() && player.getItemBySlot(EquipmentSlot.FEET).getItem() == ModItems.CHEF_BOOTS.get()) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1, 0, false, false));
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> humanoid) {
                if(armorSlot == EquipmentSlot.HEAD) {
                    ChefHatModel hat = new ChefHatModel(ChefHatModel.createModelData().bakeRoot());
                    humanoid.copyPropertiesTo(hat);

                    return hat;
                }
                return humanoid;
            }
        });
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if(slot == EquipmentSlot.HEAD) {
            return new ResourceLocation(PizzaDelight.MODID, "textures/models/armor/chef_hat.png").toString();
        }
        return null;
    }
}
