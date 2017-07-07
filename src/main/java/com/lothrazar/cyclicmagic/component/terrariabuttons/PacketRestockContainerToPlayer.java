package com.lothrazar.cyclicmagic.component.terrariabuttons;
import com.lothrazar.cyclicmagic.module.GuiTerrariaButtonsModule;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRestockContainerToPlayer implements IMessage, IMessageHandler<PacketRestockContainerToPlayer, IMessage> {
  NBTTagCompound tags = new NBTTagCompound();
  public PacketRestockContainerToPlayer() {}
  public PacketRestockContainerToPlayer(NBTTagCompound ptags) {
    tags = ptags;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    tags = ByteBufUtils.readTag(buf);
  }
  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, this.tags);
  }
  @Override
  public IMessage onMessage(PacketRestockContainerToPlayer message, MessageContext ctx) {
    EntityPlayer p = ctx.getServerHandler().playerEntity;
    if (UtilPlayer.hasValidOpenContainer(p)) {
      IInventory openInventory = UtilPlayer.getOpenContainerInventory(p);
      UtilInventoryTransfer.sortFromInventoryToPlayer(p.getEntityWorld(), openInventory, p, GuiTerrariaButtonsModule.restockLeaveOne);
    }
    return null;
  }
}
