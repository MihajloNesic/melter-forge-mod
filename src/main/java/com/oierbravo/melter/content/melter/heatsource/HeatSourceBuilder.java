package com.oierbravo.melter.content.melter.heatsource;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class HeatSourceBuilder {
    protected final ResourceKey<HeatSource> key;
    protected Block source;
    protected HeatSource.SourceType sourceType;
    protected int heatLevel;

    public HeatSourceBuilder(ResourceKey<HeatSource> pKey) {
        this.key = pKey;
        this.source = Blocks.AIR;
        this.sourceType = HeatSource.SourceType.BLOCK;
        this.heatLevel = 0;
    }

    public HeatSourceBuilder source(Block pSource){
        this.source = pSource;
        return this;
    }
    public HeatSourceBuilder heatLevel(int pHeatLevel){
        this.heatLevel = pHeatLevel;
        return this;
    }
    public HeatSourceBuilder sourceType(HeatSource.SourceType pSourceType){
        this.sourceType = pSourceType;
        return this;
    }

    public HeatSource build() {
        return new HeatSource(source, heatLevel, sourceType);
    }

    public HeatSource register(BootstrapContext<HeatSource> ctx) {
        HeatSource type = build();
        ctx.register(key, type);
        return type;
    }
}
