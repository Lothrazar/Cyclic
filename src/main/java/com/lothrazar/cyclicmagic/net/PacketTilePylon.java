package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityXpPylon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTilePylon implements IMessage, IMessageHandler<PacketTilePylon, IMessage> {
  private BlockPos pos;
  private int value;
  private TileEntityXpPylon.Fields type;
  public PacketTilePylon() {}
  public PacketTilePylon(BlockPos p, int s, TileEntityXpPylon.Fields spr) {
    pos = p;
    value = s;
    type = spr;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    value = tags.getInteger("value");
    type = TileEntityXpPylon.Fields.values()[tags.getInteger("t")];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("value", value);
    tags.setInteger("t", type.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTilePylon message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntityXpPylon tile = (TileEntityXpPylon) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      int prev = tile.getField(message.type.ordinal());//value will be + or 1 something so increment by that
      tile.setField(message.type.ordinal(), prev + message.value);
      tile.markDirty();
      if (player.openContainer != null) {
        player.openContainer.detectAndSendChanges();
        player.sendAllWindowProperties(player.openContainer, tile);
      }
    }
    return null;
  }
}
