package com.lothrazar.cyclicmagic.component.playerext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenNormalInventory implements IMessage, IMessageHandler<PacketOpenNormalInventory, IMessage> {
  public PacketOpenNormalInventory() {}
  public PacketOpenNormalInventory(EntityPlayer player) {}
  @Override
  public void toBytes(ByteBuf buffer) {}
  @Override
  public void fromBytes(ByteBuf buffer) {}
  @Override
  public IMessage onMessage(PacketOpenNormalInventory message, MessageContext ctx) {
    ctx.getServerHandler().player.openContainer.onContainerClosed(ctx.getServerHandler().player);
    ctx.getServerHandler().player.openContainer = ctx.getServerHandler().player.inventoryContainer;
    return null;
  }
}
