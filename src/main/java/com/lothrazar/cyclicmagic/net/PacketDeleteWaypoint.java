package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.item.ItemEnderBook;
import com.lothrazar.cyclicmagic.util.UtilSound;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDeleteWaypoint implements IMessage, IMessageHandler<PacketDeleteWaypoint, IMessage> {
  public int slot;
  public PacketDeleteWaypoint() {
  }
  public PacketDeleteWaypoint(int s) {
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
  public IMessage onMessage(PacketDeleteWaypoint message, MessageContext ctx) {
    EntityPlayer player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
    ItemEnderBook.deleteWaypoint(player, message.slot);
    // http://minecraft.gamepedia.com/Sounds.json
    UtilSound.playSound(player, player.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS);
    // player.playSound("mob.endermen.portal", 1, 1);
    return null;
  }
}
