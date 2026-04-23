package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.capabilities.ClientDataManager;
import com.lothrazar.cyclic.capabilities.chunk.ChunkDataStorage;
import com.lothrazar.cyclic.capabilities.player.PlayerCapabilityStorage;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Sync Player and Chunk capabilities to client
 *
 */
public class PacketSyncManaToClient implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketSyncManaToClient> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_sync_mana_to_client"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketSyncManaToClient> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketSyncManaToClient::encode, PacketSyncManaToClient::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


  private int playerMana; // TODO : playerData object
  private int chunkMana; // TODO: chunkData object

  public PacketSyncManaToClient(PlayerCapabilityStorage playerMana, ChunkDataStorage chunkMana) {
    this.playerMana = playerMana.getMana();
    this.chunkMana = chunkMana.getMana();
  }
  //  public PacketSyncManaToClient() {}

  public static void handle(PacketSyncManaToClient message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      // .println("client sync message spam");
      ClientDataManager.set(message.playerMana, message.chunkMana);
    });
    
  }

  public static PacketSyncManaToClient decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
    return new PacketSyncManaToClient(new PlayerCapabilityStorage(buf.readInt()), new ChunkDataStorage(buf.readInt()));
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, PacketSyncManaToClient msg) {
    buf.writeInt(msg.playerMana);
    buf.writeInt(msg.chunkMana);
  }
}
