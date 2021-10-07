package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.base.PacketBase;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class PacketItemToggle extends PacketBase {

  private int slot;

  public PacketItemToggle(int slot) {
    this.slot = slot;
  }

  public static void handle(PacketItemToggle message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      if (player.containerMenu == null) {
        return;
      }
      int scount = player.containerMenu.slots.size();
      //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
      if (message.slot >= scount) {
        //will NOT work in creative mode. slots are messed up
        return;
      }
      Slot slotObj = player.containerMenu.getSlot(message.slot);
      if (slotObj != null
          && !slotObj.getItem().isEmpty()) {
        ItemStack maybeCharm = slotObj.getItem();
        if (maybeCharm.getItem() instanceof IHasClickToggle) {
          //example: is a charm or something
          IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
          c.toggle(player, maybeCharm);
        }
      }
    });
    message.done(ctx);
  }

  public static PacketItemToggle decode(FriendlyByteBuf buf) {
    PacketItemToggle p = new PacketItemToggle(buf.readInt());
    return p;
  }

  public static void encode(PacketItemToggle msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.slot);
  }
}
