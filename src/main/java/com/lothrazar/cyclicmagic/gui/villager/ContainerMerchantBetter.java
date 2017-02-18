package com.lothrazar.cyclicmagic.gui.villager;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketSyncVillagerToClient;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class ContainerMerchantBetter extends Container {
  public final EntityVillager merchant;
  private MerchantRecipeList trades;
  private final InventoryMerchantBetter merchantInventory;
  private final World theWorld;
  EntityPlayer player;
  public ContainerMerchantBetter(InventoryPlayer playerInventory, EntityVillager m, InventoryMerchantBetter im, World worldIn, List<EntityVillager> all) {
    this.merchant = m;
    this.theWorld = worldIn;
    this.merchantInventory = im;
    player = playerInventory.player;
    trades = merchant.getRecipes(player);
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
    this.detectAndSendChanges();
  }
  public void setCareer(int c) {
    //hopefully only client side
    int cOld = this.getCareer();
    if (cOld == c) { return; }
    //http://export.mcpbot.bspk.rs/snapshot/1.10.2/ "snapshot_20161111"
    if (ReflectionRegistry.fieldCareer != null) {
      try {
        //        int BEFORE = getCareer();
        ReflectionRegistry.fieldCareer.set(merchant, c);
        //        int test = ReflectionRegistry.fieldCareer.getInt(merchant);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {
      ModCyclic.logger.error("reflection fail");
    }
  }
  public InventoryMerchantBetter getMerchantInventory() {
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
        && player.openContainer instanceof ContainerMerchantBetter) {
      MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(player);
      EntityPlayerMP mp = (EntityPlayerMP) player;
      ModCyclic.network.sendTo(new PacketSyncVillagerToClient(this.getCareer(), merchantrecipelist), mp);
    }
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
  public boolean canInteractWith(EntityPlayer playerIn) {
    return this.merchant.getCustomer() == playerIn;
  }
  final static int SLOT_OUTPUT = 2;
  final static int SLOT_INPUT = 0;
  final static int SLOT_INPUTX = 1;
  final static int HOTBAR_START = 30;
  final static int HOTBAR_END = 38;
  final static int INV_START = 3;
  final static int INV_END = 29;
  @Nullable
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = null;
    Slot slot = (Slot) this.inventorySlots.get(index);
    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      if (index == SLOT_OUTPUT) {
        if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true)) { return null; }
        slot.onSlotChange(itemstack1, itemstack);
      }
      else if (index != SLOT_INPUT && index != SLOT_INPUTX) {  //so it must be a player slot

        if (!this.mergeItemStack(itemstack1, SLOT_INPUT, SLOT_INPUTX + 1, false)) { return null; }
      }
      else {//so it is 0,1
        if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, false)) { return null; }
      }
      //cleanup steps
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
  public void setTrades(MerchantRecipeList t) {
    this.trades = t;
    this.merchant.setRecipes(t);
    this.merchantInventory.setRecipes(t);
  }
  public MerchantRecipeList getTrades() {
    return trades;
  }
}
