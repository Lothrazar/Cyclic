package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.base.IHasClickToggle;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlayerFalldamage {

  private int slot;

  public PacketPlayerFalldamage() {}

  public PacketPlayerFalldamage(int slot) {
    this.slot = slot;
  }

  public static void handle(PacketPlayerFalldamage message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      if (player.openContainer == null) {
        return;
      }
      int scount = player.openContainer.inventorySlots.size();
      //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
      if (message.slot >= scount) {
        //will NOT work in creative mode. slots are messed up
        return;
      }
      Slot slotObj = player.openContainer.getSlot(message.slot);
      if (slotObj != null
          && !slotObj.getStack().isEmpty()) {
        ItemStack maybeCharm = slotObj.getStack();
        if (maybeCharm.getItem() instanceof IHasClickToggle) {
          //example: is a charm or something
          IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
          c.toggle(player, maybeCharm);
        }
      }
    });
  }

  public static PacketPlayerFalldamage decode(PacketBuffer buf) {
    PacketPlayerFalldamage message = new PacketPlayerFalldamage();
    message.slot = buf.readInt();
    return message;
  }

  public static void encode(PacketPlayerFalldamage msg, PacketBuffer buf) {
    buf.writeInt(msg.slot);
  }
}
