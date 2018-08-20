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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EntityBoomerang extends EntityThrowableDispensable {

  private static final int TICKS_UNTIL_RETURN = 12;
  private static final int TICKS_UNTIL_DEATH = 900;
  private static final double SPEED = 0.95;
  private static final DataParameter<Byte> IS_RETURNING = EntityDataManager.createKey(EntityBoomerang.class, DataSerializers.BYTE);
  private static final DataParameter<String> OWNER = EntityDataManager.createKey(EntityBoomerang.class, DataSerializers.STRING);
  @GameRegistry.ObjectHolder(Const.MODRES + "boomerang")
  public static final Item boomerangItem = null;
  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 2.8F;

  private EntityLivingBase targetEntity;

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

  public boolean isOwner(Entity entityHit) {
    String id = dataManager.get(OWNER);
    return id.equalsIgnoreCase(entityHit.getUniqueID().toString());
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound tag) {
    tag.setString("OWNER", dataManager.get(OWNER));
    tag.setByte("returning", dataManager.get(IS_RETURNING));
    super.writeEntityToNBT(tag);
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound tag) {
    dataManager.set(OWNER, tag.getString("OWNER"));
    dataManager.set(IS_RETURNING, tag.getByte("returning"));
    super.readEntityFromNBT(tag);
  }

  @Override
  protected void entityInit() {
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
      }
    }
    else {
      // hit a block or something, go back
      setIsReturning();
    }
  }

  private void dropAsItem() {
    UtilItemStack.dropItemStackInWorld(world, this.getPosition().up(), boomerangItem);
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


    tryPickupNearby();
    movementReturnCheck();
    //failed animation attempt
    //    double max = 180.0D;
    //    float yFactor = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    //    this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * max / Math.PI);
    //    this.rotationPitch = (float) (MathHelper.atan2(this.motionY, yFactor) * max / Math.PI);
    //    setLocationAndAngles(this.posX, this.posY, this.posZ, rotationYaw, rotationPitch);
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
      if (entityHit instanceof EntityItem) {
        ModCyclic.logger.log("passanger");
        //        this.addPassenger(entityHit);
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
