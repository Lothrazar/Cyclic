package com.lothrazar.cyclicmagic.net;
import com.lothrazar.cyclicmagic.component.playerext.storage.InventoryPlayerExtended;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilPlayerInventoryFilestorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwapPlayerHotbar implements IMessage, IMessageHandler<PacketSwapPlayerHotbar, IMessage> {
  public PacketSwapPlayerHotbar() {}
  private int row;
  private boolean armor = false;
  public PacketSwapPlayerHotbar(int isdown) {
    row = isdown;
  }
  public PacketSwapPlayerHotbar(boolean doArmor) {
    this.armor = doArmor;
  }
  @Override
  public void fromBytes(ByteBuf buf) {
    NBTTagCompound tags = ByteBufUtils.readTag(buf);
    row = tags.getInteger("row");
    armor = tags.getBoolean("armor");
  }
  @Override
  public void toBytes(ByteBuf buf) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger("row", row);
    tags.setBoolean("armor", armor);
    ByteBufUtils.writeTag(buf, tags);
  }
  @Override
  public IMessage onMessage(PacketSwapPlayerHotbar message, MessageContext ctx) {
    EntityPlayer player = ctx.getServerHandler().player;
    if (message.armor) {
      //vertically swap column with armor if possible 
      int[] playerSlots = new int[] { 36, 27, 18, 9 };
      int playerSlot;
      ItemStack onPlayer, onStorage;
      EntityEquipmentSlot[] ARMOR = new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD };
      for (int armorSlot = 0; armorSlot < player.inventory.armorInventory.size(); armorSlot++) {
        onPlayer = player.inventory.armorInventory.get(armorSlot).copy();
        playerSlot = playerSlots[armorSlot];
        onStorage = UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, playerSlot).copy();
        if (onStorage.getItem().isValidArmor(onStorage, ARMOR[armorSlot], player)) {
          //it can go here! send the copies and overwrite!
          player.inventory.armorInventory.set(armorSlot, onStorage);
          UtilPlayerInventoryFilestorage.setPlayerInventoryStack(player, playerSlot, onPlayer);
        }
      }
    }
    else {
      //hotbar with row transfer
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
    }
    return null;
  }
}
