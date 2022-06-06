package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class DarkFireEntity extends ThrowableItemProjectile {

  public DarkFireEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public DarkFireEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.DARKFIRE_BOLT, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.FIREBALL_DARK.get();
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive()) {
        target.hurt(DamageSource.thrown(this, this.getOwner()), Mth.nextInt(level.random, 4, 8));
        if (!target.level.isClientSide && target.isOnFire() == false
            && target instanceof LivingEntity living) {
          living.hurt(DamageSource.MAGIC, Mth.nextInt(level.random, 3, 5));
          living.addEffect(new MobEffectInstance(MobEffects.WITHER, Const.TICKS_PER_SEC * 5, 1));
        }
      }
    }
    else if (type == HitResult.Type.BLOCK) {
      BlockHitResult ray = (BlockHitResult) result;
      if (ray.getBlockPos() == null) {
        return;
      }
      final BlockPos pos = ray.getBlockPos();
      final Block blockHere = this.level.getBlockState(pos).getBlock();
      if (blockHere == Blocks.SNOW
          || blockHere == Blocks.SNOW_BLOCK
          || blockHere == Blocks.SNOW
          || blockHere == Blocks.ICE) {
        this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      }
    }
    this.remove(RemovalReason.DISCARDED);
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
