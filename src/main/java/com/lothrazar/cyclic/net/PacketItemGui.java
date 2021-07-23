package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.item.storagebag.ContainerStorageBag;
import com.lothrazar.cyclic.item.storagebag.StorageBagContainerProvider;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class PacketItemGui extends PacketBase {

  private int slot;

  public PacketItemGui(int slot) {
    this.slot = slot;
  }

  public static void handle(PacketItemGui message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      if ((player.openContainer instanceof ContainerStorageBag) == false) {
        NetworkHooks.openGui(player, new StorageBagContainerProvider(), player.getPosition());
      }
      //
      //
    });
    message.done(ctx);
  }

  public static PacketItemGui decode(PacketBuffer buf) {
    PacketItemGui p = new PacketItemGui(buf.readInt());
    return p;
  }

  public static void encode(PacketItemGui msg, PacketBuffer buf) {
    buf.writeInt(msg.slot);
  }
}
