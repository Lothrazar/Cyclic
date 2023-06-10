package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class StoneEntity extends ThrowableItemProjectile {

  private static final float CHANCE_STUN = 0.5F;

  public StoneEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public StoneEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.STONE_BOLT.get(), livingEntityIn, worldIn);
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
        target.hurt(level.damageSources().thrown(this, owner), Mth.nextInt(level.random, 2, 6));
        if (target.level.random.nextDouble() < CHANCE_STUN && !target.level.isClientSide && target instanceof LivingEntity) {
          LivingEntity living = (LivingEntity) target;
          MobEffectInstance effect = new MobEffectInstance(PotionEffectRegistry.STUN.get(), Const.TICKS_PER_SEC * 2, 1);
          effect.visible = false;
          living.addEffect(effect);
        }
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
