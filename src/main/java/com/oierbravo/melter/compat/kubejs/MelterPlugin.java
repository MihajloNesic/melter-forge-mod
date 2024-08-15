package com.oierbravo.melter.compat.kubejs;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.content.melter.MeltingRecipe;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;

public class MelterPlugin implements KubeJSPlugin {

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace(Melter.MODID).register(MeltingRecipe.ID, MeltingRecipeSchema.SCHEMA);
    }
}