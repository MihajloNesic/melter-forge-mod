package com.oierbravo.melter.network.packets.handler;

import com.oierbravo.melter.content.melter.MelterBlockEntity;
import com.oierbravo.melter.network.packets.data.ItemSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ItemSyncPacket {
    public static final ItemSyncPacket INSTANCE = new ItemSyncPacket();

    public static ItemSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final ItemSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof MelterBlockEntity controller) {
                    controller.setItemStack(payload.itemStack());
                }
            }
        });
    }
}
