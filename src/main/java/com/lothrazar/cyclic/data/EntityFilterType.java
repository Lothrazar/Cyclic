package com.lothrazar.cyclic.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum EntityFilterType {

  //living DOES include players
  LIVING, PLAYERS, MOB, MONSTER, ANIMAL, TAMEABLE, FLYING, WATER, AMBIENT;

  public List<? extends LivingEntity> getEntities(World world, BlockPos pos, int radius) {
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    AxisAlignedBB axisalignedbb = (new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1)).grow(radius).expand(0.0D, world.getHeight(), 0.0D);
    //
    return getEntities(world, axisalignedbb);
  }

  public List<? extends LivingEntity> getEntities(World world, AxisAlignedBB axisalignedbb) {
    List<LivingEntity> list = new ArrayList<>();
    switch (this) {
      case AMBIENT:
        list.addAll(world.getEntitiesWithinAABB(AmbientEntity.class, axisalignedbb));
      break;
      case ANIMAL:
        list.addAll(world.getEntitiesWithinAABB(AnimalEntity.class, axisalignedbb));
      break;
      case FLYING:
        list.addAll(world.getEntitiesWithinAABB(FlyingEntity.class, axisalignedbb));
      break;
      case LIVING:
        list.addAll(world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb));
      break;
      case MOB:
        list.addAll(world.getEntitiesWithinAABB(MobEntity.class, axisalignedbb));
      break;
      case PLAYERS:
        list.addAll(world.getEntitiesWithinAABB(PlayerEntity.class, axisalignedbb));
      break;
      case TAMEABLE:
        list.addAll(world.getEntitiesWithinAABB(TameableEntity.class, axisalignedbb));
      break;
      case WATER:
        list.addAll(world.getEntitiesWithinAABB(WaterMobEntity.class, axisalignedbb));
      break;
      case MONSTER:
        list.addAll(world.getEntitiesWithinAABB(MonsterEntity.class, axisalignedbb));
      break;
    }
    return list;
  }
}
