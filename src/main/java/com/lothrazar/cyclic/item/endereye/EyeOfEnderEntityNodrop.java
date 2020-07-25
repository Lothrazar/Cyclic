package com.lothrazar.cyclic.item.endereye;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EyeOfEnderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EyeOfEnderEntityNodrop extends EyeOfEnderEntity {

  public EyeOfEnderEntityNodrop(EntityType<? extends EyeOfEnderEntity> t, World w) {
    super(t, w);
  }

  public EyeOfEnderEntityNodrop(World worldIn, double posX, double d, double posZ) {
    super(worldIn, posX, d, posZ);
  }

  @Override
  public ItemStack getItem() {
    return ItemStack.EMPTY;
  }
}
