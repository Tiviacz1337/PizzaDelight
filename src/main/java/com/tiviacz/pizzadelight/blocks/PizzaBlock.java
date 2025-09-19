package com.tiviacz.pizzadelight.blocks;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.blocks.dispenser.PizzaDispenserBehaviour;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.init.ModItems;
import com.tiviacz.pizzadelight.items.PizzaPeelItem;
import com.tiviacz.pizzadelight.util.TextUtils;
import com.tiviacz.pizzadelight.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PizzaBlock extends AbstractPizzaBlock {
    public static final IntegerProperty SLICES = IntegerProperty.create("slices", 0, 3);

    public PizzaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(SLICES, 0));
    }

    public ItemStack getPizzaSliceItem() {
        return new ItemStack(ModItems.PIZZA_SLICE.get());
    }

    public ItemStack getPizzaSliceItem(@Nonnull Level level, @Nonnull BlockPos pos) {
        if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) {
            ItemStack stack = getPizzaSliceItem();
            blockEntity.getSlice(stack);
            return stack;
        }
        return getPizzaSliceItem();
    }

    public int getMaxSlices() {
        return 4;
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        pPlayer.hurt(pPlayer.damageSources().onFire(), 1.0F);
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(handIn);
        if(!heldStack.isEmpty()) {
            return heldStack.is(ModTags.KNIVES) ? this.cutSlice(level, pos, state, player.getDirection().getOpposite()) : (heldStack.getItem() instanceof PizzaPeelItem && state.getValue(SLICES) == 0) ? this.pickUpPizza(level, pos, state, player.getDirection().getOpposite()) : InteractionResult.PASS;
        }

        if(level.isClientSide) {
            if(this.consumeSlice(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return this.consumeSlice(level, pos, state, player);
    }

    protected InteractionResult consumeSlice(Level level, BlockPos pos, BlockState state, Player playerIn) {
        if(!playerIn.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            ItemStack sliceStack = this.getPizzaSliceItem(level, pos);
            FoodProperties sliceFood = sliceStack.getItem().getFoodProperties(sliceStack, playerIn);
            if(sliceFood != null) {
                playerIn.getFoodData().eat(sliceStack.getItem(), sliceStack, playerIn);

                for(Pair<MobEffectInstance, Float> effect : sliceFood.getEffects()) {
                    if(!level.isClientSide && effect != null && level.random.nextFloat() < effect.getSecond()) {
                        playerIn.addEffect(effect.getFirst());
                    }
                }
            }

            int slices = state.getValue(SLICES);
            if(slices < this.getMaxSlices() - 1) {
                level.setBlock(pos, state.setValue(SLICES, slices + 1), 3);
            } else {
                level.removeBlock(pos, false);
            }

            if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) blockEntity.requestModelDataUpdate();

            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 0.8F);
            return InteractionResult.SUCCESS;
        }
    }

    public InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Direction direction) {
        int slices = state.getValue(SLICES);
        ItemStack pizzaSlice = this.getPizzaSliceItem(level, pos);

        if(slices < this.getMaxSlices() - 1) {
            level.setBlock(pos, state.setValue(SLICES, slices + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) blockEntity.requestModelDataUpdate();

        ItemUtils.spawnItemEntity(level, pizzaSlice, (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SLICES);
    }

    /*@Override
    public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction side) {
        return this.getMaxSlices() - blockState.getValue(SLICES);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flagIn) {
        FoodProperties foodProperties = stack.getItem().getFoodProperties(stack, null);

        addInformationForPizza(stack, tooltip, foodProperties != null && !foodProperties.getEffects().isEmpty());
    }

    public static void addInformationForPizza(ItemStack stack, List<Component> tooltip, boolean addEffects) {
        if(addEffects) {
            TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
        }

        if(!Utils.isShiftPressed()) {
            tooltip.add(Component.translatable("information.pizzadelight.view_ingredients"));
        }
    }
}