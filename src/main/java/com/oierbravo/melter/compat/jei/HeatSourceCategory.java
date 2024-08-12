package com.oierbravo.melter.compat.jei;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.content.melter.HeatSources;
import com.oierbravo.melter.registrate.ModBlocks;
import com.simibubi.create.AllBlocks;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class HeatSourceCategory implements IRecipeCategory<HeatSourceCategory.Recipe> {

    public final static RecipeType<Recipe> TYPE = RecipeType.create("melter", "heatsource", Recipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotDrawable;

    public HeatSourceCategory(IGuiHelper guiHelper) {
        this.background = new IDrawable() {
            @Override
            public int getWidth() {
                return 176;
            }

            @Override
            public int getHeight() {
                return 25;
            }

            @Override
            public void draw( GuiGraphics graphics, int xOffset, int yOffset) {
            }
        };
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MELTER.get()));
        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("heatsource.recipe");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup iFocusGroup) {
        var input = builder.addSlot(RecipeIngredientRole.INPUT, 4, 5)
            .setBackground(slotDrawable, -1, -1);

        if (recipe.block != null) {
            input.addItemStack(recipe.block);
        }

        if (recipe.fluid != null) {
            input.addFluidStack(recipe.fluid.getFluid(), 1000L);
        }

        if (!recipe.description.isEmpty()) {
            input.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, Component.literal(recipe.description).withStyle(ChatFormatting.ITALIC)));
        }
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        if (recipe.block != null && recipe.block.is(ModBlocks.CREATIVE_HEAT_SOURCE_BLOCK.asItem())) {
            guiGraphics.drawString(minecraft.font, Component.translatable("melter.tooltip.heat_level").append(" ").append(Component.translatable("melter.tooltip.heat_level.creative")), 30, 9, 0xFF808080, false);
        }
        else guiGraphics.drawString(minecraft.font, Component.translatable("melter.tooltip.heat_level").append(Component.literal(" " + recipe.heat)), 30, 9, 0xFF808080, false);
    }

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        for (HeatSources.Config hs : HeatSources.getHeatSourcesConfig()) {
            Melter.LOGGER.info("processing heat source: " + hs.name());
            var rl = hs.rl();
            if (hs.type().equals(HeatSources.Type.BLOCK)) {
                var item = BuiltInRegistries.ITEM.get(rl);
                Block block = null;

                if (!item.equals(new ItemStack(Blocks.AIR).getItem())) {
                    if (item instanceof BlockItem blockItem) {
                        block = blockItem.getBlock();
                    }
                }
                else {
                    block = BuiltInRegistries.BLOCK.get(rl);
                }

                if (block == null || block.equals(Blocks.AIR)) {
                    continue;
                }

                var heat = HeatSources.getHeatSourceMap().getOrDefault(hs.name(), 0);
                if (heat > 0) {
                    ItemStack is = switch(rl.toString()) {
                        case "minecraft:fire" -> new ItemStack(Items.FLINT_AND_STEEL).setHoverName(Component.translatable("block.minecraft.fire").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                        case "minecraft:soul_fire" -> new ItemStack(Items.FIRE_CHARGE).setHoverName(Component.translatable("block.minecraft.soul_fire").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
                        case "minecraft:wall_torch" -> new ItemStack(Items.TORCH);
                        case "minecraft:soul_wall_torch" -> new ItemStack(Items.SOUL_TORCH);
                        case "create:lit_blaze_burner" -> Melter.withCreate ? new ItemStack(AllBlocks.BLAZE_BURNER) : new ItemStack(Blocks.AIR);
                        default -> new ItemStack(block);
                    };

                    if (is.is(Blocks.AIR.asItem())) {
                        continue;
                    }

                    boolean isItemStackPresent = recipes.stream()
                        .filter(r -> r.block != null)
                        .anyMatch(r -> r.block.is(is.getItem()) && r.description.equals(hs.description()));

                    if (!isItemStackPresent) {
                        recipes.add(new Recipe(is, null, heat, hs.description()));
                        Melter.LOGGER.info("added heat source block: " + hs.name());
                    }
                }
            }
            else if (hs.type().equals(HeatSources.Type.FLUID)) {
                var fluid = BuiltInRegistries.FLUID.get(rl);

                if (fluid.equals(Fluids.EMPTY)) {
                    continue;
                }

                var heat = HeatSources.getHeatSourceMap().getOrDefault(hs.name(), 0);
                if (heat > 0) {
                    FluidStack fs = new FluidStack(fluid, 1000);

                    boolean isFluidStackPresent = recipes.stream()
                        .filter(r -> r.fluid != null)
                        .anyMatch(r -> r.fluid.isFluidEqual(fs) && r.description.equals(hs.description()));

                    if (!isFluidStackPresent) {
                        recipes.add(new Recipe(null, fs, heat, hs.description()));
                        Melter.LOGGER.info("added heat source fluid: " + hs.name());
                    }
                }
            }
        }

        // creative
        recipes.add(new Recipe(new ItemStack(ModBlocks.CREATIVE_HEAT_SOURCE_BLOCK), null, Integer.MAX_VALUE, ""));

        // Sort by heat ascending
        recipes.sort(Comparator.comparingInt(Recipe::heat));

        Melter.LOGGER.info("Added '{}' heat sources to JEI", recipes.size());

        return recipes;
    }

    public record Recipe(@Nullable ItemStack block, @Nullable FluidStack fluid, int heat, String description) {
    }
}
