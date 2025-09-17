package com.tiviacz.pizzadelight.blockentity.content;

import com.tiviacz.pizzadelight.PizzaDelight;

public record BasinContent(String name, BasinContentType contentType) {
    public static final BasinContent AIR = BasinContentRegistry.REGISTRY.register(new BasinContent("air", BasinContentType.EMPTY));
    public static final BasinContent MILK = BasinContentRegistry.REGISTRY.register(new BasinContent("milk", BasinContentType.MILK));
    public static final BasinContent FERMENTING_MILK = BasinContentRegistry.REGISTRY.register(new BasinContent("fermenting_milk", BasinContentType.FERMENTING_MILK));
    public static final BasinContent CHEESE = BasinContentRegistry.REGISTRY.register(new BasinContent("cheese", BasinContentType.CHEESE));

    public static void register() {
    }

    public boolean isEmpty() {
        return this == AIR;
    }

    public BasinContentType getContentType() {
        return this.contentType;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getTranslationKey() {
        return PizzaDelight.MODID + "." + this.name;
    }
}
