package com.lothrazar.cyclicmagic.component.beaconpotion;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotSingleStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBeaconPotion extends ContainerBaseMachine {
  public ContainerBeaconPotion(InventoryPlayer inventoryPlayer, TileEntityBeaconPotion te) {
    this.setTile(te);
    int x = 176 / 2 - Const.SQ / 2;
    int y = 38;
    this.addSlotToContainer(new SlotSingleStack(tile, 0, x, y) {
      @Override
      public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemPotion;
      }
    });
    super.addFurnaceFuelSlot(SLOTX_FUEL, SLOTY_FUEL);
    super.bindPlayerInventory(inventoryPlayer);
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
}
