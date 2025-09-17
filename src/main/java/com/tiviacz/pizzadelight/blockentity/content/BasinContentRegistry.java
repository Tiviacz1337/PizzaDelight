package com.tiviacz.pizzadelight.blockentity.content;

import com.google.common.collect.Maps;

import java.util.Map;

public class BasinContentRegistry {
    public static Map<String, BasinContent> contentsRegistry = Maps.newHashMap();

    public static BasinContent register(BasinContent content) {
        contentsRegistry.putIfAbsent(content.name(), content);

        return contentsRegistry.get(content.name());
    }

    public static BasinContent fromString(String name) {
        if(contentsRegistry.containsKey(name)) {
            return contentsRegistry.get(name);
        }
        throw new IllegalStateException("Content does not exist in registry!");
    }
}