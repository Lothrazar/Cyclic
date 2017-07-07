package com.lothrazar.cyclicmagic.component.cyclicwand;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellBuildSize implements IMessage, IMessageHandler<PacketSpellBuildSize, IMessage> {
  public PacketSpellBuildSize() {}
  private int size;
  public PacketSpellBuildSize(int s) {
    size = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    size = tags.getInteger("size");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("size", size);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSpellBuildSize message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);
    if (wand != null && !wand.isEmpty()) {
      ItemCyclicWand.BuildType.setBuildSize(wand, message.size);
    }
    return null;
  }
}
