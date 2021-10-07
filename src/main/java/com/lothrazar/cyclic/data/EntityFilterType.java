package com.lothrazar.cyclic.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public enum EntityFilterType {

  //living DOES include players
  LIVING, PLAYERS, MOB, MONSTER, ANIMAL, TAMEABLE, FLYING, WATER, AMBIENT;

  public List<? extends LivingEntity> getEntities(Level world, BlockPos pos, int radius) {
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    AABB axisalignedbb = (new AABB(x, y, z, x + 1, y + 1, z + 1)).inflate(radius).expandTowards(0.0D, world.getMaxBuildHeight(), 0.0D);
    //
    return getEntities(world, axisalignedbb);
  }

  public List<? extends LivingEntity> getEntities(Level world, AABB axisalignedbb) {
    List<LivingEntity> list = new ArrayList<>();
    switch (this) {
      case AMBIENT:
        list.addAll(world.getEntitiesOfClass(AmbientCreature.class, axisalignedbb));
      break;
      case ANIMAL:
        list.addAll(world.getEntitiesOfClass(Animal.class, axisalignedbb));
      break;
      case FLYING:
        list.addAll(world.getEntitiesOfClass(FlyingMob.class, axisalignedbb));
      break;
      case LIVING:
        list.addAll(world.getEntitiesOfClass(LivingEntity.class, axisalignedbb));
      break;
      case MOB:
        list.addAll(world.getEntitiesOfClass(Mob.class, axisalignedbb));
      break;
      case PLAYERS:
        list.addAll(world.getEntitiesOfClass(Player.class, axisalignedbb));
      break;
      case TAMEABLE:
        list.addAll(world.getEntitiesOfClass(TamableAnimal.class, axisalignedbb));
      break;
      case WATER:
        list.addAll(world.getEntitiesOfClass(WaterAnimal.class, axisalignedbb));
      break;
      case MONSTER:
        list.addAll(world.getEntitiesOfClass(Monster.class, axisalignedbb));
      break;
    }
    return list;
  }
}
