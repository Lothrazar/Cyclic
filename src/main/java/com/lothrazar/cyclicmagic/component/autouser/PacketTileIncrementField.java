package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileIncrementField implements IMessage, IMessageHandler<PacketTileIncrementField, IMessage> {
  private BlockPos pos;
  private int field;
  public PacketTileIncrementField() {}
  public PacketTileIncrementField(BlockPos p, int f) {
    pos = p;
  field = f;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    field = tags.getInteger("f");
    pos = new BlockPos(x, y, z);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("f", field);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileIncrementField message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null && tile instanceof TileEntityBaseMachineInvo) {
      TileEntityBaseMachineInvo te = ((TileEntityBaseMachineInvo) tile);
      te.setField(message.field,te.getField(message.field)+1);
    }
    return null;
  }
}
