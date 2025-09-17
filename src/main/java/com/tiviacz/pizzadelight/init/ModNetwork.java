package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.PizzaDelight;
import com.tiviacz.pizzadelight.network.ServerboundRenamePizzaPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(PizzaDelight.MODID, "network");
    public static final String NETWORK_VERSION = new ResourceLocation(PizzaDelight.MODID, "1").toString();

    public static SimpleChannel registerNetworkChannel() {
        final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .networkProtocolVersion(() -> NETWORK_VERSION)
                .simpleChannel();

        PizzaDelight.NETWORK = channel;

        channel.messageBuilder(ServerboundRenamePizzaPacket.class, 0)
                .decoder(ServerboundRenamePizzaPacket::decode)
                .encoder(ServerboundRenamePizzaPacket::encode)
                .consumerMainThread(ServerboundRenamePizzaPacket::handle)
                .add();

        return channel;
    }
}