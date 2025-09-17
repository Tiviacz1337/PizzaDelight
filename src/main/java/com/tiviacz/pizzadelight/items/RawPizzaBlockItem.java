package com.tiviacz.pizzadelight.items;

import com.tiviacz.pizzadelight.client.tooltip.PizzaTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class RawPizzaBlockItem extends BlockItem {
    public RawPizzaBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
        return Optional.of(new PizzaTooltipComponent(pStack));
    }
}