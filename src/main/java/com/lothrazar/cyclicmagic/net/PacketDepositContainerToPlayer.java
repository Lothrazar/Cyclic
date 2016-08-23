package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilInventorySort;
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
    if (p.openContainer == null || p.openContainer.getSlot(0) == null || p.openContainer.getSlot(0).inventory == null) {
      ModMain.logger.error("ERROR LOG: null container inventory");
    }
    else {
      // a workaround since player does not reference the inventory, only the
      // container
      // and Container has no get method
      IInventory openInventory = p.openContainer.getSlot(0).inventory;
      UtilInventorySort.sortFromInventoryToPlayer(p.worldObj, openInventory, p, false);
      UtilInventorySort.dumpFromIInventoryToPlayer(p.worldObj, openInventory, p);
    }
    return null;
  }
}
