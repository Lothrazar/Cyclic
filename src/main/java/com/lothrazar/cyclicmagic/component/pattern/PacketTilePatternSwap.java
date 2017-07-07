package com.lothrazar.cyclicmagic.component.pattern;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTilePatternSwap implements IMessage, IMessageHandler<PacketTilePatternSwap, IMessage> {
  private BlockPos pos;
  private int type;
  public static enum SwapType {
    POSITION, RENDER;
  }
  public PacketTilePatternSwap() {}
  public PacketTilePatternSwap(BlockPos p, SwapType t) {
    pos = p;
    type = t.ordinal();
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
  public IMessage onMessage(PacketTilePatternSwap message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntityPatternBuilder tile = (TileEntityPatternBuilder) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      if (message.type == SwapType.POSITION.ordinal())
        tile.swapTargetSource();
      //      else if (message.type == SwapType.RENDER.ordinal())
      //        tile.swapShowRender();
    }
    return null;
  }
}
