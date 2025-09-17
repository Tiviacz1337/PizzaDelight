package com.tiviacz.pizzadelight.container.slots;

import com.tiviacz.pizzadelight.container.PizzaStationMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class PizzaStationResultSlot extends SlotItemHandler {
    private final PizzaStationMenu menu;

    public PizzaStationResultSlot(PizzaStationMenu menu, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);

        this.menu = menu;
    }

    @Override
    public void setChanged() {
        menu.updateOutput();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
        for(int i = 0; i < menu.blockEntity.getInventory().getSlots() - 1; i++) {
            ItemStack itemStack = menu.blockEntity.getInventory().getStackInSlot(i).copy();

            if(!itemStack.isEmpty()) {
                boolean isPotion = itemStack.getItem() instanceof PotionItem;
                boolean isSauce = itemStack.has(DataComponents.FOOD) && !itemStack.get(DataComponents.FOOD).usingConvertsTo().isEmpty();

                ItemStack container = itemStack.getCraftingRemainingItem();
                itemStack.shrink(1);
                menu.blockEntity.getInventory().setStackInSlot(i, itemStack);

                if(container.isEmpty()) {
                    if(isPotion) {
                        container = new ItemStack(Items.GLASS_BOTTLE);
                    } else if(isSauce) {
                        container = itemStack.get(DataComponents.FOOD).usingConvertsTo().get();
                    }
                }

                if(!container.isEmpty()) {
                    if(itemStack.isEmpty()) {
                        menu.blockEntity.getInventory().setStackInSlot(i, container);
                    } else {
                        ItemHandlerHelper.giveItemToPlayer(player, container);
                    }
                }
            }
        }
        menu.updateOutput();
    }
}