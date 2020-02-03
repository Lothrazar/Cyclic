package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BoomerangEntityCarry extends BoomerangEntity {

  public BoomerangEntityCarry(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
    super(type, worldIn);
    boomerangType = Boomer.CARRY;
  }

  public BoomerangEntityCarry(World worldIn, LivingEntity throwerIn) {
    super(CyclicRegistry.Entities.boomerang_carry, throwerIn, worldIn);
    boomerangType = Boomer.CARRY;
  }

  @Override
  protected Item getDefaultItem() {
    return CyclicRegistry.Items.boomerang_carry;
  }
}
