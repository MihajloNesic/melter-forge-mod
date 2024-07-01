package com.oierbravo.melter.content.melter;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class CreativeHeatSourceBlock extends Block {

    public CreativeHeatSourceBlock(Properties properties) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_MAGENTA)
                .strength(0.6f)
        );
    }
}
