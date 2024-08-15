package com.oierbravo.melter.registrate;

import com.oierbravo.melter.Melter;
import com.oierbravo.melter.network.packets.data.FluidSyncPayload;
import com.oierbravo.melter.network.packets.data.ItemSyncPayload;
import com.oierbravo.melter.network.packets.handler.FluidSyncPacket;
import com.oierbravo.melter.network.packets.handler.ItemSyncPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModMessages {
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Melter.MODID);

        //Going to Client
        registrar.playToClient(FluidSyncPayload.TYPE, FluidSyncPayload.STREAM_CODEC, FluidSyncPacket.get()::handle);
        registrar.playToClient(ItemSyncPayload.TYPE, ItemSyncPayload.STREAM_CODEC, ItemSyncPacket.get()::handle);

    }
    public static void sendToAllClients(CustomPacketPayload message) {
        PacketDistributor.sendToAllPlayers(message);
    }

    public static void register() {
    }
}
