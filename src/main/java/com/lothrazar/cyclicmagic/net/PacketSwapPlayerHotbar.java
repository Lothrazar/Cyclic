package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.component.playerext.storage.InventoryPlayerExtended;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwapPlayerHotbar implements IMessage, IMessageHandler<PacketSwapPlayerHotbar, IMessage> {
  public PacketSwapPlayerHotbar() {}
  private int row;
  public PacketSwapPlayerHotbar(int isdown) {
    row = isdown;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    row = tags.getInteger("row");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("row", row);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSwapPlayerHotbar message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    int slotHotbar = 0, slotStorage = 0;
    ItemStack stackStorage, stackHotbar;
    for (int i = 0; i < Const.HOTBAR_SIZE; i++) {
      slotHotbar = i;
      slotStorage = i + message.row * InventoryPlayerExtended.ICOL;
      stackStorage = UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, slotStorage);
      stackHotbar = player.inventory.getStackInSlot(slotHotbar);
      UtilPlayerInventoryFilestorage.setPlayerInventoryStack(player, slotStorage, stackHotbar);
      player.inventory.setInventorySlotContents(slotHotbar, stackStorage);
    }
    return null;
  }
}
