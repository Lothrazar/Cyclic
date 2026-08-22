package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.block.enderitemshelf.ClientAutoSyncItemHandler;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.util.CapabilityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public final class ClientNetHandlers {

  private ClientNetHandlers() {
  }

  public static void handleTileInventory(PacketTileInventoryToClient message) {
    if (Minecraft.getInstance().level == null) {
      return;
    }
    var item = CapabilityUtil.item(Minecraft.getInstance().level, message.blockPos);
    if (item != null) {
      if (message.type == PacketTileInventoryToClient.SyncPacketType.SET) {
        if (item instanceof EnderShelfItemHandler es) {
          ItemStack extracted = es.emptySlot(message.slot);
        }
        else if (item instanceof ClientAutoSyncItemHandler cas) {
          ItemStack extracted = cas.emptySlot(message.slot);
        }
        else {
          item.extractItem(message.slot, 64, false);
        }
      }
      item.insertItem(message.slot, message.itemStack, false);
    }
  }

  public static void handlePlayerSync(PacketPlayerSyncToClient message) {
    Minecraft.getInstance().player.getAbilities().mayfly = message.mayfly;
    if (!message.mayfly) {
      Minecraft.getInstance().player.getAbilities().flying = false;
    }
  }

}
