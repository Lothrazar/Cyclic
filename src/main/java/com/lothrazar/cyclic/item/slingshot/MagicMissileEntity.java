package com.lothrazar.cyclic.item.slingshot;

import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class MagicMissileEntity extends ThrowableItemProjectile {

  private static final int MAX_LIFETIME = 120;
  private static final int TIME_UNTIL_HOMING = 8;
  private static final double SPEED = 0.95;
  private UUID targetId = null;
  private LivingEntity targetEntity;
  private int lifetime = MAX_LIFETIME;

  public MagicMissileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public MagicMissileEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.LASER_BOLT, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.NETHERITE_NUGGET.get();
  }

  public void setTarget(LivingEntity target) {
    targetId = target == null ? null : target.getUUID();
    targetEntity = target;
  }

  @Override
  public void tick() {
    super.tick();
    lifetime--;
    if (lifetime > MAX_LIFETIME - TIME_UNTIL_HOMING) {
      return; //keep normal path for first fiew
    }
    //ModCyclic.logger.error("UPDATE ET  isclient==" + this.world.isRemote);
    if (lifetime == 0 || targetId == null || targetEntity == null || !targetEntity.isAlive()) {
      this.kill();
      //no target found
      ModCyclic.LOGGER.info("Self kill no target found");
      return;
    }
    if (this.level.isClientSide &&
        (targetEntity == null || this.targetEntity.blockPosition().equals(this.blockPosition()))) {
      this.kill(); //setDead(); bandaid for client leftover
      return;
    }
    //+ target.getEyeHeight() / 2.0
    moveTowardsTarget();
  }

  private void moveTowardsTarget() {
    double posX = this.position().x;
    double posY = this.position().y;
    double posZ = this.position().z;
    this.setRot(TIME_UNTIL_HOMING, MAX_LIFETIME);
    // pitch is Y
    //yaw is X 
    float rotationYaw = (float) Math.toRadians(UtilEntity.yawDegreesBetweenPoints(posX, posY, posZ, targetEntity.position().x, targetEntity.position().y, targetEntity.position().z));
    float rotationPitch = (float) Math.toRadians(UtilEntity.pitchDegreesBetweenPoints(posX, posY, posZ, targetEntity.position().x, targetEntity.position().y, targetEntity.position().z));
    this.setRot(rotationYaw, rotationPitch);
    Vec3 moveVec = UtilEntity.lookVector(rotationYaw, rotationPitch).scale(SPEED);
    this.setDeltaMovement(moveVec);
  }

  @Override
  protected void onHit(HitResult result) {
    HitResult.Type type = result.getType();
    if (type == HitResult.Type.ENTITY) {
      //damage entity by zero 
      EntityHitResult entityRayTrace = (EntityHitResult) result;
      Entity target = entityRayTrace.getEntity();
      Entity owner = getOwner();
      if (target.isAlive()) {
        target.hurt(DamageSource.thrown(this, owner), Mth.nextInt(level.random, 1, 6));
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public Packet<?> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
