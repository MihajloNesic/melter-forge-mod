package com.oierbravo.melter.content.melter;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("creative_heat_source.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

}
