package com.lothrazar.cyclicmagic;

import net.minecraft.item.ItemStack;

public interface ICanToggleOnOff {
  public void toggleOnOff(ItemStack held) ;
  public boolean isOn(ItemStack held);
}
