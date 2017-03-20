package com.lothrazar.cyclicmagic.gui.storage;
import com.lothrazar.cyclicmagic.gui.InventoryBase;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class InventoryStorage extends InventoryBase implements IInventory {
  public static final int INV_SIZE = 66; //6*11
  //  private ItemStack[] inv = new ItemStack[INV_SIZE];
  private final ItemStack internalWand;
  private EntityPlayer thePlayer;
  public InventoryStorage(EntityPlayer player, ItemStack wand) {
    super(INV_SIZE);
    internalWand = wand;
    inv = readFromNBT(internalWand);
    thePlayer = player;
  }
  public EntityPlayer getPlayer() {
    return thePlayer;
  }
  @Override
  public String getName() {
    return "";
  }
  @Override
  public boolean hasCustomName() {
    return false;
  }
  @Override
  public ITextComponent getDisplayName() {
    return null;
  }
  @Override
  public int getSizeInventory() {
    return INV_SIZE;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv.get(index);
  }
  @Override
  public ItemStack decrStackSize(int slot, int amount) {
    ItemStack stack = getStackInSlot(slot);
    if (stack != null) {
      if (stack.getCount() > amount) {
        stack = stack.splitStack(amount);
        // Don't forget this line or your inventory will not be saved!
        markDirty();
      }
      else {
        // this method also calls onInventoryChanged, so we don't need
        // to call it again
        setInventorySlotContents(slot, null);
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    // used to be 'getStackInSlotOnClosing'
    ItemStack stack = getStackInSlot(index);
    setInventorySlotContents(index, null);
    return stack;
  }
  @Override
  public void setInventorySlotContents(int slot, ItemStack stack) {
    if (stack == null) {
      stack = ItemStack.EMPTY;
    }
    inv.set(slot, stack);
    if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
      stack.setCount(getInventoryStackLimit());
    }
    markDirty();
  }
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public void markDirty() {
    for (int i = 0; i < getSizeInventory(); ++i) {
      if (getStackInSlot(i) != ItemStack.EMPTY && getStackInSlot(i).getCount() == 0) {
        inv.set(i, ItemStack.EMPTY);
      }
    }
    // set any empty item stacks (red zeroes) to empty
    for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++) {
      if (thePlayer.inventory.getStackInSlot(i) != ItemStack.EMPTY && thePlayer.inventory.getStackInSlot(i).getCount() == 0) {
        thePlayer.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
      }
    }
    writeToNBT(internalWand, inv);
  }
  @Override
  public boolean isUsableByPlayer(EntityPlayer player) {
    return true;
  }
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }
  /************** public static ******************/
  public static int countNonEmpty(ItemStack stack) {
    NonNullList<ItemStack> inv = readFromNBT(stack);
    int count = 0;
    for (int i = 0; i < inv.size(); ++i) {
      if (!inv.get(i).isEmpty()) {
        count++;
      }
    }
    return count;
  }
  public static NonNullList<ItemStack> readFromNBT(ItemStack stack) {
    NonNullList<ItemStack> inv = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);
    if (stack == ItemStack.EMPTY) { return inv; }
    NBTTagList items = UtilNBT.getItemStackNBT(stack).getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < items.tagCount(); ++i) {
      // 1.7.2+ change to items.getCompoundTagAt(i)
      NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
      int slot = item.getInteger("Slot");
      if (slot >= 0 && slot < INV_SIZE) {
        inv.set(slot, UtilNBT.itemFromNBT(item));
      }
    }
    return inv;
  }
  public static void writeToNBT(ItemStack item, NonNullList<ItemStack> theInventory) {
    NBTTagCompound tagcompound = UtilNBT.getItemStackNBT(item);
    // Create a new NBT Tag List to store itemstacks as NBT Tags
    NBTTagList items = new NBTTagList();
    ItemStack stack;
    for (int i = 0; i < theInventory.size(); ++i) {
      stack = theInventory.get(i);
      if (stack != ItemStack.EMPTY && stack.getCount() == 0) {
        stack = ItemStack.EMPTY;
      }
      if (stack != ItemStack.EMPTY) {
        // Make a new NBT Tag Compound to write the itemstack and slot
        // index to
        NBTTagCompound itemTags = new NBTTagCompound();
        itemTags.setInteger("Slot", i);
        // Writes the itemstack in slot(i) to the Tag Compound we just
        // made
        stack.writeToNBT(itemTags);
        // add the tag compound to our tag list
        items.appendTag(itemTags);
      }
    }
    // Add the TagList to the ItemStack's Tag Compound with the name
    // "ItemInventory"
    tagcompound.setTag("ItemInventory", items);
  }
  public static void decrementSlot(ItemStack stack, int itemSlot) {
    NonNullList<ItemStack> invv = InventoryStorage.readFromNBT(stack);
    invv.get(itemSlot).shrink(1);
    //    invv[itemSlot].setCount(invv[itemSlot].getCount()-1);
    //    invv[itemSlot].stackSize--;
    if (invv.get(itemSlot).getCount() == 0) {
      invv.set(itemSlot, ItemStack.EMPTY);
    }
    InventoryStorage.writeToNBT(stack, invv);
  }
  public static ItemStack getFromSlot(ItemStack stack, int i) {
    if (i < 0 || i >= InventoryStorage.INV_SIZE) { return ItemStack.EMPTY; }
    return InventoryStorage.readFromNBT(stack).get(i);
  }
}
