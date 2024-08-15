package com.oierbravo.melter.compat.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.FluidStackComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public interface MeltingRecipeSchema {
    RecipeKey<Ingredient> INPUT = IngredientComponent.INGREDIENT.key("input", ComponentRole.INPUT);
    RecipeKey<FluidStack> RESULT = FluidStackComponent.FLUID_STACK.key("output", ComponentRole.OUTPUT);
    RecipeKey<Integer> PROCESSING_TIME = NumberComponent.INT.key("processingTime",ComponentRole.OTHER).optional(100);
    RecipeKey<Integer> MINIMUM_HEAT = NumberComponent.INT.key("minimumHeat",ComponentRole.OTHER).optional(1);

    RecipeSchema SCHEMA = new RecipeSchema( RESULT, INPUT, PROCESSING_TIME, MINIMUM_HEAT);

}
