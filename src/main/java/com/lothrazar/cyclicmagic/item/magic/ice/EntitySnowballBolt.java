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
package com.lothrazar.cyclicmagic.item.magic.ice;

import com.lothrazar.cyclicmagic.core.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.core.entity.RenderBall;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilParticle;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntitySnowballBolt extends EntityThrowableDispensable {

  private static final int SLOWNESSLEVEL = 2;
  private static final int POTIONSECONDS = 30;

  public static class FactorySnow implements IRenderFactory<EntitySnowballBolt> {

    @Override
    public Render<? super EntitySnowballBolt> createRenderFor(RenderManager rm) {
      return new RenderBall<EntitySnowballBolt>(rm, "snow");
    }
  }

  private float damage = 3;

  public EntitySnowballBolt(World worldIn) {
    super(worldIn);
  }

  public EntitySnowballBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntitySnowballBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public void setDamage(float d) {
    damage = d;
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    World world = getEntityWorld();
    if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase) {
      UtilParticle.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, mop.entityHit.getPosition());
      if (mop.entityHit instanceof EntityPlayer) {
        onHitPlayer(mop, (EntityPlayer) mop.entityHit);
      }
      else {
        onHitEntity(mop);
      }
      this.setDead();
    }
    else if (this.isInWater()) {
      onHitWater(mop);
      this.setDead();
    }
    else {
      onHitGround(mop);
      this.setDead();
    }
  }

  private void onHitPlayer(RayTraceResult mop, EntityPlayer entityHit) {
    //not slowness only snow
    entityHit.extinguish();
    entityHit.addPotionEffect(new PotionEffect(PotionEffectRegistry.SNOW, Const.TICKS_PER_SEC * POTIONSECONDS));
  }

  private void onHitEntity(RayTraceResult mop) {
    EntityLivingBase e = (EntityLivingBase) mop.entityHit;
    if (e.isBurning()) {
      e.extinguish();
    }
    e.addPotionEffect(new PotionEffect(PotionEffectRegistry.SNOW, Const.TICKS_PER_SEC * 30));
    e.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, Const.TICKS_PER_SEC * 30, SLOWNESSLEVEL));
    // do the snowball damage, which should be none. put out the fire
    mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
  }

  private void onHitGround(RayTraceResult mop) {
    BlockPos pos = mop.getBlockPos();
    if (pos == null) {
      return;
    }
    UtilParticle.spawnParticle(world, EnumParticleTypes.SNOW_SHOVEL, pos);
    World world = getEntityWorld();
    if (mop.sideHit != null) {
      world.extinguishFire((EntityPlayer) this.getThrower(), pos, mop.sideHit);
    }
    // on land, so snow?
    BlockPos hit = pos;
    BlockPos hitDown = hit.down();
    BlockPos hitUp = hit.up();
    IBlockState hitState = world.getBlockState(hit);
    if (hitState.getBlock() == Blocks.SNOW_LAYER) {
      setMoreSnow(world, hit);
    } // these other cases do not really fire, i think. unless the entity goes
    // inside a
    // block before despawning
    else if (world.getBlockState(hitDown).getBlock() == Blocks.SNOW_LAYER) {
      setMoreSnow(world, hitDown);
    }
    else if (world.getBlockState(hitUp).getBlock() == Blocks.SNOW_LAYER) {
      setMoreSnow(world, hitUp);
    }
    else if (world.isAirBlock(hit) == false // one below us cannot be
        // air
        && // and we landed at air or replaceable
        world.isAirBlock(hitUp) == true)
    // this.worldObj.getBlockState(this.getPosition()).getBlock().isReplaceable(this.worldObj,
    // this.getPosition()))
    {
      setNewSnow(world, hitUp);
    }
    else if (world.isAirBlock(hit) == false // one below us cannot be
        // air
        && // and we landed at air or replaceable
        world.isAirBlock(hitUp) == true) {
      setNewSnow(world, hitUp);
    }
  }

  public void onHitWater(RayTraceResult mop) {
    World world = getEntityWorld();
    BlockPos posWater = this.getPosition();
    if (world.getBlockState(posWater) != Blocks.WATER.getDefaultState()) {
      posWater = null;// look for the closest water source, sometimes it was
      // air and we
      // got ice right above the water if we dont do this check
      if (world.getBlockState(mop.getBlockPos()) == Blocks.WATER.getDefaultState())
        posWater = mop.getBlockPos();
      else if (world.getBlockState(mop.getBlockPos().offset(mop.sideHit)) == Blocks.WATER.getDefaultState())
        posWater = mop.getBlockPos().offset(mop.sideHit);
    }
    if (posWater != null) {
      world.setBlockState(posWater, Blocks.ICE.getDefaultState());
    }
  }

  private static void setMoreSnow(World world, BlockPos pos) {
    IBlockState hitState = world.getBlockState(pos);
    int m = hitState.getBlock().getMetaFromState(hitState) + 1;
    if (BlockSnow.LAYERS.getAllowedValues().contains(m + 1)) {
      world.setBlockState(pos,
          Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, m + 1));
      UtilSound.playSound(world, pos, SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS);
    }
    else {
      setNewSnow(world, pos.up());
    }
  }

  private static void setNewSnow(World world, BlockPos pos) {
    world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
    UtilSound.playSound(world, pos, SoundEvents.BLOCK_SNOW_PLACE, SoundCategory.BLOCKS);
  }
}
