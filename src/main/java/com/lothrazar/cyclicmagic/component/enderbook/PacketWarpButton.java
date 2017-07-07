package com.lothrazar.cyclicmagic.component.enderbook;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWarpButton implements IMessage, IMessageHandler<PacketWarpButton, IMessage> {
  public int slot;
  public PacketWarpButton() {}
  public PacketWarpButton(int s) {
    slot = s;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    this.slot = buf.readInt();
  }
  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(slot);
  }
  @Override
  public IMessage onMessage(PacketWarpButton message, MessageContext ctx) {
    EntityPlayer player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
    int cost = (int) ItemEnderBook.getExpCostPerTeleport(player, ItemEnderBook.getPlayersBook(player), message.slot);
    if (player.isCreative()) {
      ItemEnderBook.teleport(player, message.slot);
    }
    else if (cost > 0 && UtilExperience.getExpTotal(player) < cost) {
      UtilChat.addChatMessage(player, "gui.chatexp");
    }
    else if (ItemEnderBook.teleport(player, message.slot)) {
      //if the teleport worked in non creative, drain it
      UtilExperience.drainExp(player, cost);
    }
    return null;
  }
}
