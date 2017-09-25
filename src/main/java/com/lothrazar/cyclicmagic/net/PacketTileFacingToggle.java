package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.gui.ITileFacingToggle;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileFacingToggle implements IMessage, IMessageHandler<PacketTileFacingToggle, IMessage> {
  private BlockPos pos;
  private EnumFacing facing;
  public PacketTileFacingToggle() {}
  public PacketTileFacingToggle(BlockPos p, EnumFacing f) {
    pos = p;
    facing = f;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    this.facing = EnumFacing.values()[tags.getInteger("facing")];
    pos = new BlockPos(x, y, z);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("facing", facing.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTileFacingToggle message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().player;
    TileEntity tile = player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null && tile instanceof ITileFacingToggle) {
      ITileFacingToggle te = ((ITileFacingToggle) tile);
      te.toggleSide(message.facing);
    }
    return null;
  }
}
