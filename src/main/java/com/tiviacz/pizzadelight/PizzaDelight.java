package com.tiviacz.pizzadelight;

import com.tiviacz.pizzadelight.blockentity.content.BasinContent;
import com.tiviacz.pizzadelight.blocks.dispenser.PizzaDispenserBehaviour;
import com.tiviacz.pizzadelight.client.renderer.BasinRenderer;
import com.tiviacz.pizzadelight.client.renderer.PizzaRenderer;
import com.tiviacz.pizzadelight.config.PizzaDelightConfig;
import com.tiviacz.pizzadelight.init.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("pizzadelight")
public class PizzaDelight {
    public static final String MODID = "pizzadelight";
    public static final Logger LOGGER = LogManager.getLogger();

    public PizzaDelight(IEventBus eventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, PizzaDelightConfig.serverSpec);

        if(FMLEnvironment.dist == Dist.CLIENT)
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        eventBus.addListener(this::onFinish);

        ModItems.ITEMS.register(eventBus);
        ModBlocks.BLOCKS.register(eventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(eventBus);
        ModMenuTypes.MENU_TYPES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(eventBus);
        ModDataComponents.DATA_COMPONENT_TYPES.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModVanillaCompat.setup();
            BasinContent.register();
            DispenserBlock.registerBehavior(ModItems.STONE_PIZZA_PEEL.get(), new PizzaDispenserBehaviour.Pickup());
            DispenserBlock.registerBehavior(ModItems.IRON_PIZZA_PEEL.get(), new PizzaDispenserBehaviour.Pickup());
            DispenserBlock.registerBehavior(ModItems.GOLDEN_PIZZA_PEEL.get(), new PizzaDispenserBehaviour.Pickup());
            DispenserBlock.registerBehavior(ModItems.DIAMOND_PIZZA_PEEL.get(), new PizzaDispenserBehaviour.Pickup());
            DispenserBlock.registerBehavior(ModItems.NETHERITE_PIZZA_PEEL.get(), new PizzaDispenserBehaviour.Pickup());

            DispenserBlock.registerBehavior(vectorwing.farmersdelight.common.registry.ModItems.FLINT_KNIFE.get(), new PizzaDispenserBehaviour.CutSlice());
            DispenserBlock.registerBehavior(vectorwing.farmersdelight.common.registry.ModItems.IRON_KNIFE.get(), new PizzaDispenserBehaviour.CutSlice());
            DispenserBlock.registerBehavior(vectorwing.farmersdelight.common.registry.ModItems.GOLDEN_KNIFE.get(), new PizzaDispenserBehaviour.CutSlice());
            DispenserBlock.registerBehavior(vectorwing.farmersdelight.common.registry.ModItems.DIAMOND_KNIFE.get(), new PizzaDispenserBehaviour.CutSlice());
            DispenserBlock.registerBehavior(vectorwing.farmersdelight.common.registry.ModItems.NETHERITE_KNIFE.get(), new PizzaDispenserBehaviour.CutSlice());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        //BlockEntityRenderers
        BlockEntityRenderers.register(ModBlockEntityTypes.BASIN.get(), BasinRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.PIZZA.get(), PizzaRenderer::new);

        //RenderTypes
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PIZZA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAW_PIZZA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BASIN.get(), RenderType.cutout());
    }

    private void onFinish(final FMLLoadCompleteEvent event) {
        PizzaLayers.setMaps();
    }
}