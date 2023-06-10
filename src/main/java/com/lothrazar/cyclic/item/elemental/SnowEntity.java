package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class SnowEntity extends ThrowableItemProjectile {

  public SnowEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public SnowEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.SNOW_BOLT.get(), livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.SNOWBALL;
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero
      //drop torch
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target.isAlive() && target instanceof LivingEntity) {
        target.hurt(DamageSource.thrown(this, this.getOwner()), Mth.nextInt(level.random, 2, 5));
        target.hurt(DamageSource.DRY_OUT, Mth.nextInt(level.random, 2, 3));
        LivingEntity living = (LivingEntity) target;
        living.addEffect(new MobEffectInstance(PotionEffectRegistry.STUN.get(), 60, 1));
        //        if (world.isAirBlock(living.getPosition()))
        //          this.world.setBlockState(living.getPosition(), Blocks.SNOW.getDefaultState());
      }
    }
    else if (type == HitResult.Type.BLOCK) {
      BlockHitResult ray = (BlockHitResult) result;
      if (ray.getBlockPos() == null || ray.getDirection() == null) {
        return;
      }
      //      BlockPos pos = ray.getPos().offset(ray.getFace());
      //      if (world.isAirBlock(pos))
      //        this.world.setBlockState(pos, Blocks.SNOW.getDefaultState());
      //      else {
      //        BlockState here = world.getBlockState(ray.getPos());
      //        if (here.getBlock() == Blocks.SNOW) {
      //          //inc
      //          int newy = here.get(SnowBlock.LAYERS).intValue() + 1;
      //          world.setBlockState(ray.getPos(), here.with(SnowBlock.LAYERS, newy));
      //          //
      //        }
      //        here = world.getBlockState(pos);
      //        if (here.getBlock() == Blocks.WATER) {
      //          if (world.rand.nextDouble() < 0.25)
      //            world.setBlockState(pos, Blocks.BLUE_ICE.getDefaultState());
      //          else
      //            world.setBlockState(pos, Blocks.PACKED_ICE.getDefaultState());
      //        }
      //      }
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
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
