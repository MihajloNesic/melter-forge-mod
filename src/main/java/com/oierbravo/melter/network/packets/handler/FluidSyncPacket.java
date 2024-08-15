package com.oierbravo.melter.network.packets.handler;

import com.oierbravo.melter.content.melter.MelterBlockEntity;
import com.oierbravo.melter.network.packets.data.FluidSyncPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FluidSyncPacket {
    public static final FluidSyncPacket INSTANCE = new FluidSyncPacket();

    public static FluidSyncPacket get() {
        return INSTANCE;
    }

    public void handle(final FluidSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            var level = context.player().level();
            if (level.isLoaded(payload.pos())) {
                var be = level.getBlockEntity(payload.pos());
                if (be instanceof MelterBlockEntity controller) {
                    controller.setFluid(payload.fluidStack());
                }
            }
        });
    }
}
