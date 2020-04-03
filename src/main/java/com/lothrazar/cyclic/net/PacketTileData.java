package com.lothrazar.cyclic.net;

import java.util.function.Supplier;
import com.lothrazar.cyclic.base.PacketBase;
import com.lothrazar.cyclic.base.TileEntityBase;
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

  public PacketTileData(int field, int value, BlockPos pos) {
    super();
    this.field = field;
    this.value = value;
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
        base.setField(message.field, message.value);
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
  }
}
