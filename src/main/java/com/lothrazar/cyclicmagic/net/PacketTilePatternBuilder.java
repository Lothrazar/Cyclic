package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTilePatternBuilder implements IMessage, IMessageHandler<PacketTilePatternBuilder, IMessage> {
  private BlockPos pos;
  private TileEntityPatternBuilder.Fields type;
  private int direction;
  public PacketTilePatternBuilder() {}
  public PacketTilePatternBuilder(BlockPos p, boolean up, TileEntityPatternBuilder.Fields t) {
    pos = p;
    type = t;
    direction = (up) ? 1 : -1;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    type = TileEntityPatternBuilder.Fields.values()[tags.getInteger("type")];
    direction = tags.getInteger("d");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("type", type.ordinal());
    tags.setInteger("d", direction);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketTilePatternBuilder message, MessageContext ctx) {
    EntityPlayerMP player = ctx.getServerHandler().playerEntity;
    TileEntityPatternBuilder tile = (TileEntityPatternBuilder) player.getEntityWorld().getTileEntity(message.pos);
    if (tile != null) {
      tile.setField(message.type, tile.getField(message.type) + message.direction);
    }
    return null;
  }
}
