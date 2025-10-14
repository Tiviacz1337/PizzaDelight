package com.tiviacz.pizzadelight.blocks;

import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.init.ModBlockEntityTypes;
import com.tiviacz.pizzadelight.init.ModSounds;
import com.tiviacz.pizzadelight.items.PizzaPeelItem;
import com.tiviacz.pizzadelight.util.RenderUtils;
import com.tiviacz.pizzadelight.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class RawPizzaBlock extends AbstractPizzaBlock {
    public RawPizzaBlock(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) {
            if(blockEntity.isBaking()) {
                //worldIn.playSound(null, pos, ModSounds.SIZZLING_SOUND.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);

                if(rand.nextDouble() < 0.3D) {
                    level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_PIZZA_SIZZLING.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }

                if(rand.nextDouble() > 0.5D) {
                    double[] particlePos = RenderUtils.getPosRandomAboveBlockHorizontal(level, pos);
                    level.addParticle(ParticleTypes.POOF, particlePos[0], pos.getY() + 0.4D, particlePos[1], 0D, 0.025D, 0D);
                }
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldStack = player.getItemInHand(hand);
        return heldStack.getItem() instanceof PizzaPeelItem ? this.pickUpPizza(level, pos, state, player.getDirection().getOpposite()) : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity && !player.isCreative() && !(player.getMainHandItem().getItem() instanceof PizzaPeelItem)) {
            for(int i = 0; i < blockEntity.getInventory().getSlots(); i++) {
                if(!blockEntity.getInventory().getStackInSlot(i).isEmpty()) {
                    Utils.spawnItemStackInWorld(level, pos, blockEntity.getInventory().getStackInSlot(i));
                    blockEntity.getInventory().setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!(newState.getBlock() instanceof PizzaBlock)) {
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return Utils.getTicker(blockEntityType, ModBlockEntityTypes.PIZZA.get(), PizzaBlockEntity::tick);
    }
}