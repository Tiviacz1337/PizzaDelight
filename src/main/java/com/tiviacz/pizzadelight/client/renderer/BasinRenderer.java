package com.tiviacz.pizzadelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.blockentity.BasinBlockEntity;
import com.tiviacz.pizzadelight.blockentity.content.BasinContent;
import com.tiviacz.pizzadelight.blockentity.content.BasinContentType;
import com.tiviacz.pizzadelight.util.RenderUtils;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class BasinRenderer implements BlockEntityRenderer<BasinBlockEntity> {
    private static final ResourceLocation MILK_TEX = ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "textures/block/milk.png");
    private static final ResourceLocation CHEESE_TEX = ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "textures/block/cheese.png");
    private static final ResourceLocation OLIVE_OIL_TEX = ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "textures/block/olive_oil.png");
    private final Random rand = new Random();

    protected ContentModel cheese;
    protected ContentModel milk;
    protected ContentModel model;

    public BasinRenderer(BlockEntityRendererProvider.Context context) {
        cheese = new ContentModel(context);
        milk = new ContentModel(context);
        model = new ContentModel(context);
    }

    @Override
    public void render(BasinBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        rand.setSeed(RenderUtils.CONSTANT_RENDERING_LONG);
        ResourceLocation tex = getTexture(blockEntity.getBasinContent());
        BasinContent basinContent = blockEntity.getBasinContent();

        poseStack.pushPose();

        if(basinContent.getContentType() == BasinContentType.FERMENTING_MILK || basinContent.getContentType() == BasinContentType.MILK) {
            float progressToFloatInv = 1.0F - (float)blockEntity.getFermentProgress() / blockEntity.getDefaultFermentTime();
            cheese.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entitySolid(CHEESE_TEX)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            milk.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entityTranslucent(MILK_TEX)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, progressToFloatInv);
        }
        if(basinContent.getContentType() == BasinContentType.CHEESE) {
            model.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entitySolid(tex)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        poseStack.popPose();
    }

    public ResourceLocation getTexture(BasinContent content) {
        return switch(content.getContentType()) {
            case MILK, FERMENTING_MILK -> MILK_TEX;
            case CHEESE -> CHEESE_TEX;
            default -> MissingTextureAtlasSprite.getLocation();
        };
    }

    public static class ContentModel extends Model {
        public static final ResourceLocation CONTENT = ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "content");
        public static final ModelLayerLocation CONTENT_LAYER = new ModelLayerLocation(CONTENT, "main");
        private final ModelPart content;

        public ContentModel(BlockEntityRendererProvider.Context context) {
            super(RenderType::entitySolid);
            content = context.getModelSet().bakeLayer(CONTENT_LAYER).getChild("main");
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
            this.content.render(poseStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

        public static LayerDefinition createModelData() {
            MeshDefinition mesh = new MeshDefinition();
            mesh.getRoot().addOrReplaceChild("main",
                    CubeListBuilder.create().addBox(2.0F, 1.0F, 2.0F, 12.0F, 6.0F, 12.0F),
                    PartPose.ZERO
            );
            return LayerDefinition.create(mesh, 64, 32);
        }
    }
}
