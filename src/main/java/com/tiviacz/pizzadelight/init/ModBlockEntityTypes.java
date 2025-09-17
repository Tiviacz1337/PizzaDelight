package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.blockentity.BasinBlockEntity;
import com.tiviacz.pizzadelight.blockentity.PizzaBlockEntity;
import com.tiviacz.pizzadelight.blockentity.PizzaStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PizzaDelight.MODID);

    public static final RegistryObject<BlockEntityType<PizzaBlockEntity>> PIZZA = BLOCK_ENTITY_TYPES.register("pizza",
            () -> BlockEntityType.Builder.of(PizzaBlockEntity::new, ModBlocks.RAW_PIZZA.get(), ModBlocks.PIZZA.get()).build(null));

    public static final RegistryObject<BlockEntityType<BasinBlockEntity>> BASIN = BLOCK_ENTITY_TYPES.register("basin",
            () -> BlockEntityType.Builder.of(BasinBlockEntity::new,
                    ModBlocks.BASIN.get()).build(null));

    public static final RegistryObject<BlockEntityType<PizzaStationBlockEntity>> PIZZA_STATION = BLOCK_ENTITY_TYPES.register("pizza_station",
            () -> BlockEntityType.Builder.of(PizzaStationBlockEntity::new,
                    ModBlocks.PIZZA_STATION.get()).build(null));
}