package com.tiviacz.pizzadelight.init;

import com.tiviacz.pizzadelight.network.ServerboundRenamePizzaPacket;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {
    public static void register(final PayloadRegistrar registrar) {
        //Server
        registrar.playToServer(ServerboundRenamePizzaPacket.TYPE, ServerboundRenamePizzaPacket.STREAM_CODEC, ServerboundRenamePizzaPacket::handle);
    }
}