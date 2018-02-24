/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.component.workbench;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * FOR @runwyld https://www.twitch.tv/runwyld
 * 
 * @author Sam Lothrazar
 *
 */
public class ContainerWorkBench extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  protected TileEntityWorkbench tileEntity;
  private InventoryWorkbench craftMatrix;
  private InventoryCraftResult craftResult = new InventoryCraftResult();
  private World world;
  private final EntityPlayer player;
  public ContainerWorkBench(InventoryPlayer inventoryPlayer, TileEntityWorkbench te) {
    craftMatrix = new InventoryWorkbench(this, te);
    this.world = inventoryPlayer.player.world;
    this.player = inventoryPlayer.player;
    this.setTile(te);
    tileEntity = te;
    this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 136, 35));
    int slot = 0;
    //inpt on left
    int xPrefix = Const.PAD, yPrefix = 27;
    int rows = TileEntityWorkbench.ROWS;
    int cols = TileEntityWorkbench.COLS;
    //crafting in the middle
    rows = cols = 3;
    xPrefix = (screenSize.width() / 2 - (Const.SQ * 3) / 2);//(GuiWorkbench.WIDTH / 2 - (Const.SQ * 3) / 2);
    yPrefix = 20;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        addSlotToContainer(new Slot(this.craftMatrix, slot,
            xPrefix + j * Const.SQ,
            yPrefix + i * Const.SQ));
        slot++;
      }
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
    this.onCraftMatrixChanged(this.craftMatrix);
  }
  @Override
  public void onCraftMatrixChanged(IInventory inventory) {
    //i have to assume the recipe will safely validate itself
    //cant validate myself unless i restrict to vanilla-like recipes
    try {
      this.slotChangedCraftingGrid(this.world, player, this.craftMatrix, this.craftResult);
    }
    catch (Exception e) {
      //if ingredients to not satisfy recipe, it should just silently do nothing and not craft
      //but from another mod there could be an error bubbling up to here
      ModCyclic.logger.info("A recipe has thrown an error unexpectedly");
    }
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = ItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    //slot 0 is crafting output
    // [1,9] is the matrix
    //[10,36] is player
    //[37,45] hotbar
    //getSizeInventory is only 9 though, because output stack is not part of the size
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      if (slot <= 9) {
        if (!this.mergeItemStack(stackInSlot, 10, 46, true)) {
          return ItemStack.EMPTY;
        }
      }
      //Move up into crafting grid
      else if (!this.mergeItemStack(stackInSlot, 1, 10, false)) {
        return ItemStack.EMPTY;
      }
      if (stackInSlot.getCount() == 0) {
        slotObject.putStack(ItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) {
        return ItemStack.EMPTY;
      }
      slotObject.onTake(player, stackInSlot);
    }
    return stack;
  }
}
