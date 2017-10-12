package com.lothrazar.cyclicmagic.component.playerext.crafting;
import java.lang.ref.WeakReference;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryPlayerExtWorkbench extends InventoryCrafting {
  protected NonNullList<ItemStack> inv;
  private ContainerPlayerExtWorkbench eventHandler;
  public WeakReference<EntityPlayer> player;
  public static final int IROW = 3;
  public static final int ICOL = 3;
  public InventoryPlayerExtWorkbench(ContainerPlayerExtWorkbench containerPlayerExtWorkbench, EntityPlayer player) {
    super(containerPlayerExtWorkbench, 3, 3);
    this.eventHandler = containerPlayerExtWorkbench;
    inv = NonNullList.withSize(IROW * ICOL + 5, ItemStack.EMPTY);//5 armor + 3x3
    this.player = new WeakReference<EntityPlayer>(player);
  }
  @Override
  public int getSizeInventory() {
    return this.inv.size();
  }
  @Override
  public ITextComponent getDisplayName() {
    ITextComponent name = super.getDisplayName();
    if (name == null) {
      return new TextComponentTranslation("cyclic.inventory.crafting");
    }
    return name;
  }
  @Override
  public ItemStack getStackInSlot(int s) {
    return s >= this.getSizeInventory() ? null : this.inv.get(s);
  }
  /**
   * When some containers are closed they call this on each slot, then drop
   * whatever it returns as an EntityItem - like when you close a workbench GUI.
   */
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    setInventorySlotContents(index, ItemStack.EMPTY);
    return stack;
  }
  /**
   * Removes from an inventory slot (first arg) up to a specified number (second
   * arg) of items and returns them in a new stack.
   */
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.getMaxStackSize() <= count) {
        setInventorySlotContents(index, ItemStack.EMPTY);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.getMaxStackSize() == 0) {
          setInventorySlotContents(index, ItemStack.EMPTY);
        }
      }
    }
    this.eventHandler.onCraftMatrixChanged(this);
    return stack;
  }
  /**
   * Sets the given item stack to the specified slot in the inventory (can be
   * crafting or armor sections).
   */
  @Override
  public void setInventorySlotContents(int idx, ItemStack stack) {
    if (idx >= this.inv.size()) {
      return;
    }
    if (stack == null) {
      stack = ItemStack.EMPTY;
    }
    inv.set(idx, stack);
    this.eventHandler.onCraftMatrixChanged(this);
  }
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public void markDirty() {
    super.markDirty();
    try {
      player.get().inventory.markDirty();
    }
    catch (Exception e) {}
  }
  @Override
  public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer) {
    return true;
  }
  @Override
  public void clear() {
    for (int i = 0; i < inv.size(); i++) {
      inv.set(i, ItemStack.EMPTY);
    }
  }
  public void saveNBT(EntityPlayer player) {
    NBTTagCompound tags = player.getEntityData();
    saveNBT(tags);
  }
  public void saveNBT(NBTTagCompound tags) {
    NBTTagList tagList = new NBTTagList();
    NBTTagCompound invSlot;
    for (int i = 0; i < this.inv.size(); ++i) {
      if (!this.inv.get(i).isEmpty()) {
        invSlot = new NBTTagCompound();
        invSlot.setByte("Slot", (byte) i);
        this.inv.get(i).writeToNBT(invSlot);
        tagList.appendTag(invSlot);
      }
    }
    tags.setTag(Const.MODID + ".Inventory", tagList);
  }
  public void readNBT(EntityPlayer player) {
    NBTTagCompound tags = player.getEntityData();
    readNBT(tags);
  }
  public void readNBT(NBTTagCompound tags) {
    NBTTagList tagList = tags.getTagList(Const.MODID + ".Inventory", 10);
    for (int i = 0; i < tagList.tagCount(); ++i) {
      NBTTagCompound nbttagcompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
      int j = nbttagcompound.getByte("Slot") & 255;
      ItemStack itemstack = UtilNBT.itemFromNBT(nbttagcompound);
      if (itemstack != null) {
        this.inv.set(j, itemstack);
      }
    }
  }
}
