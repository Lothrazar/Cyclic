package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.base.TileEntityBase;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTileData extends PacketBase {

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
    super();
    this.field = field;
    this.value = value ? 1 : 0;
    this.autoIncrement = false;
    this.pos = pos;
  }

  public PacketTileData() {}

  public static void handle(PacketTileData message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      World world = player.getEntityWorld();
      TileEntity tile = world.getTileEntity(message.pos);
      if (tile instanceof TileEntityBase) {
        TileEntityBase base = (TileEntityBase) tile;
        if (message.autoIncrement) {
          //ignore message.value, do a ++
          int incr = base.getField(message.field) + 1;
          base.setField(message.field, incr);
        }
        else {
          base.setField(message.field, message.value);
        }
      }
    });
    message.done(ctx);
  }

  public static PacketTileData decode(PacketBuffer buf) {
    PacketTileData p = new PacketTileData();
    p.field = buf.readInt();
    p.value = buf.readInt();
    CompoundNBT tags = buf.readCompoundTag();
    p.pos = new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z"));
    p.autoIncrement = buf.readBoolean();
    return p;
  }

  public static void encode(PacketTileData msg, PacketBuffer buf) {
    buf.writeInt(msg.field);
    buf.writeInt(msg.value);
    CompoundNBT tags = new CompoundNBT();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeCompoundTag(tags);
    buf.writeBoolean(msg.autoIncrement);
  }
}
