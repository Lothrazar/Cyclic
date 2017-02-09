package com.lothrazar.cyclicmagic.gui.villager;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketSyncVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerMerchantBetter extends Container {
  /** Instance of Merchant. */
  public final EntityVillager merchant;
  private final InventoryMerchant merchantInventory;
  /** Instance of World. */
  private final World theWorld;
  public int syncCareer=-1;
  EntityPlayer player;
  public ContainerMerchantBetter(InventoryPlayer playerInventory, EntityVillager merchant, InventoryMerchant im, World worldIn) {
    this.merchant = merchant;
    this.theWorld = worldIn;
    this.merchantInventory = im;
    player=playerInventory.player;
    this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
    this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
    this.addSlotToContainer(new SlotMerchantResult(playerInventory.player, merchant, this.merchantInventory, 2, 120, 53));
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }
    for (int k = 0; k < 9; ++k) {
      this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
    }
    
    int career = getCareer();
    ModCyclic.logger.info(career+" CONSTRUCTOR career");
//    if(playerInventory.player instanceof EntityPlayerMP){
//      ModCyclic.logger.info(career+" MP career Good");
//    ModCyclic.network.sendTo(new PacketSyncVillager(career), (EntityPlayerMP)playerInventory.player);
//    }
//    else{
//
//      ModCyclic.logger.info(career+" ?CLIENT career bad");
//    }
    this.detectAndSendChanges();
  }
  private int getCareer() {
    int career = this.merchant.serializeNBT().getInteger("Career");
    return career;
  }
  public void setCareer(int c){
    //hopefully only client side
    this.merchant.serializeNBT().setInteger("Career",c);
  }
  public InventoryMerchant getMerchantInventory() {
    return this.merchantInventory;
  }
  public void addListener(IContainerListener listener) {
    super.addListener(listener);  
    listener.sendAllWindowProperties(this, this.merchantInventory);

  }
  /**
   * Looks for changes made in the container, sends them to every listener.
   */
  public void detectAndSendChanges() {
    merchantInventory.markDirty();
    super.detectAndSendChanges();
    System.out.println("isRemote "+ this.theWorld.isRemote);//false ALWAYS fkn server eh
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);

      if (this.syncCareer != this.getCareer()) {
        System.out.println("!!!!!!!!sendProgressBarUpdate"+ this.getCareer());
        icontainerlistener.sendProgressBarUpdate(this, 0, this.getCareer());  
        
        if(player instanceof EntityPlayerMP)
  ModCyclic.network.sendTo(new PacketSyncVillager( this.getCareer()), (EntityPlayerMP)player);
     // 
      }
    }  

    System.out.println("  this.syncCareer"+  this.syncCareer);
//    this.syncCareer = this.getCareer();
    
  }
  /**
   * Callback for when the crafting matrix is changed.
   */
  public void onCraftMatrixChanged(IInventory inventoryIn) {
    this.merchantInventory.resetRecipeAndSlots();
    super.onCraftMatrixChanged(inventoryIn);
  }
  public void setCurrentRecipeIndex(int currentRecipeIndex) {
    this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
  }
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    System.out.println("update progressbar "+id+"::"+data);
    this.syncCareer = data;

    System.out.println("update progressbar so TODO: set that villager career ! ");
    this.setCareer(data);
  }
  /**
   * Determines whether supplied player can use this container
   */
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.merchant.getCustomer() == playerIn;
  }
  /**
   * Take a stack from the specified inventory slot.
   */
  @Nullable
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = null;
    Slot slot = (Slot) this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index == 2) {
        if (!this.mergeItemStack(itemstack1, 3, 39, true)) { return null; }
        slot.onSlotChange(itemstack1, itemstack);
      }
      else if (index != 0 && index != 1) {
        if (index >= 3 && index < 30) {
          if (!this.mergeItemStack(itemstack1, 30, 39, false)) { return null; }
        }
        else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) { return null; }
      }
      else if (!this.mergeItemStack(itemstack1, 3, 39, false)) { return null; }
      if (itemstack1.stackSize == 0) {
        slot.putStack((ItemStack) null);
      }
      else {
        slot.onSlotChanged();
      }
      if (itemstack1.stackSize == itemstack.stackSize) { return null; }
      slot.onPickupFromSlot(playerIn, itemstack1);
    }
    return itemstack;
  }
  /**
   * Called when the container is closed.
   */
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);
    this.merchant.setCustomer((EntityPlayer) null);
    super.onContainerClosed(playerIn);
    if (!this.theWorld.isRemote) {
      ItemStack itemstack = this.merchantInventory.removeStackFromSlot(0);
      if (itemstack != null) {
        playerIn.dropItem(itemstack, false);
      }
      itemstack = this.merchantInventory.removeStackFromSlot(1);
      if (itemstack != null) {
        playerIn.dropItem(itemstack, false);
      }
    }
  }
}
