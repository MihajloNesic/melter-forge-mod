package com.oierbravo.melter.content.melter;

import com.oierbravo.melter.Melter;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.LitBlazeBurnerBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public enum HeatSources implements StringRepresentable {
    NONE(0,  "", "", "None"),

    // level 1
    TORCH(1, "Block{minecraft:torch}", "minecraft:torch", "Torch"),
    SOUL_TORCH(1, "Block{minecraft:soul_torch}", "minecraft:soul_torch", "Soul Torch"),
    WALL_TORCH(1,"Block{minecraft:wall_torch}", "minecraft:wall_torch", "Torch"),
    SOUL_WALL_TORCH(1,"Block{minecraft:soul_wall_torch}", "minecraft:soul_wall_torch", "Soul Torch"),

    // level 2
    FIRE(2,"Block{minecraft:fire}", "minecraft:flint_and_steel", "Fire"),
    SOUL_FIRE(2,"Block{minecraft:soul_fire}", "minecraft:fire_charge", "Soul Fire"),
    CAMPFIRE(2,"Block{minecraft:campfire}", "minecraft:campfire", "Campfire"),
    SOUL_CAMPFIRE(2,"Block{minecraft:soul_campfire}", "minecraft:soul_campfire", "Soul Campfire"),

    // level 3
    MAGMA_BLOCK(3,"Block{minecraft:magma_block}", "minecraft:magma_block", "Magma Block"),

    // level 4
    LAVA(4, Type.FLUID, "Block{minecraft:lava}", "minecraft:lava", "Lava"),

    // Creative
    OVER_9000(9001, "Block{melter:creative_heat_source}", "", "Over 9000!"),

    // Create
    LIT_BLAZE_BURNER(2,"create:blocks/lit_blaze_burner", "create:empty_blaze_burner", "Lit Blaze Burner"),
    //BLAZE_BURNER_INACTIVE(0,"create:blocks/blaze_burner:smouldering", "create:blaze_burner", "Blaze Burner"),
    BLAZE_BURNER_FADING(3,"create:blocks/blaze_burner:fading", "create:blaze_burner","Blaze Burner"),
    BLAZE_BURNER_ACTIVE(3,"create:blocks/blaze_burner:kindled", "create:blaze_burner","Blaze Burner"),
    BLAZE_BURNER_SUPERHEATED(5,"create:blocks/blaze_burner:seething", "create:blaze_burner","SUPERHEATED!");

    private final int heatLevel;
    private final Type type;
    private final String resourceName;
    private final String textureName;
    private final String displayName;

    HeatSources(int heatLevel, Type type, String resourceName, String textureName, String displayName) {
        this.heatLevel = heatLevel;
        this.type = type;
        this.resourceName = resourceName;
        this.textureName = textureName;
        this.displayName = displayName;
    }

    HeatSources(int heatLevel, String resourceName, String textureName, String displayName) {
        this(heatLevel, Type.BLOCK, resourceName, textureName, displayName);
    }

    public static HeatSources fromLevel(Level level, BlockPos below) {
        BlockState belowBlockState = level.getBlockState(below);
        return HeatSources.get(belowBlockState);
    }

    public int getHeatLevel() {
        return heatLevel;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getTextureName() {
        return textureName;
    }

    public Type getType() {
        return type;
    }

    public static HeatSources get(int minimumHeat){
        return Arrays.stream(HeatSources.values())
                .filter(e -> e.getHeatLevel() > minimumHeat)
                .findFirst()
                .orElse(HeatSources.NONE);
    }
    public static HeatSources get(String resourceName){
        return Arrays.stream(HeatSources.values())
                .filter(e -> e.resourceName.equals(resourceName))
                .findFirst()
                .orElse(HeatSources.NONE);
    }
    public static HeatSources get(BlockState blockState){
        String nameString = blockState.getBlock().getLootTable().toString();
        String blockString = blockState.getBlock().toString();

        if(blockString.equals(HeatSources.CAMPFIRE.getResourceName()) || blockString.equals(HeatSources.SOUL_CAMPFIRE.getResourceName())){
            if(!blockState.getValue(BlockStateProperties.LIT)){
                return HeatSources.NONE;
            }
        }

        if (Melter.withCreate) {
            if (blockState.hasProperty(BlazeBurnerBlock.HEAT_LEVEL)) {
                BlazeBurnerBlock.HeatLevel heatLevel = blockState.getValue(BlazeBurnerBlock.HEAT_LEVEL);
                nameString += ":" + heatLevel.getSerializedName();
                return get(nameString);
            }

            if (blockState.hasProperty(LitBlazeBurnerBlock.FLAME_TYPE)) {
                return HeatSources.LIT_BLAZE_BURNER;
            }
        }

        return get(blockString);
    }
    public static boolean isHeatSource(BlockState blockState){
        HeatSources source = HeatSources.get(blockState);
        if(source != HeatSources.NONE){
            return true;
        }
        return false;
    }

    /**
     * There isn't a good way to display both FluidStacks and ItemStacks in a JEI recipe.
     * Here, we are getting every valid heat source for a specific level.
     * A map is returned with a 'type' as a key and a list of 'anything', but really either a FluidStack or an ItemStack.
     * There are then passed to MeltingRecipeCategory to display the textures.
     *
     * Shame we can't display fire, since it is not a block obtainable in Minecraft (like it use to).
     *
     * If no valid heat source is present, we are adding a Barrier block with a custom text.
     */
    public static Map<Type, List> getStacksForHeatLevel(int heatLevel) {
        Map<Type, List> stackMap = new HashMap<>(); // I know...
        Arrays.stream(Type.values()).forEach(type -> stackMap.put(type, new ArrayList<>()));

        Arrays.stream(HeatSources.values())
                .filter(source -> source.getHeatLevel() == heatLevel)
                .filter(source -> !source.equals(OVER_9000))
                .forEach(source -> {
                    String texture = source.getTextureName();
                    ResourceLocation resourceLocation = ResourceLocation.tryParse(texture);
                    if (source.getType().equals(Type.FLUID)) {
                        FluidStack fs = new FluidStack(ForgeRegistries.FLUIDS.getValue(resourceLocation), 1000);
                        if (!stackMap.get(Type.FLUID).contains(fs)) {
                            stackMap.get(Type.FLUID).add(fs);
                        }
                    }
                    else if (source.getType().equals(Type.BLOCK)) {
                        // Fire and Soul Fire don't really have a "Block" we can use to texture
                        ItemStack is = switch(source) {
                            case FIRE -> new ItemStack(Items.FLINT_AND_STEEL).setHoverName(Component.translatable("block.minecraft.fire").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                            case SOUL_FIRE -> new ItemStack(Items.FIRE_CHARGE).setHoverName(Component.translatable("block.minecraft.soul_fire").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
                            default -> new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
                        };
                        if (!stackMap.get(Type.BLOCK).contains(is) && !is.getItem().equals(new ItemStack(Blocks.AIR).getItem())) {
                            stackMap.get(Type.BLOCK).add(is);
                        }
                    }
                });

        Boolean isMapEmpty = stackMap.values().stream()
                .map(List::isEmpty)
                .reduce(Boolean::logicalAnd)
                .orElse(true);

        if (isMapEmpty) {
            stackMap.put(Type.BLOCK, List.of(new ItemStack(Blocks.BARRIER).setHoverName(Component.translatable("melter.tooltip.no_source_found"))));
        }

        return stackMap;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getDisplayName() {
        return displayName;
    }

    public enum Type {
        BLOCK, FLUID
    }
}
