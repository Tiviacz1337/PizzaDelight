package com.tiviacz.pizzadelight.blockentity;

import com.tiviacz.pizzadelight.config.PizzaDelightConfig;
import com.tiviacz.pizzadelight.container.PizzaStationMenu;
import com.tiviacz.pizzadelight.init.ModBlockEntityTypes;
import com.tiviacz.pizzadelight.init.ModItems;
import com.tiviacz.pizzadelight.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PizzaStationBlockEntity extends BaseBlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = createHandler();
    private final LazyOptional<ItemStackHandler> inventoryCapability = LazyOptional.of(() -> this.inventory);

    public PizzaStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.PIZZA_STATION.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.inventory.deserializeNBT(compound.getCompound(INVENTORY));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(INVENTORY, this.inventory.serializeNBT());
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new PizzaStationMenu(id, playerInventory, this);
    }

    public void openGUI(Player player, MenuProvider containerSupplier, BlockPos pos) {
        if(!player.level().isClientSide) {
            NetworkHooks.openScreen((ServerPlayer)player, containerSupplier, pos);
        }
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(12) {
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                //Prevent sandwiches
                if(stack.getItemHolder().is(new ResourceLocation("some_assembly_required", "sandwich")))
                    return false;

                //Output
                if(slot == 0) {
                    return false;
                }
                //Base
                if(slot == 1) {
                    return stack.is(ModItems.RAW_PIZZA.get());
                }
                //Sauce
                if(slot == 2) {
                    return (stack.is(ModTags.SAUCE)) || stack.getItem() instanceof PotionItem;
                }

                //Ingredients
                if(PizzaDelightConfig.SERVER.allowOnlyRecommendedIngredients.get()) {
                    return stack.is(ModTags.INGREDIENTS);
                }
                return stack.isEdible() || stack.is(ModTags.INGREDIENTS);
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER)
            return inventoryCapability.cast();
        return super.getCapability(cap, side);
    }
}