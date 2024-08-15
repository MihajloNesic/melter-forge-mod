package com.oierbravo.melter.network.packets.data;

import com.oierbravo.melter.Melter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ItemSyncPayload(
        ItemStack itemStack,
        BlockPos pos
    ) implements CustomPacketPayload {

    public static final Type<ItemSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Melter.MODID, "itemsync_payload"));

    @Override
    public Type<ItemSyncPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, ItemSyncPayload::itemStack,
            BlockPos.STREAM_CODEC, ItemSyncPayload::pos,
            ItemSyncPayload::new
    );
}
