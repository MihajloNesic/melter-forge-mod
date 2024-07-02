package com.oierbravo.melter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.oierbravo.melter.registrate.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("melter")
public class Melter
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "melter";
    public static final String DISPLAY_NAME = "Melter";

    public static IEventBus modEventBus;

    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(MODID));

    public static final boolean withCreate = ModList.get().isLoaded("create");


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    public Melter()
    {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModBlocks.register();
        ModBlockEntities.register();
        ModCreativeTabs.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMessages.register();
        Config.register();

        registrate().addRawLang("itemGroup.melter:main", "Melter");
        registrate().addRawLang("melter.block.display", "Melter");
        registrate().addRawLang("melting.recipe", "Melting");
        registrate().addRawLang("creative_heat_source.tooltip", "Infinite heat level for the Melter");
        registrate().addRawLang("melter.tooltip.progress", "Progress: %d%%");
        registrate().addRawLang("melter.tooltip.heat_level", "Heat: %s §6(%d§6)");
        registrate().addRawLang("melter.tooltip.heat_level.creative", "Heat: %s (infinite)");
        registrate().addRawLang("melter.tooltip.heat_level.none", "§cNot heated!");
        registrate().addRawLang("melter.tooltip.no_source_found", "No valid heat source found");
        registrate().addRawLang("config.jade.plugin_melter.melter_data", "Melter data");
        registrate().addRawLang("jei.melting.recipe.minimum_heat", "Minimum heat: %d");

    }


    public static Registrate registrate() {
        return REGISTRATE.get();
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
