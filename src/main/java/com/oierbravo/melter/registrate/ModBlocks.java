package com.oierbravo.melter.registrate;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.content.melter.CreativeHeatSourceBlock;
import com.oierbravo.melter.content.melter.MelterBlock;
import com.oierbravo.melter.content.melter.MelterBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class ModBlocks {
    public static final BlockEntry<MelterBlock> MELTER = Melter.registrate()
            .block("melter", MelterBlock::new)
            .lang("Melter")
            .blockstate((ctx, prov) ->
                    prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                        int heatLevel = state.getValue(MelterBlock.HEAT_SOURCE).getHeatLevel();
                        String suffix = heatLevel > 0 ? "_heat" + heatLevel : "";
                        return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(ResourceLocation.tryParse("melter:block/melter" + suffix))).build();
                    })
           )
            .simpleItem()
            .blockEntity(MelterBlockEntity::new)
            .build()
            .register();

    public static final BlockEntry<CreativeHeatSourceBlock> CREATIVE_HEAT_SOURCE_BLOCK = Melter.registrate()
            .block("creative_heat_source", CreativeHeatSourceBlock::new)
            .lang("Creative Heat Source")
            .simpleItem()
            .register();


    public static void register() {

    }
}
