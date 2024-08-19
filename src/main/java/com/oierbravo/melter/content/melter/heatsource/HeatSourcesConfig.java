package com.oierbravo.melter.content.melter.heatsource;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;

public class HeatSourcesConfig {
    public static ModConfigSpec.IntValue MAX_HEAT_SOURCE_LEVEL;
    public static ModConfigSpec.BooleanValue HEAT_SOURCES_FROM_CONFIG;

    public static ModConfigSpec.ConfigValue<List<? extends List<? extends String>>> HEAT_SOURCES;

    public static void registerServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Settings for heat sources").push("heat_sources");

        HEAT_SOURCES_FROM_CONFIG = builder
                .comment("Read heat sources from config")
                .define("fromConfig", true);

        HEAT_SOURCES = builder
                .comment("List of heat source blocks or fluids. Each element in a list must follow the order: type (block, fluid), name, heat level (1-10), additional information shown in JEI")
                .defineList("heatSources", Arrays.asList(
                    Arrays.asList("block", "minecraft:torch", "1", ""),
                    Arrays.asList("block", "minecraft:soul_torch", "1", ""),
                    Arrays.asList("block", "minecraft:wall_torch", "1", ""),
                    Arrays.asList("block", "minecraft:soul_wall_torch", "1", ""),
                    Arrays.asList("block", "minecraft:fire", "2", ""),
                    Arrays.asList("block", "minecraft:soul_fire", "2", ""),
                    Arrays.asList("block", "minecraft:campfire", "2", ""),
                    Arrays.asList("block", "minecraft:soul_campfire", "2", ""),
                    Arrays.asList("block", "minecraft:magma_block", "3", ""),
                    Arrays.asList("fluid", "minecraft:lava", "4", ""),
                    Arrays.asList("block", "create:lit_blaze_burner", "2", "Lit"),
                    Arrays.asList("block", "create:blaze_burner/fading", "3", "Heated"),
                    Arrays.asList("block", "create:blaze_burner/kindled", "3", "Heated"),
                    Arrays.asList("block", "create:blaze_burner/seething", "5", "Super-Heated")
                ), entry -> true);
        builder.pop();
    }
}
