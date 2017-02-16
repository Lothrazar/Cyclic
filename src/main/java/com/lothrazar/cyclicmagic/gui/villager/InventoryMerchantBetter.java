package com.lothrazar.cyclicmagic.gui.villager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchantBetter extends InventoryMerchant implements IInventory {
  private final IMerchant theMerchant;
  private final ItemStack[] theInventory = new ItemStack[3];
  private final EntityPlayer thePlayer;
  private MerchantRecipe currentRecipe;
  private int currentRecipeIndex;
  private MerchantRecipeList trades;
  public InventoryMerchantBetter(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
    super(thePlayerIn, theMerchantIn);
    System.out.println(thePlayerIn.worldObj.isRemote + ":InventoryMerchantBetter");
    this.thePlayer = thePlayerIn;
    this.theMerchant = theMerchantIn;
    trades = this.theMerchant.getRecipes(this.thePlayer);
  }
  /**
   * Returns the number of slots in the inventory.
   */
  public int getSizeInventory() {
    return this.theInventory.length;
  }
  /**
   * Returns the stack in the given slot.
   */
  @Nullable
  public ItemStack getStackInSlot(int index) {
    return this.theInventory[index];
  }
  /**
   * Removes up to a specified number of items from an inventory slot and
   * returns them in a new stack.
   */
  @Nullable
  public ItemStack decrStackSize(int index, int count) {
    if (index == 2 && this.theInventory[index] != null) {
      System.out.println("decrStackSize " + this.theInventory[index]);
      return ItemStackHelper.getAndSplit(this.theInventory, index, this.theInventory[index].stackSize);
    }
    else {
      ItemStack itemstack = ItemStackHelper.getAndSplit(this.theInventory, index, count);
      if (itemstack != null && this.inventoryResetNeededOnSlotChange(index)) {
        this.resetRecipeAndSlots();
      }
      return itemstack;
    }
  }
  /**
   * if par1 slot has changed, does resetRecipeAndSlots need to be called?
   */
  private boolean inventoryResetNeededOnSlotChange(int slotIn) {
    return slotIn == 0 || slotIn == 1;
  }
  /**
   * Removes a stack from the given slot and returns it.
   */
  @Nullable
  public ItemStack removeStackFromSlot(int index) {
    if (this.theInventory[index] != null)
      System.out.println("removeStackFromSlot " + this.theInventory[index]);
    return ItemStackHelper.getAndRemove(this.theInventory, index);
  }
  public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
    this.theInventory[index] = stack;
    if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
      stack.stackSize = this.getInventoryStackLimit();
    }
    if (this.inventoryResetNeededOnSlotChange(index)) {
      this.resetRecipeAndSlots();
    }
  }
  /**
   * Get the name of this object. For players this returns their username
   */
  public String getName() {
    return "mob.villager";
  }
  public boolean hasCustomName() {
    return false;
  }
  public ITextComponent getDisplayName() {
    return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
  }
  public int getInventoryStackLimit() {
    return 64;
  }
  /**
   * Don't rename this method to canInteractWith due to conflicts with Container
   */
  public boolean isUseableByPlayer(EntityPlayer player) {
    return this.theMerchant.getCustomer() == player;
  }
  public void openInventory(EntityPlayer player) {}
  public void closeInventory(EntityPlayer player) {}
  /**
   * Returns true if automation is allowed to insert the given stack (ignoring
   * stack size) into the given slot. For guis use Slot.isItemValid
   */
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }
  /**
   * For tile entities, ensures the chunk containing the tile entity is saved to
   * disk later - the game won't think it hasn't changed and skip it.
   */
  public void markDirty() {
    this.resetRecipeAndSlots();
  }
  public void setRecipes(MerchantRecipeList t) {
    if (t.size() != trades.size()) {
      ModCyclic.logger.info(t.size() + "[setRecipes] " + this.thePlayer.worldObj.isRemote);
      trades = t;
    }
    //     this.theMerchant.setRecipes(t);
  }
  public MerchantRecipeList getRecipes() {
    return trades;
    //return  this.theMerchant.getRecipes(this.thePlayer);
  }
  public void resetRecipeAndSlots() {
    boolean remote = this.thePlayer.getEntityWorld().isRemote;
    this.currentRecipe = null;
    ItemStack itemstack = this.theInventory[0];
    ItemStack itemstack1 = this.theInventory[1];
    if (itemstack == null) {
      itemstack = itemstack1;
      itemstack1 = null;
    }
    if (itemstack == null) {
      this.setInventorySlotContents(2, (ItemStack) null);
    }
    else {
      MerchantRecipeList merchantrecipelist = this.getRecipes();
      if (merchantrecipelist != null) {
        MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
          this.currentRecipe = merchantrecipe;
          ModCyclic.logger.info(this.currentRecipeIndex+" currentRecipe "+remote + this.currentRecipe.getItemToSell());
          this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
        }
        else if (itemstack1 != null) {
          merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
          if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
            ModCyclic.logger.info(this.currentRecipeIndex+" currentRecipe "+remote + this.currentRecipe.getItemToSell());
            this.currentRecipe = merchantrecipe;
            this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
          }
          else {
            this.setInventorySlotContents(2, (ItemStack) null);
          }
        }
        else {
          this.setInventorySlotContents(2, (ItemStack) null);
        }
      }
    }
    this.theMerchant.verifySellingItem(this.getStackInSlot(2));
  }
  public MerchantRecipe getCurrentRecipe() {
    return this.currentRecipe;
  }
  public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
    this.currentRecipeIndex = currentRecipeIndexIn;
    System.out.println(this.currentRecipeIndex+"[INV] setCurrentRecipeIndexx   "+this.thePlayer.getEntityWorld().isRemote);
    this.resetRecipeAndSlots();
  }
  public int getField(int id) {
    return 0;
  }
  public void setField(int id, int value) {}
  public int getFieldCount() {
    return 0;
  }
  public void clear() {
    for (int i = 0; i < this.theInventory.length; ++i) {
      this.theInventory[i] = null;
    }
  }
}
