package com.tiviacz.pizzadelight.blockentity;

import com.tiviacz.pizzadelight.blockentity.content.BasinContent;
import com.tiviacz.pizzadelight.blockentity.content.BasinContentRegistry;
import com.tiviacz.pizzadelight.blockentity.content.BasinContentType;
import com.tiviacz.pizzadelight.init.ModBlockEntityTypes;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.init.ModSounds;
import com.tiviacz.pizzadelight.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public class BasinBlockEntity extends BaseBlockEntity {
    private BasinContent content = BasinContent.AIR;
    private int fermentProgress = 0;
    private final int defaultFermentTime = 1200;

    private static final String BASIN_CONTENT = "BasinContent";
    private static final String FERMENT_PROGRESS = "FermentProgress";

    public BasinBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BASIN.get(), pos, state);
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider pRegistries) {
        super.loadAdditional(compound, pRegistries);
        this.content = BasinContentRegistry.REGISTRY.fromString(compound.getString(BASIN_CONTENT));
        this.fermentProgress = compound.getInt(FERMENT_PROGRESS);
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider pRegistries) {
        super.saveAdditional(compound, pRegistries);
        compound.putString(BASIN_CONTENT, content.toString());
        compound.putInt(FERMENT_PROGRESS, this.fermentProgress);
    }

    public BasinContent getBasinContent() {
        return this.content;
    }

    public ItemInteractionResult addMilk(Level level, Player player, InteractionHand hand) {
        this.content = BasinContent.MILK;
        if(!player.isCreative()) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        level.playSound(player, getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 0.8F, 1.0F);
        this.setChanged();
        return ItemInteractionResult.SUCCESS;
    }

    public ItemInteractionResult removeMilk(ItemStack heldStack, Level level, Player player) {
        this.content = BasinContent.AIR;
        if(!player.isCreative()) {
            heldStack.shrink(1);
            if(!player.addItem(new ItemStack(Items.MILK_BUCKET))) {
                level.addFreshEntity(new ItemEntity(player.level(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), new ItemStack(Items.MILK_BUCKET)));
            }
        }
        resetFermenting();
        level.playSound(player, getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.8F, 1.0F);
        this.setChanged();
        return ItemInteractionResult.SUCCESS;
    }

    public ItemInteractionResult useFermetingItem(ItemStack heldStack, Level level, Player player) {
        if(getBasinContent() == BasinContent.MILK) {
            if(heldStack.is(ModTags.FERMENTING_ITEMS_TAG)) {
                level.playSound(player, getBlockPos(), SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if(!player.isCreative()) {
                    heldStack.shrink(1);
                }

                this.content = BasinContent.FERMENTING_MILK;
                this.setChanged();

                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public InteractionResult removeCheese(Level level, Player player) {
        if(getBasinContent() == BasinContent.CHEESE) {
            Direction direction = player.getDirection().getOpposite();
            ItemUtils.spawnItemEntity(getLevel(), ModBlocks.CHEESE_BLOCK.toStack(), (double)getBlockPos().getX() + 0.5, (double)getBlockPos().getY() + 0.3, (double)getBlockPos().getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
            this.content = BasinContent.AIR;
            level.playSound(player, getBlockPos(), SoundEvents.FUNGUS_PLACE, SoundSource.BLOCKS, 0.8F, 0.9F + level.random.nextFloat());
            this.setChanged();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    // ======== FERMENTING ========

    public int getFermentProgress() {
        return this.fermentProgress;
    }

    public int getDefaultFermentTime() {
        return this.defaultFermentTime;
    }

    public int getComparatorOutput() {
        float f = (float)this.fermentProgress / defaultFermentTime;
        return (int)(f * 15);
    }

    public void finishFermenting() {
        if(getBasinContent().getContentType() == BasinContentType.FERMENTING_MILK) {
            this.fermentProgress = 0;
            this.content = BasinContent.CHEESE;
        }
        setChanged();
    }

    public void resetFermenting() {
        this.fermentProgress = 0;
        setChanged();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BasinBlockEntity blockEntity) {
        boolean save = false;

        if(blockEntity.getBasinContent() != null) {
            if(blockEntity.getBasinContent().getContentType() == BasinContentType.FERMENTING_MILK) {
                blockEntity.fermentProgress++;
                save = true;

                if(level.random.nextDouble() < 0.005D) {
                    level.playSound(null, pos, ModSounds.BLOCK_BASIN_FERMENTING.get(), SoundSource.BLOCKS, 0.8F, 0.9F + level.random.nextFloat());
                }

                if(level.random.nextDouble() < 0.05D) {
                    blockEntity.createMilkParticles((float)blockEntity.getFermentProgress() / blockEntity.getDefaultFermentTime());
                }
            }
            if(blockEntity.getFermentProgress() >= blockEntity.getDefaultFermentTime()) {
                blockEntity.finishFermenting();
            }

            if(blockEntity.getBasinContent().getContentType() == BasinContentType.CHEESE) {
                if(level.random.nextDouble() < 0.05D) {
                    blockEntity.createCheeseParticle();
                }
            }
        }

        if(save) {
            blockEntity.setChanged();
        }
    }

    private void createMilkParticles(float fermentProgress) {
        float[] milkColor = new float[]{1.0F, 1.0F, 1.0F};
        float[] cheeseColor = new float[]{0.91F, 0.76F, 0.31F};
        float[] currentColor = new float[3];

        for(int i = 0; i < 3; i++) {
            currentColor[i] = (1.0F - fermentProgress) * milkColor[i] + fermentProgress * cheeseColor[i];
        }

        double x = ((double)level.random.nextInt(12) / 16);
        double z = ((double)level.random.nextInt(12) / 16);
        level.addParticle(new DustParticleOptions(new Vector3f(currentColor[0], currentColor[1], currentColor[2]), 1.0F), getBlockPos().getX() + x + 0.2D, getBlockPos().getY() + 0.6D, getBlockPos().getZ() + z + 0.2D, 0.0D, 0.09D, 0.0D);
    }

    private void createCheeseParticle() {
        double x = ((double)level.random.nextInt(12) / 16);
        double z = ((double)level.random.nextInt(12) / 16);
        level.addParticle(new DustParticleOptions(new Vector3f(0.91F, 0.76F, 0.31F), 1.0F), getBlockPos().getX() + x + 0.2D, getBlockPos().getY() + 0.6D, getBlockPos().getZ() + z + 0.2D, 0.0D, 0.09D, 0.0D);
    }
}