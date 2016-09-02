package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.util.UtilItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BaseCharm extends BaseItem{

  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }

  public void damageCharm(EntityPlayer living, ItemStack stack, int itemSlot) {
    UtilItem.damageItem(living, stack);
    if(stack == null || stack.getItemDamage() == stack.getMaxDamage()){
      living.inventory.setInventorySlotContents(itemSlot, null);
      //stack.stackSize = 0;
     // stack = null;
    }
  }
}
