package com.lothrazar.cyclicmagic.component.playerext;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenExtendedInventory implements IMessage, IMessageHandler<PacketOpenExtendedInventory, IMessage> {
  public PacketOpenExtendedInventory() {}
  @Override
  public void toBytes(ByteBuf buffer) {}
  @Override
  public void fromBytes(ByteBuf buffer) {}
  @Override
  public IMessage onMessage(PacketOpenExtendedInventory message, MessageContext ctx) {
    ctx.getServerHandler().player.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_EXTENDED, ctx.getServerHandler().player.getEntityWorld(), (int) ctx.getServerHandler().player.posX, (int) ctx.getServerHandler().player.posY, (int) ctx.getServerHandler().player.posZ);
    return null;
  }
}
