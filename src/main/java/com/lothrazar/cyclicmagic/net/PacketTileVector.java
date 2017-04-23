package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.component.vector.TileEntityVector;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileVector implements IMessage, IMessageHandler<PacketTileVector, IMessage> {
  private BlockPos pos;
  private int tileFieldId;
  private int value;
  public PacketTileVector() {}
  public PacketTileVector(BlockPos p, int val, int t) {
    pos = p;
    tileFieldId = t;
    value = val;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    tileFieldId = tags.getInteger("type");
    value = tags.getInteger("d");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("type", tileFieldId);
    tags.setInteger("d", value);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileVector message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntityVector tile = (TileEntityVector) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      tile.setField(message.tileFieldId, message.value);
    }
    return null;
  }
}
