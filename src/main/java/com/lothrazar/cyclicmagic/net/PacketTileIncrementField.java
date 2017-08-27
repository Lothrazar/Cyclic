package com.lothrazar.cyclicmagic.net;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * generic packet handler for tile entities. assumes they handle cyclic fields
 * that start over
 * 
 * TODO: see if we can reuse this mroe and remove uneccessary classes
 * 
 * @author Sam
 *
 */
public class PacketTileIncrementField implements IMessage, IMessageHandler<PacketTileIncrementField, IMessage> {
  private BlockPos pos;
  private int field;
  private int value;
  public PacketTileIncrementField() {}
  public PacketTileIncrementField(BlockPos p, int f) {
    this(p, f, 1);
  }
  public PacketTileIncrementField(BlockPos p, int f, int val) {
    pos = p;
    this.field = f;
    value = val;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    field = tags.getInteger("f");
    value = tags.getInteger("v");
    pos = new BlockPos(x, y, z);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("f", field);
    tags.setInteger("v", value);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileIncrementField message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    try {
      TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
      if (tile != null && tile instanceof IInventory) {
        IInventory tileInvo = ((IInventory) tile);
        int newVal = tileInvo.getField(message.field) + message.value;
        System.out.println("inc field   "+message.field+"__" + newVal);
        tileInvo.setField(message.field, newVal);
      }
    }
    catch (Exception e) {//since we dont know which class exactly this might get run on
      e.printStackTrace();
    }
    return null;
  }
}
