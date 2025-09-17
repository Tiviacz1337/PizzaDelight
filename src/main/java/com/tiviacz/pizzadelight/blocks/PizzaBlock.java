package com.tiviacz.pizzadelight.blocks;

import com.mojang.datafixers.util.Pair;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PizzaBlock extends Block implements EntityBlock {
    public static final IntegerProperty SLICES = IntegerProperty.create("slices", 0, 3);
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);

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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        pPlayer.hurt(pPlayer.damageSources().onFire(), 1.0F);
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }

  /*  @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) //#TODO add particle for cooked pizza
    {
        if(rand.nextInt(3) == 0 && level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity)
        {
            //if(blockEntity.isFresh())
            {
                double[] particlePos = RenderUtils.getPosRandomAboveBlockHorizontal(level, pos);
                //worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.SIZZLING_SOUND.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                for(int i = 0; i < 2; i++)
                {
                    //worldIn.addParticle(ParticleTypes.POOF, pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D, 0D, 0.025D, 0D);
                    level.addParticle(ParticleTypes.POOF, particlePos[0], pos.getY() + 0.4D, particlePos[1], 0D, 0.025D, 0D);
                }
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, particlePos[0], pos.getY() + 0.3D, particlePos[1], 0D, 3.0D + rand.nextDouble(), 0.0D);
            }
        }
    } */

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldStack = player.getItemInHand(handIn);
        if(!heldStack.isEmpty()) {
            return heldStack.is(ModTags.KNIVES) ? this.cutSlice(level, pos, state, player) : (heldStack.getItem() instanceof PizzaPeelItem && state.getValue(SLICES) == 0) ? this.pickUpPizza(level, pos, state, player) : InteractionResult.PASS;
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

    protected InteractionResult cutSlice(Level level, BlockPos pos, BlockState state, Player player) {
        int slices = state.getValue(SLICES);
        ItemStack pizzaSlice = this.getPizzaSliceItem(level, pos);

        if(slices < this.getMaxSlices() - 1) {
            level.setBlock(pos, state.setValue(SLICES, slices + 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) blockEntity.requestModelDataUpdate();

        Direction direction = player.getDirection().getOpposite();
        ItemUtils.spawnItemEntity(level, pizzaSlice, (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        return InteractionResult.SUCCESS;
    }

    protected InteractionResult pickUpPizza(Level level, BlockPos pos, BlockState state, Player player) {
        PizzaBlockEntity blockEntity = (PizzaBlockEntity)level.getBlockEntity(pos);
        ItemStack pizza = asItem().getDefaultInstance();
        pizza = blockEntity.cloneToItemStack(pizza);

        Direction direction = player.getDirection().getOpposite();
        ItemUtils.spawnItemEntity(level, pizza, (double)pos.getX() + 0.5, (double)pos.getY() + 0.3, (double)pos.getZ() + 0.5, (double)direction.getStepX() * 0.15, 0.05, (double)direction.getStepZ() * 0.15);
        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) {
            if(stack.getTag() != null) {
                blockEntity.load(stack.getOrCreateTag());
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, world, pos, player);
        if(world.getBlockEntity(pos) instanceof PizzaBlockEntity blockEntity) {
            blockEntity.cloneToItemStack(stack);
        }
        return stack;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !stateIn.isViewBlocking(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SLICES);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter level, BlockPos pos, Direction side) {
        return this.getMaxSlices() - blockState.getValue(SLICES);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PizzaBlockEntity(pos, state);
    }

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