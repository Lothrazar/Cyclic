package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;

public class SnowEntity extends ThrowableItemProjectile {

  public SnowEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public SnowEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.SNOW_BOLT.get(), livingEntityIn, worldIn, new ItemStack(Items.SNOWBALL));
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
      var level = level();
      if (target.isAlive() && target instanceof LivingEntity) {
        target.hurt(level.damageSources().thrown(this, this.getOwner()), Mth.nextInt(level.getRandom(), 2, 5));
        target.hurt(level.damageSources().dryOut(), Mth.nextInt(level.getRandom(), 2, 3));
        LivingEntity living = (LivingEntity) target;
        living.addEffect(new MobEffectInstance(PotionEffectRegistry.STUN, 60, 1, false, false, false));

      }
    }
    else if (type == HitResult.Type.BLOCK) {
      BlockHitResult ray = (BlockHitResult) result;
      if (ray.getBlockPos() == null || ray.getDirection() == null) {
        return;
      }
      // check the block directly hit (e.g. water surface)
      freezeIfWater(ray.getBlockPos());
      // projectiles clip through water since it has no collision shape, so also
      // check the face the projectile arrived from - that block is the water it was traveling through
      freezeIfWater(ray.getBlockPos().relative(ray.getDirection()));
    }
    this.remove(RemovalReason.DISCARDED);
  }

  private void freezeIfWater(BlockPos pos) {
    BlockState state = level().getBlockState(pos);
    if (state.is(Blocks.WATER) && state.getValue(LiquidBlock.LEVEL) == 0) {
      level().setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
    }
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
