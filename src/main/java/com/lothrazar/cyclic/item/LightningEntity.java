package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class LightningEntity extends ProjectileItemEntity {

  public LightningEntity(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public LightningEntity(LivingEntity livingEntityIn, World worldIn) {
    super(EntityRegistry.lightningbolt, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.GHAST_TEAR;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY) {
      //damage entity by zero
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive()) {
        target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
        LightningBoltEntity lightningboltentity = new LightningBoltEntity(world, target.getPosX(), target.getPosY(), target.getPosZ(), false);
        world.addEntity(lightningboltentity);
      }
    }
    else if (type == RayTraceResult.Type.BLOCK) {
      //      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      LightningBoltEntity lightningboltentity = new LightningBoltEntity(world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ(), false);
      world.addEntity(lightningboltentity);
    }
    this.remove();
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    super.writeAdditional(tag);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    super.readAdditional(tag);
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
