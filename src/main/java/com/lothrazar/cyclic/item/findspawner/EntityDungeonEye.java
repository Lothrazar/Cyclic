package com.lothrazar.cyclic.item.findspawner;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class EntityDungeonEye extends ThrowableItemProjectile {

  public EntityDungeonEye(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public EntityDungeonEye(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.DUNGEON, livingEntityIn, worldIn);
  }

  private static final double DISTLIMIT = 0.8;
  private static final double VERT = 0.014999999664723873D;
  private static final double HORIZ = 0.0025D;
  private double targetX;
  private double targetY;
  private double targetZ;
  private boolean isLost = true;

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    compound.putDouble("sp_target_x", targetX);
    compound.putDouble("sp_target_y", targetY);
    compound.putDouble("sp_target_z", targetZ);
    compound.putInt("ticksExisted", tickCount);
    compound.putBoolean("isLost", isLost);
    super.addAdditionalSaveData(compound);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag compound) {
    super.readAdditionalSaveData(compound);
    targetX = compound.getDouble("sp_target_x");
    targetY = compound.getDouble("sp_target_y");
    targetZ = compound.getDouble("sp_target_z");
    tickCount = compound.getInt("ticksExisted");
    isLost = compound.getBoolean("isLost");
  }

  public void moveTowards(BlockPos pos) {
    this.targetX = pos.getX();
    this.targetY = pos.getY();
    this.targetZ = pos.getZ();
    this.isLost = false;
    this.shoot(this.targetX, this.targetY, this.targetZ, (this.getGravity()), 0.01F);
  }

  @Override
  public void tick() {
    if (isLost) {
      //when thread is done, it will make me unlost, or remove me
      return;
    }
    //  UtilParticle.spawnParticle(world, ParticleTypes.DRAGON_BREATH, this.getPosition(), 1);
    if (!this.level.isClientSide) {
      double posX = this.getX();
      double posY = this.getY();
      double posZ = this.getZ();
      this.xOld = posX;
      this.yOld = posY;
      this.zOld = posZ;
      double motionX = this.getDeltaMovement().x;
      double motionY = this.getDeltaMovement().y;
      double motionZ = this.getDeltaMovement().z;
      posX += motionX;
      posY += motionY;
      posZ += motionZ;
      this.setPos(posX, posY, posZ);
      double f = Math.sqrt(motionX * motionX + motionZ * motionZ);
      double distX = Math.abs(this.targetX - posX);
      double distY = Math.abs(this.targetY - posY);
      double distZ = Math.abs(this.targetZ - posZ);
      float distance = (float) Math.sqrt(distX * distX + distZ * distZ);
      float distLine = (float) Math.sqrt(distX * distX + distZ * distZ + distY * distY);
      float atan = (float) Mth.atan2(this.targetZ - posZ, this.targetX - posX);
      double horizFactor = f + (distance - f) * HORIZ;
      if (distLine < 1.0F) {
        horizFactor *= 0.8D;
        //        this.motionY *= 0.8D; 
        this.remove(RemovalReason.DISCARDED);
      }
      motionX = Math.cos(atan) * horizFactor;
      motionZ = Math.sin(atan) * horizFactor;
      motionY = (14 * distY) / distLine * VERT;
      this.setDeltaMovement(motionX, motionY, motionZ);
      if (distX < DISTLIMIT && distZ < DISTLIMIT) { //if we are right in line, stop swaggerin
        motionX = 0;
        motionZ = 0;
        if (distY < DISTLIMIT) {
          motionY = 0;
        }
      }
      if (posY < this.targetY) {
        // make sure motion is going up
        if (motionY < 0) {
          motionY *= -1;
        }
      }
      else {
        // make sure motion is going DOWN
        if (motionY > 0) {
          motionY *= -1;
        }
      }
      double speedHReduction = 1;
      double speedVReduction = 1;
      if (this.tickCount < 20) {
        speedHReduction = 2.2;
        speedVReduction = 12.2;
      }
      if (this.tickCount < 40) {
        speedHReduction = 1.8;
        speedVReduction = 10;
      }
      else if (this.tickCount < 100) {
        speedHReduction = 1.2;
        speedVReduction = 6;
      }
      else if (this.tickCount < 150) {
        speedHReduction = 1.1;
        speedVReduction = 2;
      }
      else if (this.tickCount < 500) {
        speedHReduction = 1;
        speedVReduction = 1.1;
      }
      //else no reduction
      motionX /= speedHReduction;
      motionY /= speedVReduction;
      motionZ /= speedHReduction;
      if (this.tickCount > 9999) {
        this.remove(RemovalReason.DISCARDED);
      }
      if (motionX == 0 && motionY == 0 && motionZ == 0) {
        this.remove(RemovalReason.DISCARDED);
      }
      this.setDeltaMovement(motionX, motionY, motionZ);
      //      int particleCount = (this.ticksExisted < 100) ? 30 : 14;
      //      float f3 = 0.25F;
      //      for (int i = 0; i < particleCount; ++i) {
      //        this.getEntityWorld().spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * f3 - 0.5D,
      //            this.posZ - this.motionZ * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
      //      }
    }
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.spawner_seeker;
  }

  @Override
  protected void onHit(HitResult result) {
    //    this.remove(); 
    //     .println("onimpact" + result);
  }
}
