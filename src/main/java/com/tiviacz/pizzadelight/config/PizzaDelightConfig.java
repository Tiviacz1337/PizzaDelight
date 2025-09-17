package com.tiviacz.pizzadelight.config;

import com.tiviacz.pizzadelight.PizzaDelight;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = PizzaDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PizzaDelightConfig {
    public static class Server {
        public final ForgeConfigSpec.BooleanValue allowOnlyRecommendedIngredients;

        Server(final ForgeConfigSpec.Builder builder) {
            builder.comment("Server config settings")
                    .push("server");

            allowOnlyRecommendedIngredients = builder.comment("Only items from ingredients tag are allowed to be put on pizza")
                    .define("allowOnlyRecommendedIngredients", false);

            builder.pop();
        }
    }

    //SERVER
    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    //REGISTRY
    public static void register(final ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.SERVER, serverSpec);
    }
}