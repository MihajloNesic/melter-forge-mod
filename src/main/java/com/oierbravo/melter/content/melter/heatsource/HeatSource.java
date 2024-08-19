package com.oierbravo.melter.content.melter.heatsource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class HeatSource {

    protected final int heatLevel;
    protected final Block source;
    protected final SourceType sourceType;

    public static final Codec<HeatSource> CODEC = RecordCodecBuilder.create(instance -> // Given an instance
            instance.group( // Define the fields within the instance
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("source").forGetter(HeatSource::getSource),
                    Codec.INT.fieldOf("heatLevel").forGetter(HeatSource::getHeatLevel),
                    StringRepresentable.fromEnum(SourceType::values).fieldOf("sourceType").forGetter(HeatSource::getSourceType)
            ).apply(instance, HeatSource::new) // Define how to create the object
    );

    public SourceType getSourceType() {
        return this.sourceType;
    }

    public HeatSource(Block pSource, int pHeatLevel, SourceType pSourceType){
        this.source = pSource;
        this.heatLevel = pHeatLevel;
        this.sourceType = pSourceType;
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
