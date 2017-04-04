package com.lothrazar.cyclicmagic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHasClickToggle {
  public void toggle(EntityPlayer player, ItemStack held);
  public boolean isOn(ItemStack held);
}
