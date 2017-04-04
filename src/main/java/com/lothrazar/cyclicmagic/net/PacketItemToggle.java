package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.IHasClickToggle;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketItemToggle implements IMessage, IMessageHandler<PacketItemToggle, IMessage> {
  private int slot;
  public PacketItemToggle() {}
  public PacketItemToggle(int itemSlot) {
    slot = itemSlot;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    slot = tags.getInteger("slot");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("slot", slot);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketItemToggle message, MessageContext ctx) {
    if (ctx.side.isServer()) {
      EntityPlayer player = ctx.getServerHandler().playerEntity;
      if (player.openContainer == null) { return null; }
      int scount = player.openContainer.inventorySlots.size();
      //this is an edge case but it DID happen: put charmin your hotbar and then open a creative inventory tab. avoid index OOB
      if (message.slot >= scount) { return null; }
      Slot slotObj = player.openContainer.getSlot(message.slot);
      if (slotObj != null
          && slotObj.getStack() != ItemStack.EMPTY) {
        ItemStack maybeCharm = slotObj.getStack();
        if (maybeCharm.getItem() instanceof IHasClickToggle) {
          //example: is a charm or something
          IHasClickToggle c = (IHasClickToggle) maybeCharm.getItem();
          c.toggle(player, maybeCharm);
        }
      }
    }
    return null;
  }
}
