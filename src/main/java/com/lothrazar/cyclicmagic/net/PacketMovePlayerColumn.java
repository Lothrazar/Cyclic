package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMovePlayerColumn implements IMessage, IMessageHandler<PacketMovePlayerColumn, IMessage> {
  public PacketMovePlayerColumn() {}
  private int slot;
  private boolean isDown;
  public PacketMovePlayerColumn(int slotnum, boolean upordown) {
    slot = slotnum;
    isDown = upordown;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    slot = tags.getInteger("slot");
    isDown = tags.getBoolean("isDown");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("slot", slot);
    tags.setBoolean("isDown", isDown);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketMovePlayerColumn message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    if (message.isDown) {
      UtilPlayer.shiftSlotUp(player, message.slot);
    }
    else {
      UtilPlayer.shiftSlotDown(player, message.slot);
    }
    return null;
  }
}
