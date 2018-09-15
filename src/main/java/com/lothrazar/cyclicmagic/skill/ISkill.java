package com.lothrazar.cyclicmagic.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ISkill {

  public void toggle(EntityPlayer player);

  public boolean isEnabled(EntityPlayer player);

  public ItemStack getIcon();
}