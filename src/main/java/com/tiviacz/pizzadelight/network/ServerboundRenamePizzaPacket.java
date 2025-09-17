package com.tiviacz.pizzadelight.network;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.container.PizzaStationMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundRenamePizzaPacket(String name) implements CustomPacketPayload {
    public static final Type<ServerboundRenamePizzaPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(PizzaDelight.MODID, "rename_pizza"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRenamePizzaPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ServerboundRenamePizzaPacket::name,
            ServerboundRenamePizzaPacket::new
    );

    public static void handle(final ServerboundRenamePizzaPacket message, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();


            AbstractContainerMenu menu = player.containerMenu;
            if(menu instanceof PizzaStationMenu stationMenu) {
                if(!stationMenu.stillValid(player)) {
                    PizzaDelight.LOGGER.debug("Player {} interacted with invalid menu {}", player, stationMenu);
                    return;
                }

                stationMenu.setItemName(message.name());
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}