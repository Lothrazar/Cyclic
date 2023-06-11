package com.lothrazar.cyclic.item.boomerang;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.registry.PotionEffectRegistry;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class BoomerangEntity extends ThrowableItemProjectile {

  public BoomerangEntity(EntityType<? extends ThrowableItemProjectile> type, Level worldIn) {
    super(type, worldIn);
  }

  public BoomerangEntity(EntityType<? extends ThrowableItemProjectile> type, LivingEntity et, Level worldIn) {
    super(type, et, worldIn);
  }

  @Override
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(IS_RETURNING, (byte) 0);
    this.entityData.define(REDSTONE_TRIGGERED, (byte) 0);
    this.entityData.define(OWNER, "");
  }

  private static final int STUN_SECONDS = 7;
  private static final int TICKS_UNTIL_RETURN = 15;
  private static final int TICKS_UNTIL_DEATH = 900;
  private static final double SPEED = 0.95;
  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 3.8F;
  private static final EntityDataAccessor<Byte> IS_RETURNING = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.BYTE);
  private static final EntityDataAccessor<Byte> REDSTONE_TRIGGERED = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.BYTE);
  private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.STRING);
  private ItemStack boomerangThrown = ItemStack.EMPTY;
  private Player targetEntity;
  protected Boomer boomerangType;

  public void setBoomerangThrown(ItemStack boomerangThrown) {
    this.boomerangThrown = boomerangThrown;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag tag) {
    tag.putString("OWNER", entityData.get(OWNER));
    tag.putByte("returning", entityData.get(IS_RETURNING));
    tag.putByte("REDSTONE_TRIGGERED", entityData.get(REDSTONE_TRIGGERED));
    boomerangThrown.save(tag);
    super.addAdditionalSaveData(tag);
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    entityData.set(OWNER, tag.getString("OWNER"));
    entityData.set(IS_RETURNING, tag.getByte("returning"));
    entityData.set(REDSTONE_TRIGGERED, tag.getByte("REDSTONE_TRIGGERED"));
    boomerangThrown = ItemStack.of(tag);
    super.readAdditionalSaveData(tag);
  }

  public void setOwner(Player ent) {
    this.targetEntity = ent;
    if (ent != null && ent.getUUID() != null) {
      entityData.set(OWNER, ent.getUUID().toString());
    }
  }

  private void movementReturnCheck() {
    boolean returning = entityData.get(IS_RETURNING) == 1;
    if (returning) {
      //reverse direction 
      if (this.targetEntity != null) {
        this.moveTowardsTarget();
      }
    }
  }

  private void moveTowardsTarget() {
    float newyRot = (float) Math.toRadians(EntityUtil.yawDegreesBetweenPoints(getX(), getY(), getZ(), targetEntity.getX(), targetEntity.getY(), targetEntity.getZ()));
    float newxRot = (float) Math.toRadians(EntityUtil.pitchDegreesBetweenPoints(getX(), getY(), getZ(), targetEntity.getX(), targetEntity.getY(), targetEntity.getZ()));
    this.setYRot(newyRot);
    this.setXRot(newxRot);
    Vec3 moveVec = EntityUtil.lookVector(this.getYRot(), this.getXRot()).scale(SPEED);
    this.setDeltaMovement(
        0.5f * this.getDeltaMovement().x() + 0.5f * moveVec.x,
        0.5f * this.getDeltaMovement().y() + 0.503f * moveVec.y,
        0.5f * this.getDeltaMovement().z() + 0.5f * moveVec.z);
  }

  private void tryPickupNearby() {
    Entity owner = getOwner();
    if (owner == null || level().isClientSide) {
      return;
    }
    //try to find entities to pick up
    int range = 1;
    List<Entity> list = level().getEntities(this,
        this.getBoundingBox().move(this.getDeltaMovement()).inflate(range, range, range));
    for (Entity entityHit : list) {
      if (entityHit instanceof ItemEntity || entityHit instanceof ExperienceOrb) {
        entityHit.teleportTo(owner.getX(), owner.getY(), owner.getZ());
      }
    }
  }

  @SuppressWarnings("deprecation")
  private void tryToggleRedstone(final BlockPos pos) {
    Entity owner = getOwner();
    if (owner instanceof Player) {
      try {
        BlockState blockState = level().getBlockState(pos);
        Block block = blockState.getBlock();
        InteractionResult t = block.use(blockState, level(), pos, (Player) owner, InteractionHand.MAIN_HAND, null);
        boolean hasTriggered = t == InteractionResult.SUCCESS; //block.onBlockActivated(world, pos, blockState,
        //            (PlayerEntity) this.owner, Hand.MAIN_HAND, Direction.UP, 0.5F, 0.5F, 0.5F);
        if (hasTriggered) {
          this.setRedstoneHasTriggered();
        }
      }
      catch (Throwable e) {
        //since activated can hit any block, be safe
        ModCyclic.LOGGER.error("Error on activate block", e);
      }
    }
  }

  private void setIsReturning() {
    entityData.set(IS_RETURNING, (byte) 1);
  }

  private void setRedstoneHasTriggered() {
    this.entityData.set(REDSTONE_TRIGGERED, (byte) 1);
  }

  private boolean hasTriggeredRedstoneAlready() {
    return this.entityData.get(REDSTONE_TRIGGERED) > 0;
  }

  /**
   * Drops any nonempty boomerangThrown stack and empties it, then calls remove() on self.
   */
  private void dropAsItem() {
    if (!boomerangThrown.isEmpty()) {
      Entity owner = getOwner();
      //we have something to drop
      if (owner instanceof Player) {
        Player pl = (Player) owner;
        //try to give it to the player the nicest way possible 
        if (pl.getMainHandItem().isEmpty()) {
          pl.setItemInHand(InteractionHand.MAIN_HAND, boomerangThrown);
          boomerangThrown = ItemStack.EMPTY;
        }
        else {
          owner.spawnAtLocation(boomerangThrown, 0.5F);
          boomerangThrown = ItemStack.EMPTY;
        }
      }
      else {
        ItemStackUtil.drop(level(), this.blockPosition().above(), boomerangThrown);
        boomerangThrown = ItemStack.EMPTY;
      }
    }
    this.remove(RemovalReason.DISCARDED);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.tickCount > TICKS_UNTIL_DEATH) {
      dropAsItem();
      return;
    }
    if (this.tickCount > TICKS_UNTIL_RETURN) {
      setIsReturning();
    }
    final BlockPos pos = this.blockPosition();
    Entity owner = getOwner();
    if (owner != null && LevelWorldUtil.distanceBetweenHorizontal(pos, owner.blockPosition()) < 1) {
      dropAsItem();
      return;
    }
    if (hasTriggeredRedstoneAlready() == false && level().isEmptyBlock(pos) == false) {
      tryToggleRedstone(pos);
    }
    tryPickupNearby();
    movementReturnCheck();
  }

  @Override
  protected void onHit(HitResult result) {
    switch (result.getType()) {
      case BLOCK:
        onImpactBlock((BlockHitResult) result);
      break;
      case ENTITY:
        onImpactEntity((EntityHitResult) result);
      break;
      case MISS:
      default:
      break;
    }
  }

  private void onImpactBlock(BlockHitResult mop) {
    BlockState block = level().getBlockState(mop.getBlockPos());
    //crops are 0.0, farmland 0.6, leaves are 0.2  etc
    //    if (this.canHarvest(block, mop.getPos(), mop.getFace())) {
    //      if (this.owner instanceof PlayerEntity) {
    //        PlayerEntity p = (PlayerEntity) owner;
    //        //  block.getBlock().harvestBlock(world, p, mop.getBlockPos(), block, null, boomerangThrown);
    //        block.getBlock().removedByPlayer(block, world, mop.getPos(), p, true, null);
    //      }
    //      //still break regardless of harvest 
    //      world.destroyBlock(mop.getPos(), false);
    //    }
    if (mop.getDirection() != Direction.UP
        && block.isFaceSturdy(this.getCommandSenderWorld(), mop.getBlockPos(), mop.getDirection())) {
      //ok return 
      this.setIsReturning();
    }
  }

  private void onImpactEntity(EntityHitResult entityRayTrace) {
    Entity entityHit = entityRayTrace.getEntity();
    if (entityHit == null || !entityHit.isAlive()) {
      return;
    }
    Entity owner = getOwner();
    if (owner == entityHit) {
      //player catch it on return 
      dropAsItem();
      return;
    }
    switch (this.boomerangType) {
      case CARRY:
        if (!entityHit.level().isClientSide) {
          entityHit.startRiding(this);
        }
      break;
      case DAMAGE:
        if (entityHit instanceof LivingEntity) {
          LivingEntity live = (LivingEntity) entityHit;
          float damage = Mth.nextFloat(level().random, DAMAGE_MIN, DAMAGE_MAX);
          boolean attackSucc = live.hurt(level().damageSources().thrown(this, owner), damage);
          if (attackSucc && live.isAlive() == false) {
            //           ("killed one");
          }
        }
      break;
      case STUN:
        //!entityHit.getUniqueID().equals(owner.getUniqueID()) 
        if (entityHit != owner && entityHit instanceof LivingEntity
            && !(entityHit instanceof Player)) {
          LivingEntity live = (LivingEntity) entityHit;
          if (live.hasEffect(PotionEffectRegistry.STUN.get()) == false) {
            live.addEffect(new MobEffectInstance(PotionEffectRegistry.STUN.get(), STUN_SECONDS * 20, 1));
            SoundUtil.playSound(live, SoundEvents.IRON_GOLEM_ATTACK);
          }
        }
      break;
      default:
      break;
    }
  }

  @Override
  protected float getGravity() {
    return -1 * 0.02F;
  }

  @Override
  protected Item getDefaultItem() {
    return null;
  }

  @Override
  public Packet<ClientGamePacketListener> getAddEntityPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
