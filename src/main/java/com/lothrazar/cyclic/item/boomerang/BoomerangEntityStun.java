package com.lothrazar.cyclic.item.boomerang;

import com.lothrazar.cyclic.CyclicRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BoomerangEntityStun extends BoomerangEntity {

  public BoomerangEntityStun(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
    super(type, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return CyclicRegistry.Items.boomerang_stun;
  }
}
