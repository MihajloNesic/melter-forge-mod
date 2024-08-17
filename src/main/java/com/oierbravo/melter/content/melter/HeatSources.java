package com.oierbravo.melter.content.melter;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.registrate.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;

public class HeatSources {

    public static int fromLevel(Level level, BlockPos below) {
        BlockState belowBlockState = level.getBlockState(below);
        return HeatSources.getHeatSource(belowBlockState);
    }

    public static boolean isCreative(Level level, BlockPos below) {
        Block belowBlock = level.getBlockState(below).getBlock();
        return belowBlock.equals(ModBlocks.CREATIVE_HEAT_SOURCE_BLOCK.get());
    }

    public static boolean isHeatSource(BlockState blockState) {
        int heatLevel = getHeatSource(blockState);
        return heatLevel > 0;
    }

    public static int getHeatSource(BlockState state) {
        // creative
        if (state.getBlock().equals(ModBlocks.CREATIVE_HEAT_SOURCE_BLOCK.get())) {
            return 10;
        }

        Block block = state.getBlock();
        String blockName = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();

        // campfire state
        if (state.hasProperty(CampfireBlock.LIT)) {
            boolean isLit = state.getValue(CampfireBlock.LIT);
            if (!isLit) {
                return 0;
            }
        }

        // create
        if (Melter.withCreate) {
            // blaze burner

            /*if (state.hasProperty(BlazeBurnerBlock.HEAT_LEVEL)) {
                BlazeBurnerBlock.HeatLevel heatLevel = state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
                // can't have a second colon here, see ResourceLocation#assertValidNamespace
                blockName += "/" + heatLevel.getSerializedName();
            }*/
        }

        // fluid
        int liquidLevel = 0;
        if (block instanceof LiquidBlock liquidBlock) {
            ResourceLocation fluidRL = BuiltInRegistries.FLUID.getKey(liquidBlock.fluid.getSource());
            blockName = fluidRL.toString();

            if (!state.getFluidState().isSource()) {
                liquidLevel = state.getValue(LiquidBlock.LEVEL);
            }
        }
        float liquidLevelDecay = liquidLevel <= 0 ? 1 : liquidLevel + 1.2f; // 1.2 is some small delta for the 'decay'

        int heatLevel = getHeatSourceMap().getOrDefault(blockName, 0);

        // subtract fluid level decay; if liquidLevel is 0, heatLevel is returned
        heatLevel = (int)((float)heatLevel / liquidLevelDecay);

        return heatLevel;
    }

    public static List<Config> getHeatSourcesConfig() {
        return MelterConfig.HEAT_SOURCES.get()
            .stream()
            .map(s -> {
                try {
                    return new Config(Type.valueOf(s.get(0).toUpperCase()), s.get(1), Integer.valueOf(s.get(2)), s.get(3));
                }
                catch (Exception e) {
                    Melter.LOGGER.error("Failed to load heat source: {}", s);
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();
    }

    public static Map<String, Integer> getHeatSourceMap() {
        Map<String, Integer> map = new HashMap<>();
        MelterConfig.HEAT_SOURCES.get()
            .forEach(s -> {
                if (!map.containsKey(s.get(1))) {
                    map.put(s.get(1), Integer.valueOf(s.get(2)));
                }
            });
        return map;
    }

    /**
     * There isn't a good way to display both FluidStacks and ItemStacks in a JEI recipe.
     * Here, we are getting every valid heat source for a specific level.
     * A map is returned with a 'type' as a key and a list of 'anything', but really either a FluidStack or an ItemStack.
     * There are then passed to MeltingRecipeCategory to display the textures.
     *
     * If no valid heat source is present, we are adding a Barrier block with a custom text.
     */
    public static Map<Type, List> getHeatSourcesForHeatLevel(int heatLevel) {
        Map<Type, List> stackMap = new HashMap<>(); // I know...
        Arrays.stream(Type.values()).forEach(type -> stackMap.put(type, new ArrayList<>()));

        if (heatLevel > 20) {
            stackMap.put(Type.BLOCK, List.of(generateItemStackWithCustomItemName(new ItemStack(Blocks.BARRIER),Component.translatable("melter.tooltip.no_source_found"))));
            return stackMap;
        }

        MelterConfig.HEAT_SOURCES.get().stream()
            .filter(s -> Integer.valueOf(s.get(2)).equals(heatLevel))
            .map(e -> new Config(Type.valueOf(e.get(0).toUpperCase()), e.get(1), Integer.valueOf(e.get(2)), e.get(3)))
            .filter(e -> {
                try {
                    e.rl();
                    return true;
                }
                catch (Exception ex) {
                    Melter.LOGGER.error("Can't process name '{}'", e.name);
                    return false;
                }
            })
            .forEach(e -> {
                var rl = e.rl();
                if (e.type.equals(Type.BLOCK)) {
                    // Fire and Soul Fire don't really have a "Block" we can use to texture
                    ItemStack is = switch(rl.toString()) {
                        case "minecraft:fire" -> generateItemStackWithCustomItemName(new ItemStack(Items.FLINT_AND_STEEL),Component.translatable("block.minecraft.fire").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                        case "minecraft:soul_fire" -> generateItemStackWithCustomItemName(new ItemStack(Items.FIRE_CHARGE),Component.translatable("block.minecraft.soul_fire").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
                        //case "create:lit_blaze_burner" -> Melter.withCreate ? new ItemStack(AllBlocks.BLAZE_BURNER) : new ItemStack(Blocks.AIR);
                        default -> new ItemStack(BuiltInRegistries.ITEM.get(rl));
                    };

                    boolean isItemStackPresent = stackMap.get(Type.BLOCK).stream()
                        .anyMatch(stack -> ((ItemStack) stack).is(is.getItem()));

                    if (!isItemStackPresent && !is.getItem().equals(new ItemStack(Blocks.AIR).getItem())) {
                        if (!e.description.isEmpty()) {
                            stackMap.get(Type.BLOCK).add(generateItemStackWithCustomItemName(is,
                                    is.getHoverName().copy().append(Component.literal(" (" + e.description + ")"))));
                        }
                        else {
                            stackMap.get(Type.BLOCK).add(is);
                        }
                    }
                }

                if (e.type.equals(Type.FLUID)) {
                    FluidStack fs = new FluidStack(BuiltInRegistries.FLUID.get(rl), 1000);

                    boolean isFluidStackPresent = stackMap.get(Type.FLUID).stream()
                        .anyMatch(stack -> ((FluidStack) stack).is(fs.getFluidType()));

                    if (!isFluidStackPresent) {
                        stackMap.get(Type.FLUID).add(fs);
                    }
                }
            });

        Boolean isMapEmpty = stackMap.values().stream()
            .map(List::isEmpty)
            .reduce(Boolean::logicalAnd)
            .orElse(true);

        if (isMapEmpty) {
            stackMap.put(Type.BLOCK, List.of(generateItemStackWithCustomItemName(new ItemStack(Blocks.BARRIER),Component.translatable("melter.tooltip.no_source_found"))));
            return getHeatSourcesForHeatLevel(heatLevel + 1);
        }

        return stackMap;
    }
    public static ItemStack generateItemStackWithCustomItemName(ItemStack itemStack, MutableComponent component){
        itemStack.set(DataComponents.ITEM_NAME,component);
        return itemStack;
    }
    @Override
    public String toString() {
        return super.toString();
    }

    public record Config(Type type, String name, Integer level, String description) {
        public ResourceLocation rl() {
            // we split by '/' and get the first part, because we don't want the state
            return ResourceLocation.parse(name.split("/")[0]);
        }
    }

    public enum Type {
        BLOCK, FLUID
    }
}
