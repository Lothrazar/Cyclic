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
package com.lothrazar.cyclicmagic.component.wandmissile;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 * From what used to be roots 1 by @elucent
 * 
 *
 */
public class EntityHomingProjectile extends EntityThrowableDispensable {// implements IRangedAttackMob {
  public static class FactoryMissile implements IRenderFactory<EntityHomingProjectile> {
    @Override
    public Render<? super EntityHomingProjectile> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityHomingProjectile>(rm, "magic_missile");
    }
  }
  public int lifetime = 120;
  public UUID targetId = null;
  public BlockPos targetPos;
  private float damage = 6.0f;
  public EntityHomingProjectile(World worldIn) {
    super(worldIn);
  }
  public EntityHomingProjectile(World worldIn, EntityPlayer thrower) {
    super(worldIn, thrower);
  }

  public void setTarget(EntityLivingBase target) {//, Vec3d color
    this.targetId = target.getUniqueID();
    targetPos = target.getPosition();
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null
        && mop.entityHit instanceof EntityLivingBase
        && mop.entityHit.getUniqueID().compareTo(targetId) == 0) {
      mop.entityHit.attackEntityFrom(DamageSource.GENERIC, damage);
      this.getEntityWorld().removeEntity(this);
      for (int i = 0; i < 40; i++) {
        UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, targetPos);
        //  Roots.proxy.spawnParticleMagicAuraFX(getEntityWorld(), posX, posY, posZ, Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), Math.pow(1.15f*(random.nextFloat()-0.5f),3.0), color.xCoord, color.yCoord, color.zCoord);
      }
    }
  }

  @Override
  public void onUpdate() {
    super.onUpdate();


    lifetime--;
    if (lifetime == 0 || targetId == null) {
      ModCyclic.logger.info("homing particles SET DEAD -> " + world.isRemote + this.getPosition());
      this.getEntityWorld().removeEntity(this);
      this.setDead();
      return;
    }

      //+ target.getEyeHeight() / 2.0
      rotationYaw = (float) Math.toRadians(UtilEntity.yawDegreesBetweenPoints(posX, posY, posZ, targetPos.getX(), targetPos.getY(), targetPos.getZ()));
      rotationPitch = (float) Math.toRadians(UtilEntity.pitchDegreesBetweenPoints(posX, posY, posZ, targetPos.getX(), targetPos.getY(), targetPos.getZ()));
      Vec3d moveVec = UtilEntity.lookVector(this.rotationYaw, this.rotationPitch).scale(0.35f);
      this.motionX = 0.5f * motionX + 0.5f * moveVec.x;
      this.motionY = 0.5f * motionY + 0.5f * moveVec.y;
      this.motionZ = 0.5f * motionZ + 0.5f * moveVec.z;
      for (double i = 0; i < 1; i++) {
        double x = this.getEntityBoundingBox().minX * 0.5 + this.getEntityBoundingBox().maxX * 0.5;
        double y = this.getEntityBoundingBox().minY * 0.5 + this.getEntityBoundingBox().maxY * 0.5;
        double z = this.getEntityBoundingBox().minZ * 0.5 + this.getEntityBoundingBox().maxZ * 0.5;
        UtilParticle.spawnParticlePacket(EnumParticleTypes.CRIT_MAGIC, this.dimension, x, y, z);
        //Roots.proxy.spawnParticleMagicAuraFX(getEntityWorld(), x, y, z, -0.125*moveVec.xCoord, -0.125*moveVec.yCoord, -0.125*moveVec.zCoord, color.xCoord, color.yCoord, color.zCoord);
      }

  }
}
