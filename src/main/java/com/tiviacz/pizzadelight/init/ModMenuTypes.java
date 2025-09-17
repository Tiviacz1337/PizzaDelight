package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.container.PizzaMenu;
import com.tiviacz.pizzadelight.container.PizzaStationMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, PizzaDelight.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<PizzaStationMenu>> PIZZA_STATION = MENU_TYPES.register("pizza_station", () -> IMenuTypeExtension.create(PizzaStationMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<PizzaMenu>> PIZZA = MENU_TYPES.register("pizza", () -> IMenuTypeExtension.create(PizzaMenu::new));
}