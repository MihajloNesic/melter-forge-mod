package com.oierbravo.melter.content.melter.heatsource;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.registrate.ModHeatSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class HeatSourceProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(HeatSourcesRegistry.HEAT_SOURCE_REGISTRY_KEY, ModHeatSources::bootstrap);

    //public GeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    //    super(output, registries, BUILDER, Set.of(Create.ID));
    //}
    public HeatSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
        super(output, registries, datapackEntriesBuilder, Set.of(Melter.MODID));
    }
}
