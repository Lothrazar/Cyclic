package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFakeWorkbench implements IMessage, IMessageHandler<PacketFakeWorkbench, IMessage> {
  public PacketFakeWorkbench() {
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    //    tags = ByteBufUtils.readTag(buf);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    //    ByteBufUtils.writeTag(buf, this.tags);
  }
  @Override
  public IMessage onMessage(PacketFakeWorkbench message, MessageContext ctx) {
    ctx.getServerHandler().playerEntity.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_PWORKBENCH, ctx.getServerHandler().playerEntity.getEntityWorld(), (int) ctx.getServerHandler().playerEntity.posX, (int) ctx.getServerHandler().playerEntity.posY, (int) ctx.getServerHandler().playerEntity.posZ);
    //below is traditional, above is newness
    //    EntityPlayer p = ctx.getServerHandler().playerEntity;
    //    EntityPlayerMP player = (EntityPlayerMP) p;
    //    player.getNextWindowId();
    //    //playerNetServerHandler
    //    player.connection.sendPacket(new SPacketOpenWindow(player.currentWindowId, "minecraft:crafting_table", p.getDisplayName(), 0, player.getEntityId()));
    //    player.openContainer = new ContainerFakeWorkbench(player.inventory, player.worldObj);
    //    player.openContainer.windowId = player.currentWindowId;
    //    //player.openContainer.onCraftGuiOpened(player);
    return null;
  }
}
