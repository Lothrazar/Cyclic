package com.lothrazar.cyclicmagic.net;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Override / fix for vanilla minecraft using an old server-> client sync system that truncates Integers to Shorts so anything under Short.MAX_VALUE aka 32767 wont work
 * 
 * Look at net.minecraft.inventory.IContainerListener
 * 
 * and you will see
 * 
 * Quote start
 * 
 * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress, and enchanting level. Normally the first int identifies which variable to update,
 * and the second contains the new value. Both are truncated to shorts in non-local SMP.
 * 
 * Quote End
 * 
 * void sendWindowProperty(Container containerIn, int varToUpdate, int newValue);
 * 
 * @author Sam
 *
 */
public class PacketGuiShortOverride implements IMessage, IMessageHandler<PacketGuiShortOverride, IMessage> {
  public int fieldId, value;
  public PacketGuiShortOverride() {}
  public PacketGuiShortOverride(int f, int v) {
    this.fieldId = f;
    this.value = v;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    fieldId = buf.readInt();
    value = buf.readInt();
  }
  @Override
  public void toBytes(ByteBuf buf) {
    //not writeShort as the legacy vanilla system does
    buf.writeInt(fieldId);
    buf.writeInt(value);
  }
  @Override
  public IMessage onMessage(PacketGuiShortOverride message, MessageContext ctx) {
    final IThreadListener mainThread = Minecraft.getMinecraft();
    mainThread.addScheduledTask(new Runnable() {
      @Override
      public void run() {
        if (Minecraft.getMinecraft().player.openContainer != null) {
          Minecraft.getMinecraft().player.openContainer.updateProgressBar(message.fieldId, message.value);
        }
      }
    });
    return null;
  }
}
