package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PizzaDelight.MODID);

    public static final DeferredBlock<Block> PIZZA = registerBlock("pizza", () -> new PizzaBlock(Block.Properties.ofFullCopy(Blocks.CAKE)));
    public static final DeferredBlock<Block> RAW_PIZZA = registerBlock("raw_pizza", () -> new RawPizzaBlock(Block.Properties.ofFullCopy(Blocks.CAKE)));
    public static final DeferredBlock<Block> CHEESE_BLOCK = registerBlock("cheese_block", () -> new CheeseBlock(Block.Properties.ofFullCopy(Blocks.CAKE).mapColor(MapColor.COLOR_YELLOW).sound(SoundType.FUNGUS)));
    public static final DeferredBlock<Block> PIZZA_STATION = registerBlock("pizza_station", () -> new PizzaStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)));
    public static final DeferredBlock<Block> BASIN = registerBlock("basin", () -> new BasinBlock(Block.Properties.ofFullCopy(vectorwing.farmersdelight.common.registry.ModBlocks.COOKING_POT.get())));

    public static DeferredBlock<Block> registerBlock(final String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }
}