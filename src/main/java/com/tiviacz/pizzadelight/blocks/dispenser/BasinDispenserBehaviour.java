package com.tiviacz.pizzadelight.blocks.dispenser;

import com.tiviacz.pizzadelight.blockentity.BasinBlockEntity;
import com.tiviacz.pizzadelight.blockentity.content.BasinContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class BasinDispenserBehaviour {
    public static class Milk extends DefaultDispenseItemBehavior {
        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Level level = source.getLevel();
            BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
            if(level.getBlockEntity(blockpos) instanceof BasinBlockEntity basinBlockEntity) {
                if(basinBlockEntity.getBasinContent() == BasinContent.AIR) {
                    basinBlockEntity.addMilk(level, null, null);
                    return new ItemStack(Items.BUCKET);
                }
            }
            return stack;
        }
    }

    public static class Fermenting extends DefaultDispenseItemBehavior {
        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Level level = source.getLevel();
            BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
            if(level.getBlockEntity(blockpos) instanceof BasinBlockEntity basinBlockEntity) {
                if(basinBlockEntity.getBasinContent() == BasinContent.MILK) {
                    basinBlockEntity.useFermentingItem(stack, level, null);
                    return stack;
                }
            }
            return stack;
        }
    }
}