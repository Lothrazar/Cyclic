package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class PacketPlayerSyncToClient implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketPlayerSyncToClient> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_player_sync_to_client"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketPlayerSyncToClient> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketPlayerSyncToClient::encode, PacketPlayerSyncToClient::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


  private boolean mayfly;

  public PacketPlayerSyncToClient(boolean mayfly) {
    this.mayfly = mayfly;
  }

  public PacketPlayerSyncToClient() {}

  public static void handle(PacketPlayerSyncToClient message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      Minecraft.getInstance().player.getAbilities().mayfly = message.mayfly;
      if (!message.mayfly) {
        //if not allowed to fly, also cancel flying
        Minecraft.getInstance().player.getAbilities().flying = false;
      }
    });
    
  }

  public static PacketPlayerSyncToClient decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
    return new PacketPlayerSyncToClient(buf.readBoolean());
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, PacketPlayerSyncToClient msg) {
    buf.writeBoolean(msg.mayfly);
  }
}
