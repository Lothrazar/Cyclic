package com.lothrazar.cyclic.item.endereye;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EyeOfEnderEntityNodrop extends EyeOfEnder {

  public EyeOfEnderEntityNodrop(EntityType<? extends EyeOfEnder> t, Level w) {
    super(t, w);
  }

  public EyeOfEnderEntityNodrop(Level worldIn, double posX, double d, double posZ) {
    super(worldIn, posX, d, posZ);
  }

  @Override
  public ItemStack getItem() {
    return ItemStack.EMPTY;
  }
}
