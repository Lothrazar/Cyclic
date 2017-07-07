package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileRedstoneToggle implements IMessage, IMessageHandler<PacketTileRedstoneToggle, IMessage> {
  private BlockPos pos;
  public PacketTileRedstoneToggle() {}
  public PacketTileRedstoneToggle(BlockPos p) {
    pos = p;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileRedstoneToggle message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null && tile instanceof ITileRedstoneToggle) {
      ITileRedstoneToggle te = ((ITileRedstoneToggle) tile);
      te.toggleNeedsRedstone();
    }
    return null;
  }
}
