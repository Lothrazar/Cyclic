package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.item.enderbook.EnderBookItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketItemScroll extends PacketBase {

  private int slot;
  private boolean isDown;

  public PacketItemScroll(int slot, boolean isDown) {
    this.slot = slot;
    this.isDown = isDown;
  }

  public static void handle(PacketItemScroll message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      System.out.println("pkt item scroll" + message.slot + "" + message.isDown);
      player.getCooldownTracker().setCooldown(ItemRegistry.ENDER_BOOK.get(), 5);
      EnderBookItem.scroll(player, message.slot, message.isDown);
      //      if (player.openContainer == null) {
      //        return;
      //      }
      //      int scount = player.openContainer.inventorySlots.size();
      //      //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
      //      if (message.slot >= scount) {
      //        //will NOT work in creative mode. slots are messed up
      //        return;
      //      }
      //      Slot slotObj = player.openContainer.getSlot(message.slot);
      //      if (slotObj != null
      //          && !slotObj.getStack().isEmpty()) {
      //        ItemStack maybeCharm = slotObj.getStack();
      //        if (maybeCharm.getItem() instanceof IHasClickToggle) {
      //          //example: is a charm or something
      //          IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
      //          c.toggle(player, maybeCharm);
      //        }
      //      }
    });
    message.done(ctx);
  }

  public static PacketItemScroll decode(PacketBuffer buf) {
    return new PacketItemScroll(buf.readInt(), buf.readBoolean());
  }

  public static void encode(PacketItemScroll msg, PacketBuffer buf) {
    buf.writeInt(msg.slot);
    buf.writeBoolean(msg.isDown);
  }
}
