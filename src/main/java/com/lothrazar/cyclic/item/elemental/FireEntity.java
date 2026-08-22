package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class FireEntity extends ThrowableItemProjectile {

  public FireEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public FireEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.FIRE_BOLT.get(), livingEntityIn, worldIn, new ItemStack(ItemRegistry.FIREBALL_ORANGE.get()));
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.FIREBALL_ORANGE.get();
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      var level = level();
      if (target.isAlive()) {
        target.hurt(level.damageSources().thrown(this, this.getOwner()), Mth.nextInt(level.getRandom(), 2, 6));
        if (!level.isClientSide() && target.isOnFire() == false
            && target instanceof LivingEntity) {
          target.hurt(level.damageSources().inFire(), Mth.nextInt(level.getRandom(), 3, 5));
          LivingEntity living = (LivingEntity) target;
          living.addEffect(new MobEffectInstance(PotionEffectRegistry.STUN, Const.TICKS_PER_SEC * 4, 1, false, false, false));
          living.igniteForSeconds(Mth.nextInt(level.getRandom(), 1, 5));
        }
      }
    }
    else if (type == HitResult.Type.BLOCK) {
      BlockHitResult ray = (BlockHitResult) result;
      //  set fire in the area here, small radius randomized, similar to lightning strike or tnt
      var level = this.level();
      if (!level.isClientSide() && ray.getBlockPos() != null) {
        final int radius = 2;
        final BlockPos center = ray.getBlockPos().relative(ray.getDirection());
        final int attempts = Mth.nextInt(level.getRandom(), 3, 6); //range similar to lightning default
        for (int i = 0; i < attempts; i++) {
          BlockPos firePos = center.offset(
              Mth.nextInt(level.getRandom(), -radius, radius),
              Mth.nextInt(level.getRandom(), -1, 1),
              Mth.nextInt(level.getRandom(), -radius, radius));
          if (level.isEmptyBlock(firePos) && BaseFireBlock.canBePlacedAt(level, firePos, ray.getDirection())) {
            level.setBlockAndUpdate(firePos, BaseFireBlock.getState(level, firePos));
          }
        }
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);
  }

  @Override
  public void readAdditionalSaveData(ValueInput input) {
    super.readAdditionalSaveData(input);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
    return new ClientboundAddEntityPacket(this, serverEntity);
  }
}
