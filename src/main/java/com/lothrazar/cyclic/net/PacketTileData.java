package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public class PacketTileData extends PacketBaseCyclic {

  private int field;
  private int value;
  private BlockPos pos;
  private boolean autoIncrement = false;

  public PacketTileData(int field, BlockPos pos) {
    super();
    this.field = field;
    this.value = -1;
    this.autoIncrement = true;
    this.pos = pos;
  }

  public PacketTileData(int field, int value, BlockPos pos) {
    super();
    this.field = field;
    this.value = value;
    this.autoIncrement = false;
    this.pos = pos;
  }

  public PacketTileData(int field, boolean value, BlockPos pos) {
    this(field, value ? 1 : 0, pos);
  }

  public PacketTileData() {}

  public static void handle(PacketTileData message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer player = ctx.get().getSender();
      Level world = player.getCommandSenderWorld();
      BlockEntity tile = world.getBlockEntity(message.pos);
      if (tile instanceof TileBlockEntityCyclic) {
        TileBlockEntityCyclic base = (TileBlockEntityCyclic) tile;
        if (message.autoIncrement) {
          //ignore message.value, do a ++
          int incr = base.getField(message.field) + 1;
          base.setField(message.field, incr);
        }
        else {
          base.setField(message.field, message.value);
        }
        base.setChanged();
      }
    });
    message.done(ctx);
  }

  public static PacketTileData decode(FriendlyByteBuf buf) {
    PacketTileData p = new PacketTileData();
    p.field = buf.readInt();
    p.value = buf.readInt();
    CompoundTag tags = buf.readNbt();
    p.pos = new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z"));
    p.autoIncrement = buf.readBoolean();
    return p;
  }

  public static void encode(PacketTileData msg, FriendlyByteBuf buf) {
    buf.writeInt(msg.field);
    buf.writeInt(msg.value);
    CompoundTag tags = new CompoundTag();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeNbt(tags);
    buf.writeBoolean(msg.autoIncrement);
  }
}
