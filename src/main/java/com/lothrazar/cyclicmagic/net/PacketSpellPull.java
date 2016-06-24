package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellPull implements IMessage, IMessageHandler<PacketSpellPull, IMessage> {
  public static final int ID = 20;
  private BlockPos pos;
  private EnumFacing side;
  public PacketSpellPull() {
  }
  public PacketSpellPull(BlockPos mouseover, EnumFacing s) {
    pos = mouseover;
    side = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int x = tags.getInteger("x");
    int y = tags.getInteger("y");
    int z = tags.getInteger("z");
    pos = new BlockPos(x, y, z);
    side = EnumFacing.values()[tags.getInteger("side")];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("x", pos.getX());
    tags.setInteger("y", pos.getY());
    tags.setInteger("z", pos.getZ());
    tags.setInteger("side", side.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSpellPull message, MessageContext ctx) {
    if (ctx.side.isServer() && message != null && message.pos != null) {
      EntityPlayer p = ctx.getServerHandler().playerEntity;
      SpellRegistry.Spells.pull.castFromServer(message.pos, message.side, p);
    }
    return null;
  }
}
