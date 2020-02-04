package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BoomerangEntityStun extends BoomerangEntity {

  public BoomerangEntityStun(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
    super(type, worldIn);
    boomerangType = Boomer.STUN;
  }

  public BoomerangEntityStun(World worldIn, LivingEntity throwerIn) {
    super(EntityRegistry.boomerang_stun, throwerIn, worldIn);
    boomerangType = Boomer.STUN;
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.boomerang_stun;
  }
}
