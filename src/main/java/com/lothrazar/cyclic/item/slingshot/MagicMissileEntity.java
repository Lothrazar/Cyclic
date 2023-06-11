package com.lothrazar.cyclic.item.slingshot;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.EntityUtil;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
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

  private static final int MAX_LIFETIME = 120000;
  private static final int TIME_UNTIL_HOMING = 4;
  private static final double SPEED = 0.95;
  private LivingEntity targetEntity;
  private int lifetime = MAX_LIFETIME;

  public MagicMissileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
    super(entityType, world);
  }

  public MagicMissileEntity(LivingEntity livingEntityIn, Level worldIn) {
    super(EntityRegistry.MAGIC_MISSILE.get(), livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.FIREBALL_ORANGE.get();
  }

  public void setTarget(LivingEntity target) {
    ModCyclic.LOGGER.info("Magic missile target found " + target);
    //    targetId = target == null ? null : target.getUUID();
    targetEntity = target;
  }

  @Override
  public void tick() {
    super.tick();
    lifetime--;
    var level = level();
    if (!level.isClientSide && lifetime <= 0) {
      this.kill();
      //no target found
      ModCyclic.LOGGER.info(" server side Self I took too long " + targetEntity);
      return;
    }
    //ModCyclic.logger.error("UPDATE ET  isclient==" + this.world.isRemote);
    if (!level.isClientSide &&
        (targetEntity == null || !targetEntity.isAlive())) {
      this.kill();
      //no target found
      ModCyclic.LOGGER.info(" - erase self dead entity  " + targetEntity);
      return;
    }
    moveTowardsTarget();
  }

  private void moveTowardsTarget() {
    if (this.targetEntity == null) {
      return;
    }
    if (targetEntity.blockPosition().equals(this.blockPosition())) {
      return; // equal pos
    }
    double posX = this.position().x;
    double posY = this.position().y;
    double posZ = this.position().z;
    this.setRot(TIME_UNTIL_HOMING, MAX_LIFETIME);
    // pitch is Y
    //yaw is X 
    float rotationYaw = (float) Math.toRadians(EntityUtil.yawDegreesBetweenPoints(posX, posY, posZ, targetEntity.position().x, targetEntity.position().y, targetEntity.position().z));
    float rotationPitch = (float) Math.toRadians(EntityUtil.pitchDegreesBetweenPoints(posX, posY, posZ, targetEntity.position().x, targetEntity.position().y, targetEntity.position().z));
    this.setRot(rotationYaw, rotationPitch);
    Vec3 moveVec = EntityUtil.lookVector(rotationYaw, rotationPitch).scale(SPEED);
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
        target.hurt(level().damageSources().thrown(this, owner), Mth.nextInt(level().random, 1, 6));
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
