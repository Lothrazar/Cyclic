package com.lothrazar.cyclicmagic.component.peat.generator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotItemRestricted;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPeatGenerator extends ContainerBaseMachine {
  public ContainerPeatGenerator(InventoryPlayer inventoryPlayer, TileEntityPeatGenerator te) {
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
    addSlotToContainer(new SlotItemRestricted(te, 0, 30, 30, Item.getByNameOrId(Const.MODRES + "peat_fuel")));
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
