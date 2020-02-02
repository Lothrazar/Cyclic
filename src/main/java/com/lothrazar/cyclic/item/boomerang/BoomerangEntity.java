package com.lothrazar.cyclic.item.boomerang;

import java.util.List;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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

  public BoomerangEntity(World worldIn, LivingEntity throwerIn) {
    super(CyclicRegistry.Entities.boomerang, throwerIn, worldIn);
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(IS_RETURNING, (byte) 0);
    this.dataManager.register(REDSTONE_TRIGGERED, (byte) 0);
    this.dataManager.register(OWNER, "");
  }

  private static final int STUN_TICKS = 45;
  private static final int TICKS_UNTIL_RETURN = 12;
  private static final int TICKS_UNTIL_DEATH = 900;
  private static final double SPEED = 0.95;
  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 2.8F;
  private static final DataParameter<Byte> IS_RETURNING = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BYTE);
  private static final DataParameter<Byte> REDSTONE_TRIGGERED = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.BYTE);
  private static final DataParameter<String> OWNER = EntityDataManager.createKey(BoomerangEntity.class, DataSerializers.STRING);
  private ItemStack boomerangThrown = ItemStack.EMPTY;
  private PlayerEntity targetEntity;

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
        ModCyclic.LOGGER.error("Error on activate block", e);
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
    //    this.dismountRidingEntity();
    if (this.targetEntity != null) {
      //try to give it to the player the nicest way possible 
      if (targetEntity.getHeldItemMainhand().isEmpty())
        targetEntity.setHeldItem(Hand.MAIN_HAND, boomerangThrown);
      else
        targetEntity.entityDropItem(boomerangThrown, 0.5F);
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
    if (hasTriggeredRedstoneAlready() == false && world.isAirBlock(pos) == false) {
      tryToggleRedstone(pos);
      //      ModCyclic.LOGGER.info("redstone");
    }
    tryPickupNearby();
    movementReturnCheck();
    //    ModCyclic.LOGGER.info("check");
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    //
    RayTraceResult.Type type = result.getType();
    if (type == RayTraceResult.Type.ENTITY && owner != null) {
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      //pickup
      if (owner == entityRayTrace.getEntity()) {
        dropAsItem();
      }
      else if (entityRayTrace.getEntity() instanceof AnimalEntity) {
        entityRayTrace.getEntity().startRiding(this);
      }
      else if (entityRayTrace.getEntity() instanceof LivingEntity) {
        LivingEntity live = (LivingEntity) entityRayTrace.getEntity();
        float damage = MathHelper.nextFloat(world.rand, DAMAGE_MIN, DAMAGE_MAX);
        //        if (living.isPotionActive(PotionEffectRegistry.STUN) == false) {
        //          living.addPotionEffect(new PotionEffect(PotionEffectRegistry.STUN, STUN_TICKS, 1));
        //        }
        entityRayTrace.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
      }
    }
    else {
      if (type == RayTraceResult.Type.BLOCK) {
        BlockRayTraceResult mop = (BlockRayTraceResult) result;
        BlockState block = world.getBlockState(mop.getPos());
        //crops are 0.0, farmland 0.6, leaves are 0.2  etc
        if (this.canHarvest(block, mop.getPos(), mop.getFace())) {
          if (this.owner instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) owner;
            //  block.getBlock().harvestBlock(world, p, mop.getBlockPos(), block, null, boomerangThrown);
            block.getBlock().removedByPlayer(block, world, mop.getPos(), p, true, null);
          }
          //still break regardless of harvest 
          world.destroyBlock(mop.getPos(), false);
        }
        if (mop.getFace() != Direction.UP) {
          //ok return 
          this.setIsReturning();
        }
        //        else {
        //          //block impact so fall over
        //          this.dropAsItem();
        //        }
      }
      else
        this.setIsReturning();
    }
  }

  private boolean canHarvest(BlockState block, BlockPos pos, Direction sideHit) {
    //is it in whitelist to override 
    //TAG? !?!?!?
    Block b = block.getBlock();
    if (b == Blocks.MELON || b == Blocks.PUMPKIN || b == Blocks.CHORUS_FLOWER) {
      return true;
    }
    return false;
  }

  @Override
  protected float getGravityVelocity() {
    //    return super.getGravityVelocity();
    return -1 * 0.02F;
  }

  @Override
  protected Item getDefaultItem() {
    return CyclicRegistry.Items.boomerang;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
