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
package com.lothrazar.cyclicmagic.component.wandtorch;

import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityTorchBolt extends EntityThrowableDispensable {

  public static class FactoryTorch implements IRenderFactory<EntityTorchBolt> {

    @Override
    public Render<? super EntityTorchBolt> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityTorchBolt>(rm, "torch");
    }
  }

  public EntityTorchBolt(World worldIn) {
    super(worldIn);
  }

  public EntityTorchBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntityTorchBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  @Override
  protected void processImpact(RayTraceResult mop) {
    if (mop.entityHit != null) {
      //zero damage means just knockback
      mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
    }
    EnumFacing sideHit = mop.sideHit;
    BlockPos pos = mop.getBlockPos();
    BlockPos offset = mop.getBlockPos();
    if (sideHit != null) {
      offset = pos.offset(sideHit);
    }
    if (offset == null) {
      return;
    }
    World world = this.getEntityWorld();
    boolean isSideSolid = world.isSideSolid(mop.getBlockPos(), sideHit);
    boolean isValidBlockstate = BlockTorch.FACING.getAllowedValues().contains(sideHit);
    boolean isValidLocation = world.isAirBlock(offset) ||
        world.getBlockState(offset) == null ||
        world.getBlockState(offset).getBlock() == null ||
        world.getBlockState(offset).getBlock().isReplaceable(world, offset);
    if (isValidLocation && isValidBlockstate && isSideSolid && world.isRemote == false) {
      world.setBlockState(offset, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, sideHit));
    }
    //    else {
    //      UtilItemStack.dropItemStackInWorld(world, this.getPosition(), renderSnowball);
    //    }
    this.setDead();
  }
}
