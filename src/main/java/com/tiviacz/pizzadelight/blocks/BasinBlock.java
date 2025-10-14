package com.tiviacz.pizzadelight.blocks;

import com.tiviacz.pizzadelight.blockentity.BasinBlockEntity;
import com.tiviacz.pizzadelight.blockentity.content.BasinContent;
import com.tiviacz.pizzadelight.init.ModBlockEntityTypes;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.tags.ModTags;
import com.tiviacz.pizzadelight.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;

public class BasinBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = box(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);

    public BasinBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof BasinBlockEntity blockEntity) {
            if(stack.is(Tags.Items.BUCKETS_MILK) && blockEntity.getBasinContent() == BasinContent.AIR) {
                return blockEntity.addMilk(level, player, hand);
            }
            if(stack.is(ModTags.FERMENTING_ITEMS_TAG) && blockEntity.getBasinContent() == BasinContent.MILK) {
                return blockEntity.useFermentingItem(stack, level, player);
            }
            if(stack.is(Tags.Items.BUCKETS_EMPTY) && blockEntity.getBasinContent() == BasinContent.MILK) {
                return blockEntity.removeMilk(stack, level, player);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BasinBlockEntity blockEntity = (BasinBlockEntity)level.getBlockEntity(pos);
        if(level.isClientSide) {
            if(blockEntity.removeCheese(level, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }

            if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.CONSUME;
            }
        }

        return blockEntity.removeCheese(level, player);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()) {
            if(world.getBlockEntity(pos) instanceof BasinBlockEntity blockEntity) {
                if(blockEntity.getBasinContent() == BasinContent.CHEESE) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ModBlocks.CHEESE_BLOCK.toStack());
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if(level.getBlockEntity(pos) instanceof BasinBlockEntity blockEntity) {
            return blockEntity.getComparatorOutput();
        }
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BasinBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return Utils.getTicker(blockEntityType, ModBlockEntityTypes.BASIN.get(), BasinBlockEntity::tick);
    }
}
