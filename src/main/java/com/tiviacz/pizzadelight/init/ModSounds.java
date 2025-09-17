package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PizzaDelight.MODID);

    public static final RegistryObject<SoundEvent> BLOCK_PIZZA_SIZZLING = SOUND_EVENTS.register("block.pizza.sizzling", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PizzaDelight.MODID, "block.pizza.sizzling")));
    public static final RegistryObject<SoundEvent> BLOCK_BASIN_FERMENTING = SOUND_EVENTS.register("block.basin.fermenting", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PizzaDelight.MODID, "block.basin.fermenting")));
}
