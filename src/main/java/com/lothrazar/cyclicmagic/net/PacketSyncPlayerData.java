package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.ModCyclic;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncPlayerData implements IMessage, IMessageHandler<PacketSyncPlayerData, IMessage> {
  NBTTagCompound tags = new NBTTagCompound();
  public PacketSyncPlayerData() {}
  public PacketSyncPlayerData(NBTTagCompound ptags) {
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
  public IMessage onMessage(PacketSyncPlayerData message, MessageContext ctx) {
    if (ctx.side == Side.CLIENT) {
      //update it through client proxy
      PacketSyncPlayerData.checkThreadAndEnqueue(message, ctx);
    }
    return null;
  }
  /**
   * 1.8 +: Ensures that the message is being handled on the main thread
   * https://github.com/coolAlias/Tutorial-Demo/blob/master/src/main/java/
   * tutorial/network/AbstractMessage.java#L118-L131
   * http://www.minecraftforge.net/forum/index.php?topic=31853.0
   */
  private static final void checkThreadAndEnqueue(final PacketSyncPlayerData message, final MessageContext ctx) {
    IThreadListener thread = ModCyclic.proxy.getThreadFromContext(ctx);
    // pretty much copied straight from vanilla code, see {@link PacketThreadUtil#checkThreadAndEnqueue}
    thread.addScheduledTask(new Runnable() {
      public void run() {
        // msg.process(ModMain.proxy.getPlayerEntity(ctx), ctx.side);
        ModCyclic.proxy.setClientPlayerData(ctx, message.tags);
      }
    });
  }
}
