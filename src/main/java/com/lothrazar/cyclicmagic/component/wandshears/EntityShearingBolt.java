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
package com.lothrazar.cyclicmagic.component.wandshears;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityShearingBolt extends EntityThrowableDispensable {
  private static final int FORTUNE = 3;
  public static class FactoryShear implements IRenderFactory<EntityShearingBolt> {
    @Override
    public Render<? super EntityShearingBolt> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityShearingBolt>(rm, "shears");
    }
  }
  public EntityShearingBolt(World worldIn) {
    super(worldIn);
  }
  public EntityShearingBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityShearingBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  @Override
  protected void processImpact(RayTraceResult mop) {
    World world = getEntityWorld();
    //process entity hit if any
    if (mop.entityHit != null && mop.entityHit instanceof IShearable) {
      try {
        IShearable target = (IShearable) mop.entityHit;
        BlockPos ePos = mop.entityHit.getPosition();
        if (target.isShearable(null, world, ePos)) {
          java.util.List<ItemStack> drops = target.onSheared(null, world, ePos, FORTUNE);
          UtilItemStack.dropItemStacksInWorld(world, ePos, drops);
          UtilSound.playSound(world, ePos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.NEUTRAL);
          this.setDead();
        }
      }
      catch (Exception e) { //keep because a modded entity could be shearable and have issues
        // https://github.com/PrinceOfAmber/Cyclic/issues/120
        ModCyclic.logger.error(e.getMessage());
      }
    }
    if (this.isDead || mop.getBlockPos() == null) {
      return;
    }
    //process block hit if its shearable
    BlockPos pos = mop.getBlockPos();
    //process block hit if its shearable
    Block block = world.getBlockState(pos).getBlock();
    if (block instanceof net.minecraftforge.common.IShearable) {
      net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) block;
      if (target.isShearable(null, world, pos)) {
        java.util.List<ItemStack> drops = target.onSheared(null, world, pos, FORTUNE);
        for (ItemStack stack : drops) {
          float f = 0.7F;
          double d = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d1 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(world, (double) pos.getX() + d, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
          // entityitem.setDefaultPickupDelay();
          if (world.isRemote == false) {
            world.spawnEntity(entityitem);
            world.setBlockToAir(pos);
          }
          UtilSound.playSound(world, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS);
        }
        this.setDead();
      }
    }
  }
}
