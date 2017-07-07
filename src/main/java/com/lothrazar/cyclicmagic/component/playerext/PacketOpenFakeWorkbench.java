package com.lothrazar.cyclicmagic.component.playerext;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenFakeWorkbench implements IMessage, IMessageHandler<PacketOpenFakeWorkbench, IMessage> {
  public PacketOpenFakeWorkbench() {}
  @Override
  public void fromBytes(ByteBuf buf) {}
  @Override
  public void toBytes(ByteBuf buf) {}
  @Override
  public IMessage onMessage(PacketOpenFakeWorkbench message, MessageContext ctx) {
    ctx.getServerHandler().playerEntity.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_PWORKBENCH, ctx.getServerHandler().playerEntity.getEntityWorld(), (int) ctx.getServerHandler().playerEntity.posX, (int) ctx.getServerHandler().playerEntity.posY, (int) ctx.getServerHandler().playerEntity.posZ);
    return null;
  }
}
