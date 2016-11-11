package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineHarvester;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileHarvester implements IMessage, IMessageHandler<PacketTileHarvester, IMessage> {
  private BlockPos pos;
  private int type;
  public PacketTileHarvester() {
  }
  public PacketTileHarvester(BlockPos p) {
    pos = p;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    type = tags.getInteger("t");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("t", type);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileHarvester message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null && tile instanceof TileMachineHarvester) {
      TileMachineHarvester te = ((TileMachineHarvester) tile);
      te.toggleSize();
      te.displayPreview();
    }
    return null;
  }
}
