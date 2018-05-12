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
package com.lothrazar.cyclicmagic.item.magic.locate;

import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.entity.RenderBall;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityDungeonEye extends EntityThrowableDispensable {

  public static class FactoryDungeon implements IRenderFactory<EntityDungeonEye> {

    @Override
    public Render<? super EntityDungeonEye> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityDungeonEye>(rm, "dungeon");
    }
  }

  private static final double DISTLIMIT = 0.8;
  private static final double VERT = 0.014999999664723873D;
  private static final double HORIZ = 0.0025D;
  private double targetX;
  private double targetY;
  private double targetZ;

  public EntityDungeonEye(World worldIn) {
    super(worldIn);
  }

  public EntityDungeonEye(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntityDungeonEye(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public void moveTowards(BlockPos pos) {
    this.targetX = (double) pos.getX();
    this.targetY = pos.getY();
    this.targetZ = (double) pos.getZ();
    this.setThrowableHeading(this.targetX, this.targetY, this.targetZ, (float) (this.getGravityVelocity()), 0.01F);
  }

  @Override
  public void onUpdate() {
    if (!this.world.isRemote) {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      double distX = Math.abs(this.targetX - this.posX);
      double distY = Math.abs(this.targetY - this.posY);
      double distZ = Math.abs(this.targetZ - this.posZ);
      float distance = (float) Math.sqrt(distX * distX + distZ * distZ);
      float distLine = (float) Math.sqrt(distX * distX + distZ * distZ + distY * distY);
      float atan = (float) MathHelper.atan2(this.targetZ - this.posZ, this.targetX - this.posX);
      double horizFactor = (double) f + (double) (distance - f) * HORIZ;
      if (distLine < 1.0F) {
        horizFactor *= 0.8D;
        this.motionY *= 0.8D;
        this.setDead();
      }
      this.motionX = Math.cos((double) atan) * horizFactor;
      this.motionZ = Math.sin((double) atan) * horizFactor;
      this.motionY = (14 * distY) / distLine * VERT;
      if (distX < DISTLIMIT && distZ < DISTLIMIT) {//if we are right in line, stop swaggerin
        motionX = 0;
        motionZ = 0;
        if (distY < DISTLIMIT) {
          motionY = 0;
        }
      }
      if (this.posY < this.targetY) {
        // make sure motion is going up
        if (this.motionY < 0) {
          this.motionY *= -1;
        }
      }
      else {
        // make sure motion is going DOWN
        if (this.motionY > 0) {
          this.motionY *= -1;
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
      this.motionX /= speedHReduction;
      this.motionY /= speedVReduction;
      this.motionZ /= speedHReduction;
    }
    if (this.ticksExisted > 9999) {
      this.setDead();
    }
    if (this.motionX == 0 && this.motionY == 0 && this.motionZ == 0) {
      this.setDead();
    }
    int particleCount = (this.ticksExisted < 100) ? 30 : 14;
    float f3 = 0.25F;
    for (int i = 0; i < particleCount; ++i) {
      this.getEntityWorld().spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * (double) f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double) f3 - 0.5D, this.posZ - this.motionZ * (double) f3 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
    }
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    this.setDead();// does not pass through walls or entities
  }
}
