package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.block.enderitemshelf.ClientAutoSyncItemHandler;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkEvent;

public class PacketTileInventoryToClient extends PacketBaseCyclic {

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
      if (Minecraft.getInstance().level == null) {
        message.done(ctx);
        return;
      }
      BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(message.blockPos);
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

  public static PacketTileInventoryToClient decode(FriendlyByteBuf buf) {
    PacketTileInventoryToClient p = new PacketTileInventoryToClient();
    p.blockPos = buf.readBlockPos();
    p.slot = buf.readInt();
    p.itemStack = buf.readItem();
    p.type = buf.readEnum(SyncPacketType.class);
    return p;
  }

  public static void encode(PacketTileInventoryToClient msg, FriendlyByteBuf buf) {
    buf.writeBlockPos(msg.blockPos);
    buf.writeInt(msg.slot);
    buf.writeItem(msg.itemStack);
    buf.writeEnum(msg.type);
  }
}
