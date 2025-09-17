package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PizzaDelight.MODID);

    public static final RegistryObject<Block> PIZZA = registerBlock("pizza", () -> new PizzaBlock(Block.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> RAW_PIZZA = registerBlock("raw_pizza", () -> new RawPizzaBlock(Block.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Block> CHEESE_BLOCK = registerBlock("cheese_block", () -> new CheeseBlock(Block.Properties.copy(Blocks.CAKE).mapColor(MapColor.COLOR_YELLOW).strength(0.5F).sound(SoundType.FUNGUS)));
    public static final RegistryObject<Block> PIZZA_STATION = registerBlock("pizza_station", () -> new PizzaStationBlock(BlockBehaviour.Properties.copy(Blocks.STONE).mapColor(MapColor.STONE).strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> BASIN = registerBlock("basin", () -> new BasinBlock(Block.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK).strength(1.5F, 6.0F)));

    //Crops
    //public static final RegistryObject<Block> WILD_PEPPERS = registerBlock("wild_peppers", () -> new WildCropBlock(MobEffects.GLOWING, 6, BlockBehaviour.Properties.copy(Blocks.TALL_GRASS)));
    //public static final RegistryObject<Block> PEPPER_CROP = registerBlock("peppers", () -> new PepperBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static RegistryObject<Block> registerBlock(final String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }
}