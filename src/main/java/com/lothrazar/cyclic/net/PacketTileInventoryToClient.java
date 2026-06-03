package com.lothrazar.cyclic.net;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

public class PacketTileInventoryToClient implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketTileInventoryToClient> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "packet_tile_inventory_to_client"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketTileInventoryToClient> STREAM_CODEC = StreamCodec.of(PacketTileInventoryToClient::encode, PacketTileInventoryToClient::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  BlockPos blockPos;
  int slot;
  ItemStack itemStack;
  SyncPacketType type;

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

  public static void handle(PacketTileInventoryToClient message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> ClientNetHandlers.handleTileInventory(message));
  }

  public static PacketTileInventoryToClient decode(RegistryFriendlyByteBuf buf) {
    PacketTileInventoryToClient p = new PacketTileInventoryToClient();
    p.blockPos = buf.readBlockPos();
    p.slot = buf.readInt();
    p.itemStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
    p.type = buf.readEnum(SyncPacketType.class);
    return p;
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketTileInventoryToClient msg) {
    buf.writeBlockPos(msg.blockPos);
    buf.writeInt(msg.slot);
    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, msg.itemStack);
    buf.writeEnum(msg.type);
  }
}
