package com.tiviacz.pizzadelight.handlers;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.client.DynamicPizzaSliceModel;
import com.tiviacz.pizzadelight.client.PizzaBakedModel;
import com.tiviacz.pizzadelight.client.gui.ScreenPizza;
import com.tiviacz.pizzadelight.client.gui.ScreenPizzaStation;
import com.tiviacz.pizzadelight.client.renderer.BasinRenderer;
import com.tiviacz.pizzadelight.client.renderer.ChefHatModel;
import com.tiviacz.pizzadelight.client.renderer.PizzaWithoutLevelRenderer;
import com.tiviacz.pizzadelight.client.tooltip.ClientIngredientsTooltip;
import com.tiviacz.pizzadelight.client.tooltip.ClientPizzaTooltipComponent;
import com.tiviacz.pizzadelight.client.tooltip.IngredientsTooltip;
import com.tiviacz.pizzadelight.client.tooltip.PizzaTooltipComponent;
import com.tiviacz.pizzadelight.init.ModBlocks;
import com.tiviacz.pizzadelight.init.ModItems;
import com.tiviacz.pizzadelight.init.ModMenuTypes;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = PizzaDelight.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(PizzaTooltipComponent.class, ClientPizzaTooltipComponent::new);
        event.register(IngredientsTooltip.class, ClientIngredientsTooltip::new);
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "pizza_slice_loader"), DynamicPizzaSliceModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem((new IClientItemExtensions() {
            private final Supplier<BlockEntityWithoutLevelRenderer> renderer = PizzaWithoutLevelRenderer::new;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        }), ModItems.RAW_PIZZA.get());

        event.registerItem((new IClientItemExtensions() {
            private final Supplier<BlockEntityWithoutLevelRenderer> renderer = PizzaWithoutLevelRenderer::new;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        }), ModItems.PIZZA.get());

        event.registerItem((new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> humanoid) {
                if(armorSlot == EquipmentSlot.HEAD) {
                    ChefHatModel hat = new ChefHatModel(ChefHatModel.createModelData().bakeRoot());
                    humanoid.copyPropertiesTo(hat);

                    return hat;
                }
                return humanoid;
            }
        }), ModItems.CHEF_HAT.get());
    }

    @SubscribeEvent
    public static void registerMenuScreensEvent(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.PIZZA.get(), ScreenPizza::new);
        event.register(ModMenuTypes.PIZZA_STATION.get(), ScreenPizzaStation::new);
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