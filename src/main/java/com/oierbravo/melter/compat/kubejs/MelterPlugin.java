package com.oierbravo.melter.compat.kubejs;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.content.melter.MeltingRecipe;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

public class MelterPlugin implements KubeJSPlugin {
    //public static EventGroup GROUP = EventGroup.of("MelterEvents");
    //public static EventHandler HEAT_SOURCE = GROUP.startup("registerHeatSource", () -> HeatSourceEvent.class);
    //public static EventHandler HEAT_SOURCE_SERVER = GROUP.server("registerHeatSource", () -> HeatSourceEvent.class);

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace(Melter.MODID).register(MeltingRecipe.ID, MeltingRecipeSchema.SCHEMA);
    }

    /*@Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
        registry.register(HeatSourcesRegistry.HEAT_SOURCE_REGISTRY_KEY, HeatSource.CODEC, HeatSource.class);
    }

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.addDefault(HeatSourcesRegistry.HEAT_SOURCE_REGISTRY_KEY, HeatSourceBuilderKube.class, HeatSourceBuilderKube::new);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(GROUP);
    }*/



}

