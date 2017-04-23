package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.component.cyclicwand.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWandGui implements IMessage, IMessageHandler<PacketWandGui, IMessage> {
  public static enum WandAction {
    BUILDTYPE, RESET
  }
  private WandAction type;
  public PacketWandGui() {}
  public PacketWandGui(WandAction t) {
    type = t;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    int t = tags.getInteger("t");
    type = WandAction.values()[t];
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("t", type.ordinal());
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketWandGui message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().playerEntity;
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand == null) { return null; }
    if (message.type == WandAction.BUILDTYPE) {
      ItemCyclicWand.BuildType.toggle(wand);
    }
    else {
      ItemCyclicWand.BuildType.resetSlot(wand);
    }
    return null;
  }
}
