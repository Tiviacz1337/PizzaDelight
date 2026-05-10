package com.tiviacz.pizzadelight.blocks;

import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.init.ModItems;
import com.tiviacz.pizzadelight.items.PizzaPeelItem;
import com.tiviacz.pizzadelight.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

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
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return heldStack.is(ModTags.Items.KNIVES) ? this.cutSlice(level, pos, state, player.getDirection().getOpposite()) : (heldStack.getItem() instanceof PizzaPeelItem && state.getValue(SLICES) == 0) ? this.pickUpPizza(level, pos, state, player.getDirection().getOpposite()) : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
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
                playerIn.getFoodData().eat(sliceFood);
                Iterator var7 = sliceFood.effects().iterator();

                while(var7.hasNext()) {
                    FoodProperties.PossibleEffect effect = (FoodProperties.PossibleEffect)var7.next();
                    if(!level.isClientSide && effect != null && level.random.nextFloat() < effect.probability()) {
                        playerIn.addEffect(effect.effect());
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

    public ItemInteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Direction direction) {
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
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SLICES);
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return this.getMaxSlices() - blockState.getValue(SLICES);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        FoodProperties foodProperties = stack.get(DataComponents.FOOD);

        addInformationForPizza(stack, tooltipComponents::add, foodProperties != null && !foodProperties.effects().isEmpty(), context.tickRate());
    }

    public static void addInformationForPizza(ItemStack stack, Consumer<Component> tooltip, boolean addEffects, float tickRate) {
        if(addEffects) {
            TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F, tickRate);
        }

        if(!Utils.isShiftPressed()) {
            tooltip.accept(Component.translatable("information.pizzadelight.view_ingredients"));
        }
    }
}