package com.lothrazar.cyclic.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

public class PacketPlayerSyncToClient implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketPlayerSyncToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_player_sync_to_client"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketPlayerSyncToClient> STREAM_CODEC = StreamCodec.of(PacketPlayerSyncToClient::encode, PacketPlayerSyncToClient::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  boolean mayfly;

  public PacketPlayerSyncToClient(boolean mayfly) {
    this.mayfly = mayfly;
  }

  public PacketPlayerSyncToClient() {}

  public static void handle(PacketPlayerSyncToClient message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> ClientNetHandlers.handlePlayerSync(message));
  }

  public static PacketPlayerSyncToClient decode(RegistryFriendlyByteBuf buf) {
    return new PacketPlayerSyncToClient(buf.readBoolean());
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketPlayerSyncToClient msg) {
    buf.writeBoolean(msg.mayfly);
  }
}
