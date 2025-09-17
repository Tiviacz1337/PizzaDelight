package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, PizzaDelight.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_PIZZA_SIZZLING = SOUND_EVENTS.register("block.pizza.sizzling", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "block.pizza.sizzling")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BLOCK_BASIN_FERMENTING = SOUND_EVENTS.register("block.basin.fermenting", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "block.basin.fermenting")));
}
