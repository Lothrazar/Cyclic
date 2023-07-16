package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.library.packet.PacketFlib;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketPlayerSyncToClient extends PacketFlib {

  private boolean mayfly;

  public PacketPlayerSyncToClient(boolean mayfly) {
    this.mayfly = mayfly;
  }

  public PacketPlayerSyncToClient() {}

  public static void handle(PacketPlayerSyncToClient message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      Minecraft.getInstance().player.getAbilities().mayfly = message.mayfly;
      if (!message.mayfly) {
        //if not allowed to fly, also cancel flying
        Minecraft.getInstance().player.getAbilities().flying = false;
      }
    });
    message.done(ctx);
  }

  public static PacketPlayerSyncToClient decode(FriendlyByteBuf buf) {
    return new PacketPlayerSyncToClient(buf.readBoolean());
  }

  public static void encode(PacketPlayerSyncToClient msg, FriendlyByteBuf buf) {
    buf.writeBoolean(msg.mayfly);
  }
}
