package com.lothrazar.cyclic.data;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public interface IContainerCraftingAction {

  ItemStack transferStack(Player playerIn, int index);

  CraftingContainer getCraftMatrix();

  ResultContainer getCraftResult();
}
