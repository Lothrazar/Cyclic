package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.util.UtilInventorySort;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDepositContainerToPlayer implements IMessage, IMessageHandler<PacketDepositContainerToPlayer, IMessage> {
  NBTTagCompound tags = new NBTTagCompound();
  public PacketDepositContainerToPlayer() {
  }
  public PacketDepositContainerToPlayer(NBTTagCompound ptags) {
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
  public IMessage onMessage(PacketDepositContainerToPlayer message, MessageContext ctx) {
    EntityPlayer p = ctx.getServerHandler().playerEntity;
    if (UtilPlayer.hasValidOpenContainer(p)) {
      IInventory openInventory = UtilPlayer.getOpenContainerInventory(p);
      UtilInventorySort.sortFromInventoryToPlayer(p.worldObj, openInventory, p, false);
      UtilInventorySort.dumpFromIInventoryToPlayer(p.worldObj, openInventory, p);
    }
    return null;
  }
}
