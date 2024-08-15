package com.oierbravo.melter.compat.jei;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.content.melter.MeltingRecipe;
import com.oierbravo.melter.registrate.ModBlocks;
import com.oierbravo.melter.registrate.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Melter.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MeltingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new HeatSourceCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MELTER.get()), new RecipeType<>(MeltingRecipeCategory.UID, MeltingRecipe.class));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MELTER.get()), HeatSourceCategory.TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<MeltingRecipe> meltingRecipes = rm.getAllRecipesFor(ModRecipes.MELTING_TYPE.get()).stream().map(meltingRecipeRecipeHolder -> meltingRecipeRecipeHolder.value()).toList();
        registration.addRecipes(MeltingRecipeCategory.TYPE, meltingRecipes);
        registration.addRecipes(HeatSourceCategory.TYPE, HeatSourceCategory.getRecipes());

        registration.addIngredientInfo(new ItemStack(ModBlocks.CREATIVE_HEAT_SOURCE_BLOCK), VanillaTypes.ITEM_STACK, Component.translatable("creative_heat_source.info"));
    }
}
