package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.function.Supplier;

public class PacketTileInventory extends PacketBase {

  private BlockPos blockPos;
  private int slot;
  private ItemStack itemStack;
  private TYPE type;

  public enum TYPE {
    CHANGE, SET
  }

  public PacketTileInventory(BlockPos blockPos, int slot, ItemStack itemStack, TYPE type) {
    this.blockPos = blockPos;
    this.slot = slot;
    this.itemStack = itemStack;
    this.type = type;
  }

  public PacketTileInventory() {}

  public static void handle(PacketTileInventory message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      PlayerEntity player = ModCyclic.proxy.getClientPlayer();
      TileEntity tile = player.world.getTileEntity(message.blockPos);
      if (tile != null)
      tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
        if (message.type == TYPE.SET && h instanceof EnderShelfItemHandler) {
          ItemStack was = h.getStackInSlot(message.slot);
          ItemStack extracted = ((EnderShelfItemHandler) h).emptySlot(message.slot);
        }
        h.insertItem(message.slot, message.itemStack, false);
      });
    });
    message.done(ctx);
  }

  public static PacketTileInventory decode(PacketBuffer buf) {
    PacketTileInventory p = new PacketTileInventory();
    p.blockPos = buf.readBlockPos();
    p.slot = buf.readInt();
    p.itemStack = buf.readItemStack();
    p.type = buf.readEnumValue(TYPE.class);
    return p;
  }

  public static void encode(PacketTileInventory msg, PacketBuffer buf) {
    buf.writeBlockPos(msg.blockPos);
    buf.writeInt(msg.slot);
    buf.writeItemStack(msg.itemStack);
    buf.writeEnumValue(msg.type);
  }
}
