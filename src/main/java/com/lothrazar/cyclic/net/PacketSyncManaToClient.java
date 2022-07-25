package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.capabilities.ManaManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncManaToClient extends PacketBaseCyclic {

  private int playerMana;
  private int chunkMana;

  public PacketSyncManaToClient(int playerMana, int chunkMana) {
    this.playerMana = playerMana;
    this.chunkMana = chunkMana;
  }
  //  public PacketSyncManaToClient() {}

  public static void handle(PacketSyncManaToClient message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      // Here we are client side.
      // Be very careful not to access client-only classes here! (like Minecraft) because
      // this packet needs to be available server-side too
      System.out.println("client sync message spam");
      ManaManager.ClientManaData.set(message.playerMana, message.chunkMana);
    });
    message.done(ctx);
  }

  public static PacketSyncManaToClient decode(FriendlyByteBuf buf) {
    return new PacketSyncManaToClient(buf.readInt(), buf.readInt());
  }

  public static void encode(PacketSyncManaToClient msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.playerMana);
    buf.writeInt(msg.chunkMana);
  }
}
