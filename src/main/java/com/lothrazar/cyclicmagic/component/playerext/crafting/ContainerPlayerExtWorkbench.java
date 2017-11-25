package com.lothrazar.cyclicmagic.component.playerext.crafting;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPlayerExtWorkbench extends ContainerBase {
  public InventoryPlayerExtWorkbench craftMatrix;
  private final EntityPlayer thePlayer;
  private static final EntityEquipmentSlot[] ARMOR = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
  public static final int SLOT_SHIELD = 40;
  public static final int SQ = 18;
  public static final int VROW = 3;
  public static final int VCOL = 9;
  public static final int HOTBAR_SIZE = 9;
  public IInventory craftResult = new InventoryCraftResult();
  final int pad = 8;
  public ContainerPlayerExtWorkbench(InventoryPlayer playerInv, EntityPlayer player) {
    this.thePlayer = player;
    craftMatrix = new InventoryPlayerExtWorkbench(this, player);
    int slotId = 0;
    int xResult = 155, yResult = 22;
    this.addSlotToContainer(new SlotCrafting(player, craftMatrix, craftResult, slotId, xResult, yResult));
    for (int k = 0; k < ARMOR.length; k++) {
      final EntityEquipmentSlot slot = ARMOR[k];
      slotId = 4 * VCOL + (VROW - k);
      this.addSlotToContainer(new Slot(playerInv, slotId, pad, pad + k * SQ) {
        @Override
        public int getSlotStackLimit() {
          return 1;
        }
        @Override
        public boolean isItemValid(ItemStack stack) {
          if (stack.isEmpty()) {
            return false;
          }
          else {
            return stack.getItem().isValidArmor(stack, slot, thePlayer);
          }
        }
        @Override
        @SideOnly(Side.CLIENT)
        public String getSlotTexture() {
          return ItemArmor.EMPTY_SLOT_NAMES[slot.getIndex()];
        }
      });
    }
    slotId++;
    this.addSlotToContainer(new Slot(playerInv, 40, 77, 62) {
      public boolean isItemValid(@Nullable ItemStack stack) {
        return super.isItemValid(stack);
      }
      @Nullable
      @SideOnly(Side.CLIENT)
      public String getSlotTexture() {
        return "minecraft:items/empty_armor_slot_shield";
      }
    });
    //the 3x3
    int xPos, yPos;
    for (int i = 0; i < InventoryPlayerExtWorkbench.IROW; ++i) {
      for (int j = 0; j < InventoryPlayerExtWorkbench.ICOL; ++j) {
        xPos = (j + 1) * SQ + 65;
        yPos = i * SQ + 6;
        slotId = j + (i) * InventoryPlayerExtWorkbench.ICOL;
        this.addSlotToContainer(new Slot(craftMatrix, slotId, xPos, yPos));
      }
    }
    for (int i = 0; i < VROW; ++i) {
      for (int j = 0; j < VCOL; ++j) {
        xPos = pad + j * SQ;
        yPos = 84 + i * SQ;
        slotId = j + (i + 1) * HOTBAR_SIZE;
        this.addSlotToContainer(new Slot(playerInv, slotId, xPos, yPos));
      }
    }
    yPos = 142;
    for (int i = 0; i < HOTBAR_SIZE; ++i) {
      xPos = pad + i * SQ;
      slotId = i;
      this.addSlotToContainer(new Slot(playerInv, slotId, xPos, yPos));
    }
    this.onCraftMatrixChanged(craftMatrix);
  }
  @Override
  public void onCraftMatrixChanged(IInventory inventory) {
    IRecipe r = CraftingManager.findMatchingRecipe(craftMatrix, this.thePlayer.getEntityWorld());
    if (r == null) {
      craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
    }
    else {
      craftResult.setInventorySlotContents(0, r.getRecipeOutput().copy());
    }
  }
  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
    super.onContainerClosed(playerIn);
    //size of 9, but it starts after the five equip slots
    for (int i = 0; i < 9; ++i) {
      ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
      if (!itemstack.isEmpty()) {
        playerIn.dropItem(itemstack, false);
      }
    }
    this.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex) {
    ItemStack itemStack = ItemStack.EMPTY;
    Slot fromSlot = (Slot) this.inventorySlots.get(slotIndex);
    //shield is slot 5 now
    int craftOutpt = 0, playerStart = 15, playerEnd = 50, craftStart = 6, craftEnd = 14, armorStart = 1, armorEnd = 5;
    if (fromSlot != null && fromSlot.getHasStack()) {
      ItemStack itemStack1 = fromSlot.getStack();
      itemStack = itemStack1.copy();
      if (slotIndex == craftOutpt) {
        if (!this.mergeItemStack(itemStack1, playerStart, playerEnd + 1, false)) {
          return ItemStack.EMPTY;
        }
        fromSlot.onSlotChange(itemStack1, itemStack);
      }
      else if (slotIndex >= craftStart && slotIndex <= craftEnd) {
        if (!this.mergeItemStack(itemStack1, playerStart, playerEnd + 1, false)) {
          fromSlot.onSlotChanged();
          return ItemStack.EMPTY;
        }
      }
      else if (slotIndex >= armorStart && slotIndex <= armorEnd) {
        if (!this.mergeItemStack(itemStack1, playerStart, playerEnd + 1, false)) {
          fromSlot.onSlotChanged();
          return ItemStack.EMPTY;
        }
      }
      else if (slotIndex >= playerStart && slotIndex <= playerEnd) {
        if (!this.mergeItemStack(itemStack1, craftStart, craftEnd + 1, false)) {
          return ItemStack.EMPTY;
        }
      }
      if (itemStack1.getCount() == 0) {
        fromSlot.putStack(ItemStack.EMPTY);
      }
      else {
        fromSlot.onSlotChanged();
      }
      if (itemStack.getCount() == itemStack1.getCount()) {
        return ItemStack.EMPTY;
      }
      fromSlot.onTake(this.thePlayer, itemStack1);
    }
    return itemStack;
  }
}
