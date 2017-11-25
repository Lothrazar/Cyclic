package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.gui.ITileFuel;
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
public class PacketTileFuelDisplay implements IMessage, IMessageHandler<PacketTileFuelDisplay, IMessage> {
  private BlockPos pos;
  public PacketTileFuelDisplay() {}
  public PacketTileFuelDisplay(BlockPos p) {
    this.pos=p;
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
  public IMessage onMessage(PacketTileFuelDisplay message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    try {
      TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
      /**
       * TODO: eventually this packet could hit any fuel function, with type   flag
       */
      if (tile != null && tile instanceof ITileFuel) {
        ITileFuel tileInvo = ((ITileFuel) tile);
        
        tileInvo.toggleFuelDisplay();
      }
    }
    catch (Exception e) {//since we dont know which class exactly this might get run on
      e.printStackTrace();
    }
    return null;
  }
}
