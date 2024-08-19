package com.oierbravo.melter.content.melter.heatsource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
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

    public enum SourceType implements StringRepresentable  {
        BLOCK, FLUID;


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
