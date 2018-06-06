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
package com.lothrazar.cyclicmagic.item.slingshot;

import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EntitySlingshot extends EntityThrowableDispensable {

  @GameRegistry.ObjectHolder(Const.MODRES + "stone_pebble")
  public static final Item bullet = null;

  static final float DAMAGE_MIN = 1.5F;
  static final float DAMAGE_MAX = 2.8F;

  public static class FactoryFire implements IRenderFactory<EntitySlingshot> {

    @Override
    public Render<? super EntitySlingshot> createRenderFor(RenderManager rm) {
      RenderSnowball<EntitySlingshot> x = new RenderSnowball<EntitySlingshot>(rm, bullet, Minecraft.getMinecraft().getRenderItem());
      return x;
    }
  }

  public EntitySlingshot(World worldIn) {
    super(worldIn);
  }

  public EntitySlingshot(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntitySlingshot(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      float damage = MathHelper.nextFloat(world.rand, DAMAGE_MIN, DAMAGE_MAX);
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
    }
    else {
      // drop as item
      UtilItemStack.dropItemStackInWorld(world, mop.getBlockPos(), bullet);
    }
    this.setDead();
  }
}
