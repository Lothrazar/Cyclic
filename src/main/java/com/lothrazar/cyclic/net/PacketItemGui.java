package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.item.storagebag.ContainerStorageBag;
import com.lothrazar.cyclic.item.storagebag.StorageBagContainerProvider;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class PacketItemGui extends PacketBase {

  private int slot;

  public PacketItemGui(int slot) {
    this.slot = slot;
  }

  public static void handle(PacketItemGui message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      if ((player.containerMenu instanceof ContainerStorageBag) == false) {
        NetworkHooks.openGui(player, new StorageBagContainerProvider(), player.blockPosition());
      }
      //
      //
    });
    message.done(ctx);
  }

  public static PacketItemGui decode(FriendlyByteBuf buf) {
    PacketItemGui p = new PacketItemGui(buf.readInt());
    return p;
  }

  public static void encode(PacketItemGui msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.slot);
  }
}
