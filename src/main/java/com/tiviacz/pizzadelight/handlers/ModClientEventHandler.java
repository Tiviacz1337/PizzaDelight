package com.tiviacz.pizzadelight.handlers;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.client.DynamicPizzaSliceModel;
import com.tiviacz.pizzadelight.client.PizzaBakedModel;
import com.tiviacz.pizzadelight.client.renderer.BasinRenderer;
import com.tiviacz.pizzadelight.client.renderer.ChefHatModel;
import com.tiviacz.pizzadelight.client.tooltip.ClientIngredientsTooltip;
import com.tiviacz.pizzadelight.client.tooltip.ClientPizzaTooltipComponent;
import com.tiviacz.pizzadelight.client.tooltip.IngredientsTooltip;
import com.tiviacz.pizzadelight.client.tooltip.PizzaTooltipComponent;
import com.tiviacz.pizzadelight.init.ModBlocks;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PizzaDelight.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(PizzaTooltipComponent.class, ClientPizzaTooltipComponent::new);
        event.register(IngredientsTooltip.class, ClientIngredientsTooltip::new);
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterGeometryLoaders event) {
        event.register("pizza_slice_loader", DynamicPizzaSliceModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BasinRenderer.ContentModel.CONTENT_LAYER, BasinRenderer.ContentModel::createModelData);
        event.registerLayerDefinition(ChefHatModel.CHEF_HAT, ChefHatModel::createModelData);
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {
        for(BlockState blockState : ModBlocks.PIZZA.get().getStateDefinition().getPossibleStates()) {
            ModelResourceLocation variantMRL = BlockModelShaper.stateToModelLocation(blockState);
            BakedModel existingModel = event.getModels().get(variantMRL);
            if(existingModel == null) {
                //LOGGER.warn("Did not find the expected vanilla baked model(s) for blockAltimeter in registry");
            } else if(existingModel instanceof PizzaBakedModel) {
                // LOGGER.warn("Tried to replace AltimeterBakedModel twice");
            } else {
                PizzaBakedModel customModel = new PizzaBakedModel(existingModel);
                event.getModels().put(variantMRL, customModel);
            }
        }

        for(BlockState blockState : ModBlocks.RAW_PIZZA.get().getStateDefinition().getPossibleStates()) {
            ModelResourceLocation variantMRL = BlockModelShaper.stateToModelLocation(blockState);
            BakedModel existingModel = event.getModels().get(variantMRL);
            if(existingModel == null) {
                //LOGGER.warn("Did not find the expected vanilla baked model(s) for blockAltimeter in registry");
            } else if(existingModel instanceof PizzaBakedModel) {
                // LOGGER.warn("Tried to replace AltimeterBakedModel twice");
            } else {
                PizzaBakedModel customModel = new PizzaBakedModel(existingModel);
                event.getModels().put(variantMRL, customModel);
            }
        }
    }
}