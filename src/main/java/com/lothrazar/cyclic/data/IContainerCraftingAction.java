package com.lothrazar.cyclic.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;

public interface IContainerCraftingAction {

  ItemStack transferStack(PlayerEntity playerIn, int index);

  CraftingInventory getCraftMatrix();

  CraftResultInventory getCraftResult();
}
