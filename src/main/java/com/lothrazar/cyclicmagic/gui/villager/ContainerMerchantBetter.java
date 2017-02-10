package com.lothrazar.cyclicmagic.gui.villager;
import java.lang.reflect.Field;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketSyncVillager;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilReflection;
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
/*
 WEIRD RIGHT so    return this.merchant.getProfessionForge().getCareer(maybeC).getName();
 //tells us in code that 3 is fletcher, wat?just.. what?
0 CONSTRUCTOR  client_farmer
3 CONSTRUCTOR  Server_fletcher
2 CONSTRUCTOR  client_shepherd
  * 
  * 
Clothing  Profession  ID  Career  ID
Brown Robe  Farmer  0 
Farmer  1
Fisherman 2
Shepherd  3 //apparently 
Fletcher  4
White Robe  Librarian 1 
Librarian 1
Cartographer  2
Purple Robe Priest  2 
Cleric  1
Black Apron Blacksmith  3 
Armorer 1
Weapon Smith  2
Tool Smith  3
White Apron Butcher 4 
Butcher 1
Leatherworker 2
Green Robe  Nitwit  5 
Nitwit  1
 */
public class ContainerMerchantBetter extends Container {
  /** Instance of Merchant. */
  public final EntityVillager merchant;
  private final InventoryMerchant merchantInventory;
  /** Instance of World. */
  private final World theWorld;
//  public int syncCareer = -1;
  EntityPlayer player;
 public ContainerMerchantBetter(InventoryPlayer playerInventory, EntityVillager merchant, InventoryMerchant im, World worldIn, List<EntityVillager> all) {
    this.merchant = merchant;
    this.theWorld = worldIn;
    this.merchantInventory = im;
    player = playerInventory.player;
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
    int career = UtilEntity.getVillagerCareer(merchant);// getCareer();
    String sc = (this.theWorld.isRemote) ? "client" : "Server";
    ModCyclic.logger.info(career + " CTR CONSTRUCTOR  " + sc + "_" + 
    UtilEntity.getCareerName(merchant));
    //    player.openContainer=this;
     this.detectAndSendChanges();
     

  }

  public void setCareer(int c) {
    System.out.println("setCareer  : " + c);
    //hopefully only client side
    int cOld = this.getCareer();
    if (cOld == c) { return; }
    //http://export.mcpbot.bspk.rs/snapshot/1.10.2/ "snapshot_20161111"
    if (ReflectionRegistry.fieldCareer != null) {
      try {
        int BEFORE = getCareer();
        System.out.println("BEFORE set: " + BEFORE);
        ReflectionRegistry.fieldCareer.set(merchant, c);
        int test = ReflectionRegistry.fieldCareer.getInt(merchant);
        System.out.println("after set: " + test);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {
      System.out.println("reflection fail");
    }
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

    if (player instanceof EntityPlayerMP 
        && player.openContainer instanceof ContainerMerchantBetter){
    ModCyclic.network.sendTo(new PacketSyncVillager(this.getCareer()), (EntityPlayerMP) player);
    }
  /*
    //    System.out.println("isRemote "+ this.theWorld.isRemote);//false ALWAYS fkn server eh
    if (!this.theWorld.isRemote) {
      //      System.out.println(this.getCareer() + "!!!!!!!!sendProgressBarUpdate");
      for (int i = 0; i < this.listeners.size(); ++i) {
        IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
        if (this.syncCareer != this.getCareer()) {
          icontainerlistener.sendProgressBarUpdate(this, 0, this.getCareer());
          // 
        }
      }
      //    System.out.println(this.syncCareer + "  this.syncCareer");
      this.syncCareer = this.getCareer();
      
    }*/
  }
  private int getCareer() {
    return UtilEntity.getVillagerCareer(merchant);
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
//    this.syncCareer = data;
//    this.setCareer(data);
  }
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.merchant.getCustomer() == playerIn;
  }
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
