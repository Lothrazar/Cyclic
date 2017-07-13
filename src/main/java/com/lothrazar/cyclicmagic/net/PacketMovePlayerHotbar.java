package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMovePlayerHotbar implements IMessage, IMessageHandler<PacketMovePlayerHotbar, IMessage> {
  public PacketMovePlayerHotbar() {}
  private boolean isDown;
  public PacketMovePlayerHotbar(boolean isdown) {
    isDown = isdown;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    isDown = tags.getBoolean("isDown");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setBoolean("isDown", isDown);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketMovePlayerHotbar message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    if (message.isDown) {
      UtilPlayer.shiftBarDown(player);
    }
    else {
      UtilPlayer.shiftBarUp(player);
    }
    return null;
  }
}
