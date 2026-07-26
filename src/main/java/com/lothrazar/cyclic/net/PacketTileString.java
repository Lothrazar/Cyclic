package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.lothrazar.cyclic.ModCyclic;

public class PacketTileString implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<PacketTileString> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ModCyclic.MODID, "packet_tile_string"));

  public static final StreamCodec<RegistryFriendlyByteBuf, PacketTileString> STREAM_CODEC = StreamCodec.of(PacketTileString::encode, PacketTileString::decode);


  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
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

  public static void handle(PacketTileString message, IPayloadContext ctx) {
    ctx.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) ctx.player();
      Level world = player.level();
      BlockEntity tile = world.getBlockEntity(message.pos);
      if (tile instanceof TileBlockEntityCyclic) {
        TileBlockEntityCyclic base = (TileBlockEntityCyclic) tile;
        base.setFieldString(message.field, message.value);
        BlockState oldState = world.getBlockState(message.pos);
        world.sendBlockUpdated(message.pos, oldState, oldState, 3);
      }
    });
    
  }

  public static PacketTileString decode(RegistryFriendlyByteBuf buf) {
    PacketTileString p = new PacketTileString();
    p.field = buf.readInt();
    CompoundTag tags = buf.readNbt();
    p.pos = new BlockPos(tags.getIntOr("x", 0), tags.getIntOr("y", 0), tags.getIntOr("z", 0));
    //something in vanilla or forge marks this as CLIENT ONLY. unless i give it a max length
    p.value = buf.readUtf(32767);
    return p;
  }

  public static void encode(RegistryFriendlyByteBuf buf, PacketTileString msg) {
    buf.writeInt(msg.field);
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
    buf.writeUtf(msg.value);
  }
}
