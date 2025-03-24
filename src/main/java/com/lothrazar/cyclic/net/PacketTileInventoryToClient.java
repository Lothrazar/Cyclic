package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.block.enderitemshelf.ClientAutoSyncItemHandler;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.fixers.CapabilityFixer;
import com.lothrazar.library.packet.PacketFlib;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketTileInventoryToClient implements PacketFlib {

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
      var item = CapabilityFixer.item( Minecraft.getInstance().level,message.blockPos);
      if (item != null) {
//        tile.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
          if (message.type == SyncPacketType.SET) {
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
          //not set, just insert
          item.insertItem(message.slot, message.itemStack, false);
//        });
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
