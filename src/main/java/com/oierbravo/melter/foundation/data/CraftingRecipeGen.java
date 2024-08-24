package com.oierbravo.melter.foundation.data;


import com.oierbravo.melter.registrate.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class CraftingRecipeGen extends RecipeProvider {

    public CraftingRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MELTER.get())
                .define('N',  Tags.Items.NUGGETS_IRON)
                .define('S', Tags.Items.INGOTS_IRON)
                .pattern("S S")
                .pattern("S S")
                .pattern("NSN")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(output);
    }

    @Override
    public final String getName() {
        return "Melters's crafting recipes.";
    }
}
