package com.lothrazar.cyclicmagic.component.disenchanter;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.SlotItemRestricted;
import com.lothrazar.cyclicmagic.gui.SlotOnlyEnchanted;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDisenchanter extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_INPUT = 0;
  public static final int SLOTX_BOTTLE = 1;
  public static final int SLOTX_REDSTONE = 2;
  public static final int SLOTX_GLOWSTONE = 3;
  public static final int SLOTX_BOOK = 4;
  public static final int SLOTY_INPUT = 0;
  public static final int SLOTY_BOTTLE = 1;
  public static final int SLOTY_REDSTONE = 2;
  public static final int SLOTY_GLOWSTONE = 3;
  public static final int SLOTY_BOOK = 4;
  protected TileEntityDisenchanter tileEntity;

  public ContainerDisenchanter(InventoryPlayer inventoryPlayer, TileEntityDisenchanter te) {
    tileEntity = te;
    this.setTile(te);
    this.playerOffsetY = 130;
    Item itemFiltered = null;
    int x = 0, y = 0, ystart = 20, spacing = 26;
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      switch (i) {
        case TileEntityDisenchanter.SLOT_BOOK://center center
          itemFiltered = Items.BOOK;
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_GLOWSTONE://left mid
          itemFiltered = Items.GLOWSTONE_DUST;
          x = GuiDisenchanter.WIDTH / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_BOTTLE://bottom center
          itemFiltered = Items.EXPERIENCE_BOTTLE;
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart + 2 * spacing;
        break;
        case TileEntityDisenchanter.SLOT_REDSTONE:// right mid
          itemFiltered = Items.REDSTONE;
          x = GuiDisenchanter.WIDTH - GuiDisenchanter.WIDTH / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_INPUT://top center
          itemFiltered = Items.ENCHANTED_BOOK;//not reallyjust the book
          x = GuiDisenchanter.WIDTH / 2;
          y = ystart;
        break;
        default:
          itemFiltered = null;
          x = Const.PAD + (i - 5) * Const.SQ;
          y = ystart + 3 * spacing - 1;
        break;
      }
      if (itemFiltered == null) {
        addSlotToContainer(new Slot(tileEntity, i, x, y));
      }
      else if (itemFiltered == Items.ENCHANTED_BOOK) {
        addSlotToContainer(new SlotOnlyEnchanted(tileEntity, i, x, y));
      }
      else {
        addSlotToContainer(new SlotItemRestricted(tileEntity, i, x, y, itemFiltered));
      }
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tileEntity.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return ItemStack.EMPTY; }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return ItemStack.EMPTY; }
      if (stackInSlot.getCount() == 0) {
        slotObject.putStack(ItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) { return ItemStack.EMPTY; }
      slotObject.onTake(player, stackInSlot);
    }
    return stack;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tileEntity.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tileEntity);
  }
}
