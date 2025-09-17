package com.tiviacz.pizzadelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiviacz.pizzadelight.client.PizzaBakedModel;
import com.tiviacz.pizzadelight.components.PizzaIngredients;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.init.ModDataComponents;
import com.tiviacz.pizzadelight.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class PizzaWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    public PizzaWithoutLevelRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState pState = stack.getItem() == ModItems.RAW_PIZZA.get() ? ModBlocks.RAW_PIZZA.get().defaultBlockState() : ModBlocks.PIZZA.get().defaultBlockState();
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(pState, poseStack, buffer, combinedLight, combinedOverlay, getItemStackModelData(stack), RenderTypeHelper.getEntityRenderType(RenderType.solid(), false));
    }

    public ModelData getItemStackModelData(ItemStack stack) {
        ItemStackHandler handler = new ItemStackHandler(stack.getOrDefault(ModDataComponents.PIZZA_INGREDIENTS, PizzaIngredients.EMPTY).getIngredients());
        ModelData.Builder builder = ModelData.builder();
        builder.with(PizzaBakedModel.LAYER_PROVIDERS, Optional.of(handler));
        builder.with(PizzaBakedModel.INTEGER_PROPERTY, Optional.of(0));
        builder.with(PizzaBakedModel.IS_RAW, Optional.of(stack.getItem() == ModItems.RAW_PIZZA.get()));
        return builder.build();
    }
}