package com.tiviacz.pizzadelight.blocks.dispenser;

import com.tiviacz.pizzadelight.blocks.AbstractPizzaBlock;
import com.tiviacz.pizzadelight.blocks.PizzaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PizzaDispenserBehaviour {
    public static class CutSlice extends DefaultDispenseItemBehavior {
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            ServerLevel serverlevel = pSource.getLevel();
            if(!serverlevel.isClientSide()) {
                Direction facing = pSource.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockpos = pSource.getPos().relative(facing);
                boolean success = tryCutSlice(serverlevel, blockpos, facing);
                if(success && pStack.hurt(1, serverlevel.getRandom(), null)) {
                    pStack.setCount(0);
                }
            }
            return pStack;
        }

        private static boolean tryCutSlice(ServerLevel level, BlockPos pos, Direction direction) {
            BlockState blockstate = level.getBlockState(pos);
            if(blockstate.getBlock() instanceof PizzaBlock pizzaBlock) {
                pizzaBlock.cutSlice(level, pos, blockstate, direction);
                return true;
            }
            return false;
        }
    }

    public static class Pickup extends DefaultDispenseItemBehavior {
        @Override
        protected ItemStack execute(BlockSource pSource, ItemStack pStack) {
            ServerLevel serverlevel = pSource.getLevel();
            if(!serverlevel.isClientSide()) {
                Direction facing = pSource.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos blockpos = pSource.getPos().relative(facing);
                boolean success = tryPickupPizza(serverlevel, blockpos, facing);
                if(success && pStack.hurt(1, serverlevel.getRandom(), null)) {
                    pStack.setCount(0);
                }
            }
            return pStack;
        }

        private static boolean tryPickupPizza(ServerLevel level, BlockPos pos, Direction direction) {
            BlockState blockstate = level.getBlockState(pos);
            if(blockstate.getBlock() instanceof AbstractPizzaBlock abstractPizzaBlock) {
                abstractPizzaBlock.pickUpPizza(level, pos, blockstate, direction);
                return true;
            }
            return false;
        }
    }
}