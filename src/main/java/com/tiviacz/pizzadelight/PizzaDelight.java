package com.tiviacz.pizzadelight;

import com.tiviacz.pizzadelight.blockentity.content.BasinContent;
import com.tiviacz.pizzadelight.blocks.dispenser.PizzaDispenserBehaviour;
import com.tiviacz.pizzadelight.client.gui.ScreenPizza;
import com.tiviacz.pizzadelight.client.gui.ScreenPizzaStation;
import com.tiviacz.pizzadelight.client.renderer.BasinRenderer;
import com.tiviacz.pizzadelight.client.renderer.PizzaRenderer;
import com.tiviacz.pizzadelight.compat.appleskin.PizzaCompat;
import com.tiviacz.pizzadelight.config.PizzaDelightConfig;
import com.tiviacz.pizzadelight.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("pizzadelight")
public class PizzaDelight {
    public static final String MODID = "pizzadelight";
    public static final Logger LOGGER = LogManager.getLogger();
    public static SimpleChannel NETWORK;

    public static boolean appleSkinLoaded;

    public PizzaDelight() {
        PizzaDelightConfig.register(ModLoadingContext.get());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onFinish);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        appleSkinLoaded = ModList.get().isLoaded("appleskin");

        if(appleSkinLoaded) PizzaCompat.load();
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModNetwork.registerNetworkChannel();
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
        //Screens
        MenuScreens.register(ModMenuTypes.PIZZA.get(), ScreenPizza::new);
        MenuScreens.register(ModMenuTypes.PIZZA_STATION.get(), ScreenPizzaStation::new);

        //BlockEntityRenderers
        BlockEntityRenderers.register(ModBlockEntityTypes.BASIN.get(), BasinRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.PIZZA.get(), PizzaRenderer::new);

        //RenderTypes
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BASIN.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PIZZA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAW_PIZZA.get(), RenderType.cutout());
    }

    private void onFinish(final FMLLoadCompleteEvent event) {
        PizzaLayers.setMaps();
    }
}