package com.tiviacz.pizzadelight.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;

public final class PizzaIngredients {
    public static final PizzaIngredients EMPTY = new PizzaIngredients(NonNullList.withSize(10, ItemStack.EMPTY));
    public static final Codec<PizzaIngredients> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, PizzaIngredients> STREAM_CODEC;
    private final NonNullList<ItemStack> items;
    private final int hashCode;

    private PizzaIngredients(NonNullList<ItemStack> items) {
        if(items.size() > 10) {
            throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 10");
        } else {
            this.items = items;
            this.hashCode = ItemStack.hashStackList(items);
        }
    }

    private PizzaIngredients() {
        this(NonNullList.withSize(10, ItemStack.EMPTY));
    }

    public PizzaIngredients(List<ItemStack> items) {
        this(NonNullList.withSize(10, ItemStack.EMPTY));

        for(int i = 0; i < Math.min(items.size(), 10); ++i) {
            this.items.set(i, items.get(i));
        }
    }

    public NonNullList<ItemStack> getIngredients() {
        return this.items;
    }

    public static PizzaIngredients fromHandler(ItemStackHandler handler) {
        PizzaIngredients ingredients = new PizzaIngredients();

        for(int i = 0; i < Math.min(handler.getSlots(), 10); i++) {
            ingredients.items.set(i, handler.getStackInSlot(i));
        }
        return ingredients;
    }

    private static PizzaIngredients fromSlots(List<PizzaIngredients.Slot> slots) {
        OptionalInt optionalint = slots.stream().mapToInt(PizzaIngredients.Slot::index).max();
        if(optionalint.isEmpty()) {
            return EMPTY;
        } else {
            PizzaIngredients ingredients = new PizzaIngredients();
            Iterator var3 = slots.iterator();

            while(var3.hasNext()) {
                PizzaIngredients.Slot itemcontainercontents$slot = (PizzaIngredients.Slot)var3.next();
                ingredients.items.set(itemcontainercontents$slot.index(), itemcontainercontents$slot.item());
            }

            return ingredients;
        }
    }

    private List<PizzaIngredients.Slot> asSlots() {
        List<PizzaIngredients.Slot> list = new ArrayList();

        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemstack = this.items.get(i);
            if(!itemstack.isEmpty()) {
                list.add(new PizzaIngredients.Slot(i, itemstack));
            }
        }

        return list;
    }

    public void copyInto(NonNullList<ItemStack> list) {
        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
            list.set(i, itemstack.copy());
        }
    }

    public ItemStack getSauceStack() {
        return this.items.get(9);
    }

    public boolean equals(Object other) {
        if(this == other) {
            return true;
        } else {
            if(other instanceof PizzaIngredients ingredients) {
                return ItemStack.listMatches(this.items, ingredients.items);
            }

            return false;
        }
    }

    public int hashCode() {
        return this.hashCode;
    }

    public int getSlots() {
        return this.items.size();
    }

    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.items.get(slot).copy();
    }

    private void validateSlotIndex(int slot) {
        if(slot < 0 || slot >= this.getSlots()) {
            throw new UnsupportedOperationException("Slot " + slot + " not in valid range - [0," + this.getSlots() + ")");
        }
    }

    static {
        CODEC = PizzaIngredients.Slot.CODEC.sizeLimitedListOf(10).xmap(PizzaIngredients::fromSlots, PizzaIngredients::asSlots);
        STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list(10)).map(PizzaIngredients::new, (p_331691_) -> p_331691_.items);
    }

    record Slot(int index, ItemStack item) {
        public static final Codec<PizzaIngredients.Slot> CODEC = RecordCodecBuilder.create((p_331695_) -> p_331695_.group(Codec.intRange(0, 9).fieldOf("slot").forGetter(Slot::index), ItemStack.CODEC.fieldOf("item").forGetter(Slot::item)).apply(p_331695_, Slot::new));

        public int index() {
            return this.index;
        }

        public ItemStack item() {
            return this.item;
        }
    }
}
