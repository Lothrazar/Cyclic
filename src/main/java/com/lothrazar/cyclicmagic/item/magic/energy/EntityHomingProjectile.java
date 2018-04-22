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
package com.lothrazar.cyclicmagic.item.magic.energy;

import java.util.UUID;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * Parts of this From what used to be roots 1 by @elucent
 * 
 */
public class EntityHomingProjectile extends EntityThrowable {

  private static final int MAX_LIFETIME = 120;
  private static final int TIME_UNTIL_HOMING = 8;
  //higher speed is fasterS
  private static final double SPEED = 0.95;

  public static class FactoryMissile implements IRenderFactory<EntityHomingProjectile> {

    @Override
    public Render<? super EntityHomingProjectile> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityHomingProjectile>(rm, "magic_missile", false);
    }
  }

  private int lifetime = MAX_LIFETIME;
  private UUID targetId = null;
  private BlockPos targetPos;
  private EntityLivingBase targetEntity;
  private float damage = 6.0f;

  public EntityHomingProjectile(World worldIn) {
    super(worldIn);
    init();
  }

  public EntityHomingProjectile(World worldIn, EntityLivingBase thrower) {
    super(worldIn, thrower);
    init();
  }

  private void init() {
    this.setNoGravity(true);
    isImmuneToFire = true;
    this.setSize(0.1F, 0.1F);
  }

  public void setTarget(EntityLivingBase target) {
    this.targetId = target.getUniqueID();
    targetPos = target.getPosition();
    targetEntity = target;
    // ModCyclic.logger.error("pos  =" + this.targetPos);
  }

  //  @Override
  //  public int getBrightnessForRender() {
  //    return 255;
  //  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (this.world.isRemote == false
        && this.isDead == false
        && mop.entityHit != null
        && mop.entityHit instanceof EntityLivingBase
        && mop.entityHit.getUniqueID().compareTo(targetId) == 0) {
      //      ModCyclic.logger.error("DAMAGE TARGET  isclient==" + this.world.isRemote);
      mop.entityHit.attackEntityFrom(DamageSource.GENERIC, damage);
      //      this.getEntityWorld().removeEntity(this);
      this.setDead();
      // for (int i = 0; i < 4; i++) {
      UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, targetEntity.posX, targetEntity.posY, targetEntity.posZ);
      //  Roots.proxy.spawnParticleMagicAuraFX(getEntityWorld(), posX, posY, posZ, Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), color.xCoord, color.yCoord, color.zCoord);
      //  }
    }
  }

  @Override
  public void setDead() {
    super.setDead();
    this.setInvisible(true);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    lifetime--;
    if (lifetime > MAX_LIFETIME - TIME_UNTIL_HOMING) {
      return; //keep normal path for first fiew
    }
    //ModCyclic.logger.error("UPDATE ET  isclient==" + this.world.isRemote);
    if (lifetime == 0 || targetId == null || targetEntity == null || targetEntity.isDead) {
      this.setDead();
      //  this.getEntityWorld().removeEntity(this);
      return;
    }
    if (this.world.isRemote &&
        (targetEntity == null || this.targetEntity.getPosition().equals(this.getPosition()))) {
      this.setDead();//bandaid for client leftover
      return;
    }
    //+ target.getEyeHeight() / 2.0
    rotationYaw = (float) Math.toRadians(UtilEntity.yawDegreesBetweenPoints(posX, posY, posZ, targetEntity.posX, targetEntity.posY, targetEntity.posZ));
    rotationPitch = (float) Math.toRadians(UtilEntity.pitchDegreesBetweenPoints(posX, posY, posZ, targetEntity.posX, targetEntity.posY, targetEntity.posZ));
    Vec3d moveVec = UtilEntity.lookVector(this.rotationYaw, this.rotationPitch).scale(SPEED);
    this.motionX = 0.5f * motionX + 0.5f * moveVec.x;
    this.motionY = 0.5f * motionY + 0.5f * moveVec.y;
    this.motionZ = 0.5f * motionZ + 0.5f * moveVec.z;
  }
}
