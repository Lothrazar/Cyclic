package com.lothrazar.cyclicmagic.net;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlayerFalldamage implements IMessage, IMessageHandler<PacketPlayerFalldamage, IMessage> {
  NBTTagCompound tags = new NBTTagCompound();
  public PacketPlayerFalldamage() {}
  @Override
  public void fromBytes(ByteBuf buf) {
    tags = ByteBufUtils.readTag(buf);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, this.tags);
  }
  @Override
  public IMessage onMessage(PacketPlayerFalldamage message, MessageContext ctx) {
    EntityPlayer p = ctx.getServerHandler().playerEntity;
    System.out.println("do climb; send falldistance to zero to server "+    p.fallDistance  );
    
    p.fallDistance = 0;
    return null;
  }
}
