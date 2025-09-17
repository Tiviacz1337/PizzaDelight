package com.tiviacz.pizzadelight.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PizzaDelightConfig {
    public static class Server {
        public final ModConfigSpec.BooleanValue allowOnlyRecommendedIngredients;

        Server(final ModConfigSpec.Builder builder) {
            builder.comment("Server config settings")
                    .push("server");

            allowOnlyRecommendedIngredients = builder.comment("Only items from ingredients tag are allowed to be put on pizza")
                    .define("allowOnlyRecommendedIngredients", false);

            builder.pop();
        }
    }

    //SERVER
    public static final ModConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }
}