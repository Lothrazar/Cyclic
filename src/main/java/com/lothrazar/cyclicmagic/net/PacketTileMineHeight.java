package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileMineHeight implements IMessage, IMessageHandler<PacketTileMineHeight, IMessage> {
  private BlockPos pos;
  private int value;
  private String type;
  public PacketTileMineHeight() {}
  public PacketTileMineHeight(BlockPos p, int s, String spr) {
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
    type = tags.getString("type");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("value", value);
    tags.setString("type", type);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileMineHeight message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileMachineMinerSmart tile = (TileMachineMinerSmart) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      //currently the ONLY type
      if (message.type.equals(TileMachineMinerSmart.Fields.HEIGHT.name().toLowerCase())) {
        tile.setHeight(tile.getHeight() + message.value);
      }
      else if (message.type.equals(TileMachineMinerSmart.Fields.LISTTYPE.name().toLowerCase())) {
        tile.toggleListType();
      }
      tile.markDirty();
      if (player.openContainer != null) {
        player.openContainer.detectAndSendChanges();
        player.sendAllWindowProperties(player.openContainer, tile);
      }
    }
    return null;
  }
}
