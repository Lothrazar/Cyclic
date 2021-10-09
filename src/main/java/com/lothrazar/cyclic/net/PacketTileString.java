package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public class PacketTileString extends PacketBase {

  private int field;
  private String value;
  private BlockPos pos;

  public PacketTileString(int field, String value, BlockPos pos) {
    super();
    this.field = field;
    this.value = value;
    this.pos = pos;
  }

  public PacketTileString() {
  }

  public static void handle(PacketTileString message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      Level world = player.getCommandSenderWorld();
      BlockEntity tile = world.getBlockEntity(message.pos);
      if (tile instanceof TileEntityBase) {
        TileEntityBase base = (TileEntityBase) tile;
        base.setFieldString(message.field, message.value);
        BlockState oldState = world.getBlockState(message.pos);
        world.sendBlockUpdated(message.pos, oldState, oldState, 3);
      }
    });
    message.done(ctx);
  }

  public static PacketTileString decode(FriendlyByteBuf buf) {
    PacketTileString p = new PacketTileString();
    p.field = buf.readInt();
    CompoundTag tags = buf.readNbt();
    p.pos = new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z"));
    //something in vanilla or forge marks this as CLIENT ONLY. unless i give it a max length
    p.value = buf.readUtf(32767);
    return p;
  }

  public static void encode(PacketTileString msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.field);
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
    buf.writeUtf(msg.value);
  }
}
