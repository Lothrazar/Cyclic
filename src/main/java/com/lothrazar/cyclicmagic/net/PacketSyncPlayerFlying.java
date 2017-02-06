package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncPlayerFlying implements IMessage, IMessageHandler<PacketSyncPlayerFlying, IMessage> {
  private boolean flying;
  public PacketSyncPlayerFlying() {}
  public PacketSyncPlayerFlying(boolean h) {
    flying = h;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    flying = tags.getBoolean("h");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setBoolean("h", flying);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSyncPlayerFlying message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      EntityPlayer player = ModCyclic.proxy.getPlayerEntity(ctx);
      if (player != null) {
        player.capabilities.allowFlying = message.flying;
        player.capabilities.isFlying = message.flying;
      }
    }
    return null;
  }
}
