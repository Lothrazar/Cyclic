package com.lothrazar.cyclic.item.boomerang;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BoomerangEntity extends ProjectileItemEntity {

  public BoomerangEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
    super(type, worldIn);
  }

  public BoomerangEntity(EntityType<BoomerangEntity> type, LivingEntity throwerIn, World worldIn) {
    super(type, throwerIn, worldIn);
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(IS_RETURNING, (byte) 0);
    this.dataManager.register(REDSTONE_TRIGGERED, (byte) 0);
    this.dataManager.register(OWNER, "");
  }

  private static final int STUN_SECONDS = 7;
  private static final int TICKS_UNTIL_RETURN = 15;
  private static final int TICKS_UNTIL_DEATH = 900;
  private static final double SPEED = 0.95;
  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 3.8F;
  private static final DataParameter<Byte> IS_RETURNING = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BYTE);
  private static final DataParameter<Byte> REDSTONE_TRIGGERED = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BYTE);
  private static final DataParameter<String> OWNER = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.STRING);
  private ItemStack boomerangThrown = ItemStack.EMPTY;
  private PlayerEntity targetEntity;
  protected Boomer boomerangType;

  public void setBoomerangThrown(ItemStack boomerangThrown) {
    this.boomerangThrown = boomerangThrown;
  }

  @Override
  public void writeAdditional(CompoundNBT tag) {
    tag.putString("OWNER", dataManager.get(OWNER));
    tag.putByte("returning", dataManager.get(IS_RETURNING));
    tag.putByte("REDSTONE_TRIGGERED", dataManager.get(REDSTONE_TRIGGERED));
    boomerangThrown.write(tag);
    super.writeAdditional(tag);
  }

  @Override
  public void readAdditional(CompoundNBT tag) {
    dataManager.set(OWNER, tag.getString("OWNER"));
    dataManager.set(IS_RETURNING, tag.getByte("returning"));
    dataManager.set(REDSTONE_TRIGGERED, tag.getByte("REDSTONE_TRIGGERED"));
    boomerangThrown = ItemStack.read(tag);
    super.readAdditional(tag);
  }

  public void setOwner(PlayerEntity ent) {
    this.targetEntity = ent;
    if (ent != null && ent.getUniqueID() != null)
      dataManager.set(OWNER, ent.getUniqueID().toString());
  }

  private void movementReturnCheck() {
    boolean returning = dataManager.get(IS_RETURNING) == 1;
    if (returning) {
      //reverse direction 
      if (this.targetEntity != null) {
        this.moveTowardsTarget();
      }
    }
  }

  private void moveTowardsTarget() {
    rotationYaw = (float) Math.toRadians(UtilEntity.yawDegreesBetweenPoints(getPosX(), getPosY(), getPosZ(), targetEntity.getPosX(), targetEntity.getPosY(), targetEntity.getPosZ()));
    rotationPitch = (float) Math.toRadians(UtilEntity.pitchDegreesBetweenPoints(getPosX(), getPosY(), getPosZ(), targetEntity.getPosX(), targetEntity.getPosY(), targetEntity.getPosZ()));
    Vec3d moveVec = UtilEntity.lookVector(this.rotationYaw, this.rotationPitch).scale(SPEED);
    this.setMotion(
        0.5f * this.getMotion().getX() + 0.5f * moveVec.x,
        0.5f * this.getMotion().getY() + 0.503f * moveVec.y,
        0.5f * this.getMotion().getZ() + 0.5f * moveVec.z);
  }

  private void tryPickupNearby() {
    if (owner == null || world.isRemote) {
      return;
    }
    //try to find entities to pick up
    int range = 1;
    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this,
        this.getBoundingBox()
            .offset(this.getMotion()).grow(range, range, range));
    for (Entity entityHit : list) {
      if (entityHit instanceof ItemEntity || entityHit instanceof ExperienceOrbEntity) {
        entityHit.setPositionAndUpdate(owner.getPosX(), owner.getPosY(), owner.getPosZ());
      }
    }
  }

  private void tryToggleRedstone(final BlockPos pos) {
    if (owner instanceof PlayerEntity) {
      try {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        ActionResultType t = block.onBlockActivated(blockState, world, pos, (PlayerEntity) owner, Hand.MAIN_HAND, null);
        boolean hasTriggered = t == ActionResultType.SUCCESS;//block.onBlockActivated(world, pos, blockState,
        //            (PlayerEntity) this.owner, Hand.MAIN_HAND, Direction.UP, 0.5F, 0.5F, 0.5F);
        if (hasTriggered) {
          this.setRedstoneHasTriggered();
        }
      }
      catch (Throwable e) {
        //since activated can hit any block, be safe
        ModCyclic.error("Error on activate block", e);
      }
    }
  }

  private void setIsReturning() {
    dataManager.set(IS_RETURNING, (byte) 1);
  }

  private void setRedstoneHasTriggered() {
    this.dataManager.set(REDSTONE_TRIGGERED, (byte) 1);
  }

  private boolean hasTriggeredRedstoneAlready() {
    return this.dataManager.get(REDSTONE_TRIGGERED) > 0;
  }

  private void dropAsItem() {
    if (this.owner != null) {
      //try to give it to the player the nicest way possible 
      if (owner.getHeldItemMainhand().isEmpty())
        owner.setHeldItem(Hand.MAIN_HAND, boomerangThrown);
      else
        owner.entityDropItem(boomerangThrown, 0.5F);
    }
    else {
      UtilItemStack.drop(world, this.getPosition().up(), boomerangThrown);
    }
    this.remove();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.ticksExisted > TICKS_UNTIL_DEATH) {
      dropAsItem();
      return;
    }
    if (this.ticksExisted > TICKS_UNTIL_RETURN) {
      setIsReturning();
    }
    final BlockPos pos = this.getPosition();
    if (owner != null && UtilWorld.distanceBetweenHorizontal(pos, this.owner.getPosition()) < 1) {
      //       ModCyclic.LOGGER.info("Drop by distance");
      dropAsItem();
      return;
    }
    if (hasTriggeredRedstoneAlready() == false && world.isAirBlock(pos) == false) {
      tryToggleRedstone(pos);
    }
    tryPickupNearby();
    movementReturnCheck();
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    switch (result.getType()) {
      case BLOCK:
        onImpactBlock((BlockRayTraceResult) result);
      break;
      case ENTITY:
        onImpactEntity((EntityRayTraceResult) result);
      break;
      case MISS:
      default:
      break;
    }
  }

  private void onImpactBlock(BlockRayTraceResult mop) {
    BlockState block = world.getBlockState(mop.getPos());
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
    if (mop.getFace() != Direction.UP
        && block.isSolidSide(this.getEntityWorld(), mop.getPos(), mop.getFace())) {
      //ok return 
      this.setIsReturning();
    }
  }

  private void onImpactEntity(EntityRayTraceResult entityRayTrace) {
    Entity entityHit = entityRayTrace.getEntity();
    if (entityHit == null || !entityHit.isAlive()) {
      return;
    }
    if (owner == entityHit) {
      //player catch it on return 
      dropAsItem();
      //      ModCyclic.LOGGER.info("Drop by catching ");
      return;
    }
    switch (this.boomerangType) {
      case CARRY:
        entityHit.startRiding(this);
      break;
      case DAMAGE:
        if (entityHit instanceof LivingEntity) {
          LivingEntity live = (LivingEntity) entityHit;
          float damage = MathHelper.nextFloat(world.rand, DAMAGE_MIN, DAMAGE_MAX);
          boolean attackSucc = live.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
          if (attackSucc && live.isAlive() == false) {
            //            System.out.println("killed one");
          }
        }
      break;
      case STUN:
        if (entityHit != owner && entityHit instanceof LivingEntity) {
          LivingEntity live = (LivingEntity) entityHit;
          if (live.isPotionActive(PotionRegistry.PotionEffects.stun) == false) {
            live.addPotionEffect(new EffectInstance(PotionRegistry.PotionEffects.stun, STUN_SECONDS * 20, 1));
            UtilSound.playSound(live, live.getPosition(), SoundEvents.ENTITY_IRON_GOLEM_ATTACK);
          }
        }
      break;
      default:
      break;
    }
  }

  @Override
  protected float getGravityVelocity() {
    return -1 * 0.02F;
  }

  @Override
  protected Item getDefaultItem() {
    return null;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
