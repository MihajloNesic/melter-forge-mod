package com.oierbravo.melter.content.melter;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MelterConfig {
    public static ModConfigSpec.IntValue MELTER_CAPACITY;

    public static void registerServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("Settings for the melter").push("melter");
        MELTER_CAPACITY = builder
                .comment("How much liquid fits into the melter, in mB")
                .defineInRange("capacity", 1000, 1, Integer.MAX_VALUE);

        builder.pop();
    }
}
