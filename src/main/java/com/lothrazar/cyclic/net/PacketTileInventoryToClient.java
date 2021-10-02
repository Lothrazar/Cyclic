package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.block.enderitemshelf.ClientAutoSyncItemHandler;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

public class PacketTileInventoryToClient extends PacketBase {

  private BlockPos blockPos;
  private int slot;
  private ItemStack itemStack;
  private SyncPacketType type;

  public static enum SyncPacketType {
    CHANGE, SET
  }

  public PacketTileInventoryToClient(BlockPos blockPos, int slot, ItemStack itemStack, SyncPacketType type) {
    this.blockPos = blockPos;
    this.slot = slot;
    this.itemStack = itemStack;
    this.type = type;
  }

  public PacketTileInventoryToClient() {}

  @SuppressWarnings("unused")
  public static void handle(PacketTileInventoryToClient message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      if (Minecraft.getInstance().world == null) {
        message.done(ctx);
        return;
      }
      TileEntity tile = Minecraft.getInstance().world.getTileEntity(message.blockPos);
      if (tile != null) {
        tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
          if (message.type == SyncPacketType.SET) {
            if (h instanceof EnderShelfItemHandler) {
              ItemStack extracted = ((EnderShelfItemHandler) h).emptySlot(message.slot);
            }
            else if (h instanceof ClientAutoSyncItemHandler) {
              ItemStack extracted = ((ClientAutoSyncItemHandler) h).emptySlot(message.slot);
            }
            else {
              h.extractItem(message.slot, 64, false);
            }
          }
          //not set, just insert
          h.insertItem(message.slot, message.itemStack, false);
        });
      }
    });
    message.done(ctx);
  }

  public static PacketTileInventoryToClient decode(PacketBuffer buf) {
    PacketTileInventoryToClient p = new PacketTileInventoryToClient();
    p.blockPos = buf.readBlockPos();
    p.slot = buf.readInt();
    p.itemStack = buf.readItemStack();
    p.type = buf.readEnumValue(SyncPacketType.class);
    return p;
  }

  public static void encode(PacketTileInventoryToClient msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.blockPos);
    buf.writeInt(msg.slot);
    buf.writeItemStack(msg.itemStack);
    buf.writeEnumValue(msg.type);
  }
}
