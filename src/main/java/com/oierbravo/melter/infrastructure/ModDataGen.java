package com.oierbravo.melter.infrastructure;

import com.oierbravo.melter.foundation.data.CraftingRecipeGen;
import com.oierbravo.melter.foundation.data.MeltingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ModDataGen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(
                event.includeServer(),
                new CraftingRecipeGen(output, lookupProvider)
        );
        generator.addProvider(
                event.includeServer(),
                new MeltingRecipeGen(output, lookupProvider)
        );
    }
}
