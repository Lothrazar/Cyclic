package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BoomerangEntityCarry extends BoomerangEntity {

  public BoomerangEntityCarry(EntityType<? extends ThrowableItemProjectile> type, Level worldIn) {
    super(type, worldIn);
    boomerangType = Boomer.CARRY;
  }

  public BoomerangEntityCarry(Level worldIn, LivingEntity throwerIn) {
    super(EntityRegistry.boomerang_carry, throwerIn, worldIn);
    boomerangType = Boomer.CARRY;
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.boomerang_carry;
  }
}
