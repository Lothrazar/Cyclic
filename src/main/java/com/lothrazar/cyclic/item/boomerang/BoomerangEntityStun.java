package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BoomerangEntityStun extends BoomerangEntity {

  public BoomerangEntityStun(EntityType<? extends ThrowableItemProjectile> type, Level worldIn) {
    super(type, worldIn);
    boomerangType = Boomer.STUN;
  }

  public BoomerangEntityStun(Level worldIn, LivingEntity throwerIn) {
    super(EntityRegistry.BOOMERANG_STUN, throwerIn, worldIn);
    boomerangType = Boomer.STUN;
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.BOOMERANG_STUN.get();
  }
}
