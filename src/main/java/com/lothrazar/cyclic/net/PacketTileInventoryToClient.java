package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.block.enderitemshelf.ClientAutoSyncItemHandler;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.fixers.CapabilityFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PacketTileInventoryToClient implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketTileInventoryToClient> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_tile_inventory_to_client"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketTileInventoryToClient> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketTileInventoryToClient::encode, PacketTileInventoryToClient::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


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
  public static void handle(PacketTileInventoryToClient message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      if (Minecraft.getInstance().level == null) {
        
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
    
  }

  public static PacketTileInventoryToClient decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
    PacketTileInventoryToClient p = new PacketTileInventoryToClient();
    p.blockPos = buf.readBlockPos();
    p.slot = buf.readInt();
    p.itemStack = net.minecraft.world.item.ItemStack.STREAM_CODEC.decode(buf);
    p.type = buf.readEnum(SyncPacketType.class);
    return p;
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, PacketTileInventoryToClient msg) {
    buf.writeBlockPos(msg.blockPos);
    buf.writeInt(msg.slot);
    net.minecraft.world.item.ItemStack.STREAM_CODEC.encode(buf, msg.itemStack);
    buf.writeEnum(msg.type);
  }
}
