package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
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
    super(EntityRegistry.boomerang_carry, throwerIn, worldIn);
    boomerangType = Boomer.CARRY;
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.boomerang_carry;
  }
}
