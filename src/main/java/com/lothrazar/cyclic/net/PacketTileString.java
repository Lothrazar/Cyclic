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

  public PacketTileString() {}

  public static void handle(PacketTileString message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      World world = player.getEntityWorld();
      TileEntity tile = world.getTileEntity(message.pos);
      if (tile instanceof TileEntityBase) {
        TileEntityBase base = (TileEntityBase) tile;
        base.setFieldString(message.field, message.value);
      }
    });
    message.done(ctx);
  }

  public static PacketTileString decode(PacketBuffer buf) {
    PacketTileString p = new PacketTileString();
    p.field = buf.readInt();
    CompoundNBT tags = buf.readCompoundTag();
    p.pos = new BlockPos(tags.getInt("x"), tags.getInt("y"), tags.getInt("z"));
    //something in vanilla or forge marks this as CLIENT ONLY. unless i give it a max length
    p.value = buf.readString(32767);
    return p;
  }

  public static void encode(PacketTileString msg, PacketBuffer buf) {
    buf.writeInt(msg.field);
    CompoundNBT tags = new CompoundNBT();
    tags.putInt("x", msg.pos.getX());
    tags.putInt("y", msg.pos.getY());
    tags.putInt("z", msg.pos.getZ());
    buf.writeCompoundTag(tags);
    buf.writeString(msg.value);
  }
}
