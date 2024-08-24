package com.oierbravo.melter.content.melter.heatsource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class HeatSource {

    protected final int heatLevel;
    protected final Block source;
    protected final SourceType sourceType;
    protected List<ICondition> conditions = List.of();

    public static final Codec<HeatSource> CODEC = RecordCodecBuilder.create(instance -> // Given an instance
            instance.group( // Define the fields within the instance
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("source").forGetter(HeatSource::getSource),
                    Codec.INT.fieldOf("heatLevel").forGetter(HeatSource::getHeatLevel),
                    StringRepresentable.fromEnum(SourceType::values).fieldOf("sourceType").forGetter(HeatSource::getSourceType),
                    ICondition.LIST_CODEC.optionalFieldOf(ConditionalOps.DEFAULT_CONDITIONS_KEY).forGetter(HeatSource::getConditions)
            ).apply(instance, HeatSource::new) // Define how to create the object
    );


    private Optional<List<ICondition>> getConditions() {
        if(conditions.isEmpty())
            return Optional.empty();
        return Optional.of(conditions);
    }


    public static final Codec<Optional<WithConditions<HeatSource>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(CODEC);

    public SourceType getSourceType() {
        return this.sourceType;
    }

    public HeatSource(Block pSource, int pHeatLevel, SourceType pSourceType){
        this(pSource, pHeatLevel, pSourceType, Optional.empty());
    }
    public HeatSource(Block pSource, int pHeatLevel, SourceType pSourceType, Optional<List<ICondition>> conditions){
        this.source = pSource;
        this.heatLevel = pHeatLevel;
        this.sourceType = pSourceType;
        conditions.ifPresent(iConditions -> this.conditions = iConditions);
    }

    public int getHeatLevel() {
        return this.heatLevel;
    }

    public Block getSource() {
        return this.source;
    }
    public ItemStack getItemStackSource(){
        ResourceLocation blockResourceLocation = BuiltInRegistries.BLOCK.getKey(source);

        //"minecraft:soul_fire" -> generateItemStackWithCustomItemName(new ItemStack(Items.FIRE_CHARGE),Component.translatable("block.minecraft.soul_fire").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
        return switch(blockResourceLocation.toString()) {
            case "minecraft:fire" -> HeatSources.generateItemStackWithCustomItemName(new ItemStack(Items.FLINT_AND_STEEL), Component.translatable("block.minecraft.fire").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            case "minecraft:soul_fire" -> HeatSources.generateItemStackWithCustomItemName(new ItemStack(Items.FIRE_CHARGE),Component.translatable("block.minecraft.soul_fire").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD));
            //case "create:lit_blaze_burner" -> Melter.withCreate ? new ItemStack(AllBlocks.BLAZE_BURNER) : new ItemStack(Blocks.AIR);
            default -> new ItemStack(BuiltInRegistries.ITEM.get(blockResourceLocation));
        };
    }
    public Fluid getFluidSource() {
        ResourceLocation blockResourceLocation = BuiltInRegistries.BLOCK.getKey(source);
        return BuiltInRegistries.FLUID.get(blockResourceLocation);
    }
    public FluidStack getFluidStackSource() {
        ResourceLocation blockResourceLocation = BuiltInRegistries.BLOCK.getKey(source);
        return new FluidStack(BuiltInRegistries.FLUID.get(blockResourceLocation),1000);
    }

    public boolean isCreative() {
        if(sourceType == SourceType.CREATIVE)
            return true;
        return false;
    }

    public enum SourceType implements StringRepresentable  {
        BLOCK, FLUID, CREATIVE;


        @Override
        public @NotNull String getSerializedName() {
            return this.name();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
