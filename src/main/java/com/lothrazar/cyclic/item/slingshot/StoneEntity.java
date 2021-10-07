package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class StoneEntity extends ThrowableItemProjectile {

  public StoneEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public StoneEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.stone_bolt, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.COBBLESTONE;
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      Entity owner = getOwner();
      if (target.isAlive()) {
        target.hurt(DamageSource.thrown(this, owner), Mth.nextInt(level.random, 2, 6));
        // 50% chance to stun for 2sec TODO: config
        if (target.level.random.nextDouble() < 0.5F && !target.level.isClientSide && target instanceof LivingEntity) {
          LivingEntity living = (LivingEntity) target;
          MobEffectInstance effect = new MobEffectInstance(PotionRegistry.PotionEffects.stun, Const.TICKS_PER_SEC * 2, 1);
          effect.visible = false;
          living.addEffect(effect);
        }
      }
    }
    this.remove();
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    super.addAdditionalSaveData(tag);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
