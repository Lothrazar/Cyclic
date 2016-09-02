package com.lothrazar.cyclicmagic.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCharmBoat extends BaseCharm {
  

  public ItemCharmBoat() {
    super(64);
  }

  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
   * update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
  {
    //TODO 1: stop drowning
    //TODO 2: boat speed if possible
  }
}
