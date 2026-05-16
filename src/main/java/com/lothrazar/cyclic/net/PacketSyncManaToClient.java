package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.capabilities.ClientDataManager;
import com.lothrazar.cyclic.capabilities.chunk.ChunkDataStorage;
import com.lothrazar.cyclic.capabilities.player.PlayerCapabilityStorage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

/**
 * Sync Player and Chunk capabilities to client
 *
 */
public class PacketSyncManaToClient implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketSyncManaToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_sync_mana_to_client"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketSyncManaToClient> STREAM_CODEC = StreamCodec.of(PacketSyncManaToClient::encode, PacketSyncManaToClient::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  private int playerMana;
  private int chunkMana;

  public PacketSyncManaToClient(PlayerCapabilityStorage playerMana, ChunkDataStorage chunkMana) {
    this.playerMana = playerMana.getMana();
    this.chunkMana = chunkMana.getMana();
  }
  //  public PacketSyncManaToClient() {}

  public static void handle(PacketSyncManaToClient message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      // .println("client sync message spam");
      ClientDataManager.set(message.playerMana, message.chunkMana);
    });
    
  }

  public static PacketSyncManaToClient decode(RegistryFriendlyByteBuf buf) {
    return new PacketSyncManaToClient(new PlayerCapabilityStorage(buf.readInt()), new ChunkDataStorage(buf.readInt()));
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketSyncManaToClient msg) {
    buf.writeInt(msg.playerMana);
    buf.writeInt(msg.chunkMana);
  }
}
