package com.tiviacz.pizzadelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.client.PizzaBakedModel;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.util.BlockAlphaRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.client.model.data.ModelData;

import java.util.Optional;

public class PizzaRenderer implements BlockEntityRenderer<PizzaBlockEntity> {
    public PizzaRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PizzaBlockEntity tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tileEntityIn.isBaking()) {
            renderPizzaBakingProcess(tileEntityIn, poseStack, bufferIn);
        }
    }

    public void renderPizzaBakingProcess(PizzaBlockEntity tileEntity, PoseStack poseStack, MultiBufferSource bufferIn) {
        poseStack.pushPose();

        //Translate and scale renderer to avoid z-fighting
        poseStack.translate(-0.0025D, 0D, -0.0025D);
        poseStack.scale(1.005F, 1.005F, 1.005F);

        //Set IModelData to baked pizza, just as we want it
        ModelData modelData = tileEntity.getModelData();
        modelData.derive().with(PizzaBakedModel.IS_RAW, Optional.of(false));

        //Custom implementation of BlockModelRenderer with accessible alpha value (Thanks Commoble!)
        BlockAlphaRenderer.renderBlockAlpha(tileEntity.getBlockPos(), ModBlocks.PIZZA.get().defaultBlockState(), tileEntity.getLevel(), poseStack, bufferIn, modelData);
        poseStack.popPose();
    }
}