package com.lothrazar.cyclicmagic.component.peat.farm;
import com.lothrazar.cyclicmagic.component.hydrator.TileEntityHydrator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotItemRestricted;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPeatFarm extends ContainerBaseMachine {
  static final int SLOTY_FLUID = 39;
  static final int SLOTX_START = 8;
  static final int MID_SPACING = 133;
  public static final int SLOTY = 30;
  public ContainerPeatFarm(InventoryPlayer inventoryPlayer, TileEntityPeatFarm te) {
    this.setTile(te);
    int slotNum = 0;
    for (int i = 0; i < 2; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          SLOTX_START + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    for (int i = 0; i < 2; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          MID_SPACING + 1 + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tile.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tile);
  }
  //  @Override
  //  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
  //    
  //  }
}
