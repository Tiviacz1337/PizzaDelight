package com.tiviacz.pizzadelight.blockentity;

import com.tiviacz.pizzadelight.blocks.PizzaBlock;
import com.tiviacz.pizzadelight.blocks.RawPizzaBlock;
import com.tiviacz.pizzadelight.client.PizzaBakedModel;
import com.tiviacz.pizzadelight.common.PizzaBlockCalculator;
import com.tiviacz.pizzadelight.container.PizzaMenu;
import com.tiviacz.pizzadelight.init.ModBlockEntityTypes;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.init.ModItems;
import com.tiviacz.pizzadelight.tags.ModTags;
import com.tiviacz.pizzadelight.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class PizzaBlockEntity extends BaseBlockEntity implements MenuProvider {
    public ItemStackHandler inventory = createHandler(NonNullList.withSize(10, ItemStack.EMPTY));
    private Component customName = null;
    private int bakingTime = 0;
    private final int BASE_BAKING_TIME = 600;
    private final int selectedSlot = 0;

    private final LazyOptional<ItemStackHandler> inventoryCapability = LazyOptional.of(() -> this.inventory);

    private final String BAKING_TIME = "BakingTime";
    private final String CUSTOM_NAME = "CustomName";

    public PizzaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.PIZZA.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.inventory.deserializeNBT(compound.getCompound(INVENTORY));
        this.bakingTime = compound.getInt(BAKING_TIME);

        if(compound.contains(CUSTOM_NAME, 8)) {
            this.customName = Component.Serializer.fromJson(compound.getString(CUSTOM_NAME));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put(INVENTORY, this.inventory.serializeNBT());
        compound.putInt(BAKING_TIME, this.bakingTime);

        if(this.customName != null) {
            compound.putString(CUSTOM_NAME, Component.Serializer.toJson(this.customName));
        }
    }

    public ItemStack cloneToItemStack(ItemStack stack) {
        if(this.inventory != null) {
            NBTUtils.saveInventoryToStack(stack, this.inventory);
        }

        if(this.customName != null) {
            stack.setHoverName(this.customName);
        }

        return stack;
    }

    public ItemStack getSlice(ItemStack stack) {
        if(this.inventory != null) {
            NBTUtils.saveInventoryToStack(stack, this.inventory);
        }

        PizzaBlockCalculator calculator = new PizzaBlockCalculator(this.inventory);
        calculator.process();

        int allNutrition = (calculator.getHunger() + 3) / 4 * 4;

        NBTUtils.setHunger(stack, allNutrition / 4);
        NBTUtils.setSaturation(stack, calculator.getSaturation());
        NBTUtils.setEffects(stack, calculator.getEffects());

        //FoodProperties.Builder foodProperties = new FoodProperties.Builder().nutrition(allNutrition / 4).saturationMod(calculator.getSaturation());

        //Mark for effects - add always edible if contains effects
        //if(!calculator.getEffects().isEmpty()) {
        //foodProperties.alwaysEat();
        //}

        //stack.set(DataComponents.FOOD, foodProperties.build());
        return stack;
    }

    // ======== BAKING ========

    public boolean isBaking() {
        return this.bakingTime > 0;
    }

    public int getBakingTime() {
        return this.bakingTime;
    }

    public int getDefaultBakingTime() {
        return BASE_BAKING_TIME;
    }

    public boolean isRaw() {
        if(level == null) {
            return true;
        }
        return this.level.getBlockState(getBlockPos()).getBlock() instanceof RawPizzaBlock;
    }

    /**
     * Each stack increases baking time adding 20 * stack#getCount() to baseBakingTime
     */
    public int getCalculatedBakingTime() {
        int bakingTime = this.getDefaultBakingTime();

        for(int i = 0; i < inventory.getSlots(); i++) {
            if(!inventory.getStackInSlot(i).isEmpty()) {
                bakingTime += 20 * inventory.getStackInSlot(i).getCount();
            }
        }
        return bakingTime;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PizzaBlockEntity blockEntity) {
        boolean save = false;

        if(blockEntity.isRaw()) {
            if(blockEntity.bakingTime == 0 && level.getBlockState(pos.below()).is(vectorwing.farmersdelight.common.tag.ModTags.HEAT_SOURCES)) {
                blockEntity.bakingTime = 1;
                save = true;
            }
        }

        if(blockEntity.isBaking()) {
            blockEntity.bakingTime++;
            save = true;

            if(blockEntity.getBakingTime() >= blockEntity.getCalculatedBakingTime()) {
                if(!level.isClientSide) {
                    level.setBlockAndUpdate(blockEntity.getBlockPos(), ModBlocks.PIZZA.get().defaultBlockState());
                    blockEntity.bakingTime = 0;
                }
            }

            if(!level.getBlockState(pos.below()).is(vectorwing.farmersdelight.common.tag.ModTags.HEAT_SOURCES)) {
                blockEntity.bakingTime = 0;
            }
        }

        if(save) {
            blockEntity.setChanged();
        }
    }

    // ======== ITEMHANDLER ========

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public boolean canAddIngredient(ItemStack stack, int slot) {
        if(stack.getItemHolder().is(ResourceLocation.fromNamespaceAndPath("some_assembly_required", "sandwich")))
            return false;

        if(stack.getItem().getFoodProperties() != null || stack.is(ModTags.INGREDIENTS)) {
            return inventory.getStackInSlot(slot).isEmpty();
        }
        return false;
    }

    public void removeFromSlot(int slot) {
        inventory.setStackInSlot(slot, ItemStack.EMPTY);
    }

    private ItemStackHandler createHandler(NonNullList<ItemStack> contents) {
        return new ItemStackHandler(contents) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if(isRaw() && !isBaking()) {
                    return canAddIngredient(stack, slot);
                }
                return false;
            }

            @Override
            protected void onContentsChanged(int slot) {
                if(getBlockState().getBlock() instanceof RawPizzaBlock) {
                    setChanged();
                    requestModelDataUpdate();
                }
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

    // ======== CONTAINER ========

    @Override
    public Component getDisplayName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    public Component getDefaultName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new PizzaMenu(id, playerInventory, this);
    }

    public void openGUI(Player player, MenuProvider menuSupplier, BlockPos pos) {
        if(!player.level().isClientSide) {
            NetworkHooks.openScreen((ServerPlayer)player, menuSupplier, pos);
        }
    }

    // ======== MODELDATA ========

    @Nonnull
    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        builder.with(PizzaBakedModel.LAYER_PROVIDERS, Optional.of(getInventory()));
        builder.with(PizzaBakedModel.INTEGER_PROPERTY, Optional.of(getBlockState().getBlock() == ModBlocks.PIZZA.get() ? getBlockState().getValue(PizzaBlock.SLICES) : 0));
        builder.with(PizzaBakedModel.IS_RAW, Optional.of(isRaw()));
        ModelData modelData = builder.build();
        return modelData;
    }

    public ModelData getItemStackModelData(ItemStack stack) {
        load(stack.getOrCreateTag());
        ModelData.Builder builder = ModelData.builder();
        builder.with(PizzaBakedModel.LAYER_PROVIDERS, Optional.of(getInventory()));
        builder.with(PizzaBakedModel.INTEGER_PROPERTY, Optional.of(0));
        builder.with(PizzaBakedModel.IS_RAW, Optional.of(stack.getItem() == ModItems.RAW_PIZZA.get()));
        return builder.build();
    }
}
