package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class StoneEntity extends ProjectileItemEntity {

  public StoneEntity(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public StoneEntity(LivingEntity livingEntityIn, World worldIn) {
    super(EntityRegistry.stone_bolt, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.COBBLESTONE;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      Entity owner = func_234616_v_();
      if (target.isAlive()) {
        target.attackEntityFrom(DamageSource.causeThrownDamage(this, owner), MathHelper.nextInt(world.rand, 2, 6));
        // 50% chance to stun for 2sec TODO: config
        if (target.world.rand.nextDouble() < 0.5F && !target.world.isRemote && target instanceof LivingEntity) {
          LivingEntity living = (LivingEntity) target;
          EffectInstance effect = new EffectInstance(PotionRegistry.PotionEffects.stun, Const.TICKS_PER_SEC * 2, 1);
          effect.showParticles = false;
          living.addPotionEffect(effect);
        }
      }
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
