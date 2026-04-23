package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PacketTileString implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {

  public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<PacketTileString> TYPE = new Type<>(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.lothrazar.cyclic.ModCyclic.MODID, "packet_tile_string"));

  public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, PacketTileString> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(PacketTileString::encode, PacketTileString::decode);


  @Override
  public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
    return TYPE;
  }


  private int field;
  private String value;
  private BlockPos pos;

  public PacketTileString(int field, String value, BlockPos pos) {
    super();
    this.field = field;
    this.value = value;
    this.pos = pos;
  }

  public PacketTileString() {}

  public static void handle(PacketTileString message, net.neoforged.neoforge.network.handling.IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) ctx.player();
      Level world = player.getCommandSenderWorld();
      BlockEntity tile = world.getBlockEntity(message.pos);
      if (tile instanceof TileBlockEntityCyclic) {
        TileBlockEntityCyclic base = (TileBlockEntityCyclic) tile;
        base.setFieldString(message.field, message.value);
        BlockState oldState = world.getBlockState(message.pos);
        world.sendBlockUpdated(message.pos, oldState, oldState, 3);
      }
    });
    
  }

  public static PacketTileString decode(net.minecraft.network.RegistryFriendlyByteBuf buf) {
    PacketTileString p = new PacketTileString();
    p.field = buf.readInt();
    CompoundTag tags = buf.readNbt();
    p.pos = new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z"));
    //something in vanilla or forge marks this as CLIENT ONLY. unless i give it a max length
    p.value = buf.readUtf(32767);
    return p;
  }

  public static void encode(net.minecraft.network.RegistryFriendlyByteBuf buf, PacketTileString msg) {
    buf.writeInt(msg.field);
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
    buf.writeUtf(msg.value);
  }
}
