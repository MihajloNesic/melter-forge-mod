package com.oierbravo.melter.compat.crafttweaker;

import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.oierbravo.melter.content.melter.MeltingRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

@IRecipeHandler.For(MeltingRecipe.class)
public class MeltingRecipeHandler implements IRecipeHandler<MeltingRecipe>{

    @Override
    public String dumpToCommandString(IRecipeManager<? super MeltingRecipe> manager, RegistryAccess registryAccess, RecipeHolder<MeltingRecipe> holder) {
        MeltingRecipe recipe = holder.value();
        return manager.getCommandString() + recipe.toString() + recipe.getOutput() + "[" + recipe.getIngredient() + "]" + " ProcessingTime: " + recipe.getProcessingTime()  + " Minimum Heat: " + recipe.getHeatLevel();
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super MeltingRecipe> manager, MeltingRecipe firstRecipe, U secondRecipe) {
        return false;
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super MeltingRecipe> manager, RegistryAccess registryAccess, MeltingRecipe recipe) {
        return Optional.empty();
    }

    @Override
    public Optional<MeltingRecipe> recompose(IRecipeManager<? super MeltingRecipe> manager, RegistryAccess registryAccess, IDecomposedRecipe recipe) {
        return Optional.empty();
    }
}
