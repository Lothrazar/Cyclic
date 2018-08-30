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
package com.lothrazar.cyclicmagic.item.fishing;

import com.lothrazar.cyclicmagic.entity.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityFishingBolt extends EntityThrowableDispensable {

  public static class FactoryFish implements IRenderFactory<EntityFishingBolt> {

    @Override
    public Render<? super EntityFishingBolt> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityFishingBolt>(rm, "fishing");
    }
  }

  public EntityFishingBolt(World worldIn) {
    super(worldIn);
  }

  public EntityFishingBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }

  public EntityFishingBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  static final double plainChance = 60;
  static final double salmonChance = 25 + plainChance; // so it is between 60
  // and 85
  static final double clownfishChance = 2 + salmonChance; // so between 85 and
  // 87

  @Override
  protected void processImpact(RayTraceResult mop) {
    BlockPos pos = mop.getBlockPos();
    if (pos == null) {
      return;
    }
    World world = getEntityWorld();
    if (this.isInWater()) {
      UtilParticle.spawnParticle(this.getEntityWorld(), EnumParticleTypes.WATER_BUBBLE, pos);
      EntityItem ei = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), getRandomFish());
      if (world.isRemote == false) {
        world.spawnEntity(ei);
      }
      UtilSound.playSound(world, pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS);
      this.setDead();
    }
    //    else {
    //      if (world.isRemote == false) {
    //        world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(renderSnowball)));
    //        this.setDead();
    //      }
    //    }
  }

  private ItemStack getRandomFish() {
    ItemStack fishSpawned = null;
    // below is from MY BlockFishing.java in the unreleased ./FarmingBlocks/
    double diceRoll = rand.nextDouble() * 100;
    if (diceRoll < plainChance) {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()); // plain
    }
    else if (diceRoll < salmonChance) {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata());// salmon
    }
    else if (diceRoll < clownfishChance) {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata());// clown
    }
    else {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata());// puffer
    }
    return fishSpawned;
  }
}
