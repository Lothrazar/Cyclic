package com.lothrazar.cyclicmagic.component.playerextensions;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncExtendedInventory implements IMessage, IMessageHandler<PacketSyncExtendedInventory, IMessage> {
  int slot;
  int playerId;
  ItemStack itemStack = null;
  public PacketSyncExtendedInventory() {}
  public PacketSyncExtendedInventory(EntityPlayer player, int slot) {
    this.slot = slot;
    this.itemStack = UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, slot);
    this.playerId = player.getEntityId();
  }
  @Override
  public void toBytes(ByteBuf buffer) {
    buffer.writeByte(slot);
    buffer.writeInt(playerId);
    ByteBufUtils.writeItemStack(buffer, itemStack);
  }
  @Override
  public void fromBytes(ByteBuf buffer) {
    slot = buffer.readByte();
    playerId = buffer.readInt();
    itemStack = ByteBufUtils.readItemStack(buffer);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public IMessage onMessage(final PacketSyncExtendedInventory message, MessageContext ctx) {
    Minecraft.getMinecraft().addScheduledTask(new Runnable() {
      public void run() {
        processMessage(message);
      }
    });
    return null;
  }
  @SideOnly(Side.CLIENT)
  void processMessage(PacketSyncExtendedInventory message) {
    World world = ModCyclic.proxy.getClientWorld();
    if (world == null)
      return;
    Entity p = world.getEntityByID(message.playerId);
    if (p != null && p instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) p;
      UtilPlayerInventoryFilestorage.setPlayerInventoryStack(player, slot, message.itemStack);
      //      UtilPlayerInventoryFilestorage.getPlayerInventory(player).stackList[message.slot] = message.itemStack;
    }
    return;
  }
}
