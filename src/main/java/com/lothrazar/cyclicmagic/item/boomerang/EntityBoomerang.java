/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.boomerang;

import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.potion.PotionEffectRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EntityBoomerang extends EntityThrowableDispensable {

  @GameRegistry.ObjectHolder(Const.MODRES + "boomerang")
  public static final Item boomerangItem = null;
  private static final int STUN_TICKS = 45;
  private static final int TICKS_UNTIL_RETURN = 12;
  private static final int TICKS_UNTIL_DEATH = 900;
  private static final double SPEED = 0.95;
  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 2.8F;
  private static final DataParameter<Byte> IS_RETURNING = EntityDataManager.createKey(EntityBoomerang.class, DataSerializers.BYTE);
  private static final DataParameter<Byte> REDSTONE_TRIGGERED = EntityDataManager.createKey(EntityBoomerang.class, DataSerializers.BYTE);
  private static final DataParameter<String> OWNER = EntityDataManager.createKey(EntityBoomerang.class, DataSerializers.STRING);

  private EntityLivingBase targetEntity;
  private ItemStack boomerangThrown = ItemStack.EMPTY;

  public static class FactoryFire implements IRenderFactory<EntityBoomerang> {

    @Override
    public Render<? super EntityBoomerang> createRenderFor(RenderManager rm) {
      return new RenderSnowballSpin<EntityBoomerang>(rm, boomerangItem, Minecraft.getMinecraft().getRenderItem());
    }
  }

  public EntityBoomerang(World worldIn) {
    super(worldIn);
  }

  public EntityBoomerang(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntityBoomerang(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public void setOwner(EntityLivingBase ent) {
    this.targetEntity = ent;
    dataManager.set(OWNER, ent.getUniqueID().toString());
  }

  public ItemStack getBoomerangThrown() {
    return boomerangThrown;
  }

  public void setBoomerangThrown(ItemStack boomerangThrown) {
    this.boomerangThrown = boomerangThrown;
  }

  private void setRedstoneHasTriggered() {
    this.dataManager.set(REDSTONE_TRIGGERED, (byte) 1);
  }

  private boolean hasTriggeredRedstoneAlready() {
    return this.dataManager.get(REDSTONE_TRIGGERED) > 0;
  }

  public boolean isOwner(Entity entityHit) {
    String id = dataManager.get(OWNER);
    return id.equalsIgnoreCase(entityHit.getUniqueID().toString());
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tag) {
    tag.setString("OWNER", dataManager.get(OWNER));
    tag.setByte("returning", dataManager.get(IS_RETURNING));
    tag.setByte("REDSTONE_TRIGGERED", dataManager.get(REDSTONE_TRIGGERED));
    boomerangThrown.writeToNBT(tag);
    super.writeEntityToNBT(tag);
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tag) {
    dataManager.set(OWNER, tag.getString("OWNER"));
    dataManager.set(IS_RETURNING, tag.getByte("returning"));
    dataManager.set(REDSTONE_TRIGGERED, tag.getByte("REDSTONE_TRIGGERED"));
    boomerangThrown = new ItemStack(tag);
    super.readEntityFromNBT(tag);
  }

  @Override
  protected void entityInit() {
    this.dataManager.register(REDSTONE_TRIGGERED, (byte) 0);
    this.dataManager.register(IS_RETURNING, (byte) 0);
    this.dataManager.register(OWNER, "");
    super.entityInit();
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      if (isOwner(mop.entityHit)) {
        //i hit owner, im done
        dropAsItem();
      }
      else {
        float damage = MathHelper.nextFloat(world.rand, DAMAGE_MIN, DAMAGE_MAX);
        mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
        if (mop.entityHit instanceof EntityLivingBase) {
          EntityLivingBase living = (EntityLivingBase) mop.entityHit;
          if (living.isPotionActive(PotionEffectRegistry.STUN) == false) {
            living.addPotionEffect(new PotionEffect(PotionEffectRegistry.STUN, STUN_TICKS, 1));
          }
        }
      }
    }
    else {
      // hit a block or something, go back
      setIsReturning();
    }
  }

  private void dropAsItem() {
    if (this.targetEntity != null) {
      //try to give it to the player the nicest way possible 
      if (targetEntity.getHeldItemMainhand().isEmpty())
        targetEntity.setHeldItem(EnumHand.MAIN_HAND,boomerangThrown);
      else
        targetEntity.entityDropItem(boomerangThrown, 0.5F);
    }
    else {
      UtilItemStack.dropItemStackInWorld(world, this.getPosition().up(), boomerangThrown);
    }
    this.setDead();
  }

  private void setIsReturning() {
    dataManager.set(IS_RETURNING, (byte) 1);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
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
    }
    tryPickupNearby();
    movementReturnCheck();
  }

  private void tryToggleRedstone(final BlockPos pos) {
    if (thrower instanceof EntityPlayer) {
      try {
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean hasTriggered = block.onBlockActivated(world, pos, blockState, (EntityPlayer) this.thrower, EnumHand.MAIN_HAND, EnumFacing.UP, 0.5F, 0.5F, 0.5F);
        if (hasTriggered) {
          this.setRedstoneHasTriggered();
        }
      }
      catch (Throwable e) {
        //since activated can hit any block, be safe
        ModCyclic.logger.error("Error on activate block", e);
      }
    }
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

  private void tryPickupNearby() {
    //try to find entities to pick up
    int range = 1;
    // processImpact does not hit entity items so we pushed it over here
    //    List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - RANGE, y - RANGE, z - RANGE, x + RANGE, y + RANGE, z + RANGE));
    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ).grow(range, range, range));
    for (Entity entityHit : list) {
      if (entityHit instanceof EntityItem || entityHit instanceof EntityXPOrb) {
        entityHit.startRiding(this);
        break;
      }
    }
  }

  @Override
  protected float getGravityVelocity() {
    return -1 * 0.02F;
  }

  private void moveTowardsTarget() {
    rotationYaw = (float) Math.toRadians(UtilEntity.yawDegreesBetweenPoints(posX, posY, posZ, targetEntity.posX, targetEntity.posY, targetEntity.posZ));
    rotationPitch = (float) Math.toRadians(UtilEntity.pitchDegreesBetweenPoints(posX, posY, posZ, targetEntity.posX, targetEntity.posY, targetEntity.posZ));
    Vec3d moveVec = UtilEntity.lookVector(this.rotationYaw, this.rotationPitch).scale(SPEED);
    this.motionX = 0.5f * motionX + 0.5f * moveVec.x;
    this.motionY = 0.5f * motionY + 0.503f * moveVec.y;
    this.motionZ = 0.5f * motionZ + 0.5f * moveVec.z;
  }
}
