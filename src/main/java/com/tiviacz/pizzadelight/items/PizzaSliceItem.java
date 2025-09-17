package com.tiviacz.pizzadelight.items;

import com.tiviacz.pizzadelight.client.tooltip.PizzaTooltipComponent;
import com.tiviacz.pizzadelight.common.PizzaBlockCalculator;
import com.tiviacz.pizzadelight.init.ModDataComponents;
import com.tiviacz.pizzadelight.util.PizzaFoodBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.Optional;

public class PizzaSliceItem extends Item {
    public PizzaSliceItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if(pStack.has(DataComponents.FOOD) && !getFoodProperties(pStack, null).effects().isEmpty()) {
            TextUtils.addFoodEffectTooltip(pStack, pTooltipComponents::add, 1.0F, pContext.tickRate());
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        return Optional.of(new PizzaTooltipComponent(pStack));
    }

    // Add effect on the fly, can't stack items otherwise because PossibleEffect is always different object
    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        FoodProperties foodProperties = stack.get(DataComponents.FOOD);
        if(foodProperties == null) {
            return null;
        }
        if(!foodProperties.canAlwaysEat()) {
            return foodProperties;
        } else { //Add effects
            PizzaFoodBuilder newProps = new PizzaFoodBuilder().nutrition(foodProperties.nutrition()).saturationModifier(foodProperties.saturation()).alwaysEdible();

            if(stack.has(ModDataComponents.PIZZA_INGREDIENTS)) {
                PizzaBlockCalculator calculator = new PizzaBlockCalculator(new ItemStackHandler(stack.get(ModDataComponents.PIZZA_INGREDIENTS).getIngredients()));
                List<FoodProperties.PossibleEffect> effects = calculator.findEffects();

                for(FoodProperties.PossibleEffect effect : effects) {
                    newProps.effect(effect.effectSupplier(), effect.probability());
                }
            }

            return newProps.build();
        }
    }
}