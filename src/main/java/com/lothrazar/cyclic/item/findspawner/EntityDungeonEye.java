package com.lothrazar.cyclic.item.findspawner;

import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDungeonEye extends ProjectileItemEntity {

  public EntityDungeonEye(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public EntityDungeonEye(LivingEntity livingEntityIn, World worldIn) {
    super(EntityRegistry.dungeon, livingEntityIn, worldIn);
  }

  private static final double DISTLIMIT = 0.8;
  private static final double VERT = 0.014999999664723873D;
  private static final double HORIZ = 0.0025D;
  private double targetX;
  private double targetY;
  private double targetZ;
  private boolean isLost = true;

  @Override
  public void writeAdditional(CompoundNBT compound) {
    compound.putDouble("sp_target_x", targetX);
    compound.putDouble("sp_target_y", targetY);
    compound.putDouble("sp_target_z", targetZ);
    compound.putInt("ticksExisted", ticksExisted);
    compound.putBoolean("isLost", isLost);
    super.writeAdditional(compound);
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    targetX = compound.getDouble("sp_target_x");
    targetY = compound.getDouble("sp_target_y");
    targetZ = compound.getDouble("sp_target_z");
    ticksExisted = compound.getInt("ticksExisted");
    isLost = compound.getBoolean("isLost");
  }

  public void moveTowards(BlockPos pos) {
    this.targetX = pos.getX();
    this.targetY = pos.getY();
    this.targetZ = pos.getZ();
    this.isLost = false;
    this.shoot(this.targetX, this.targetY, this.targetZ, (this.getGravityVelocity()), 0.01F);
  }

  @Override
  public void tick() {
    if (isLost) {
      //when thread is done, it will make me unlost, or remove me
      return;
    }
    //  UtilParticle.spawnParticle(world, ParticleTypes.DRAGON_BREATH, this.getPosition(), 1);
    if (!this.world.isRemote) {
      double posX = this.getPosX();
      double posY = this.getPosY();
      double posZ = this.getPosZ();
      this.lastTickPosX = posX;
      this.lastTickPosY = posY;
      this.lastTickPosZ = posZ;
      double motionX = this.getMotion().x;
      double motionY = this.getMotion().y;
      double motionZ = this.getMotion().z;
      posX += motionX;
      posY += motionY;
      posZ += motionZ;
      this.setPosition(posX, posY, posZ);
      float f = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
      double distX = Math.abs(this.targetX - posX);
      double distY = Math.abs(this.targetY - posY);
      double distZ = Math.abs(this.targetZ - posZ);
      float distance = (float) Math.sqrt(distX * distX + distZ * distZ);
      float distLine = (float) Math.sqrt(distX * distX + distZ * distZ + distY * distY);
      float atan = (float) MathHelper.atan2(this.targetZ - posZ, this.targetX - posX);
      double horizFactor = f + (distance - f) * HORIZ;
      if (distLine < 1.0F) {
        horizFactor *= 0.8D;
        //        this.motionY *= 0.8D; 
        this.remove();
      }
      motionX = Math.cos(atan) * horizFactor;
      motionZ = Math.sin(atan) * horizFactor;
      motionY = (14 * distY) / distLine * VERT;
      this.setMotion(motionX, motionY, motionZ);
      if (distX < DISTLIMIT && distZ < DISTLIMIT) {//if we are right in line, stop swaggerin
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
      if (this.ticksExisted < 20) {
        speedHReduction = 2.2;
        speedVReduction = 12.2;
      }
      if (this.ticksExisted < 40) {
        speedHReduction = 1.8;
        speedVReduction = 10;
      }
      else if (this.ticksExisted < 100) {
        speedHReduction = 1.2;
        speedVReduction = 6;
      }
      else if (this.ticksExisted < 150) {
        speedHReduction = 1.1;
        speedVReduction = 2;
      }
      else if (this.ticksExisted < 500) {
        speedHReduction = 1;
        speedVReduction = 1.1;
      }
      //else no reduction
      motionX /= speedHReduction;
      motionY /= speedVReduction;
      motionZ /= speedHReduction;
      if (this.ticksExisted > 9999) {
        this.remove();
      }
      if (motionX == 0 && motionY == 0 && motionZ == 0) {
        this.remove();
      }
      this.setMotion(motionX, motionY, motionZ);
      //      int particleCount = (this.ticksExisted < 100) ? 30 : 14;
      //      float f3 = 0.25F;
      //      for (int i = 0; i < particleCount; ++i) {
      //        this.getEntityWorld().spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * f3 - 0.5D,
      //            this.posZ - this.motionZ * f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
      //      }
    }
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.spawner_seeker;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    //    this.remove(); 
    //    System.out.println("onimpact" + result);
  }
}
