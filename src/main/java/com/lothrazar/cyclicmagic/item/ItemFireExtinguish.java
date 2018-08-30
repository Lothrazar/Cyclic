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
package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.core.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFireExtinguish extends BaseTool implements IHasRecipe {

  private static final int DURABILITY = 256;
  private static final int COOLDOWN = 10;
  private static final int RADIUS = 4;

  public ItemFireExtinguish() {
    super(DURABILITY);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (pos == null) {
      return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
    }
    if (side != null) {
      pos = pos.offset(side);
    }
    if (spreadWaterFromCenter(world, player, pos))
      super.onUse(stack, player, world, hand);
    return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (spreadWaterFromCenter(world, player, player.getPosition())) {
      super.onUse(stack, player, world, hand);
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }

  private boolean spreadWaterFromCenter(World world, EntityPlayer player, BlockPos posCenter) {
    int count = 0;
    for (BlockPos pos : UtilWorld.findBlocks(world, posCenter, Blocks.FIRE, RADIUS)) {
      world.setBlockToAir(pos);
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos);
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_SPLASH, pos.up());
      count++;
    }
    boolean success = count > 0;
    if (success) {//particles are on each location, sound is just once
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
      UtilSound.playSound(player, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    }
    return success;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "mwm",
        "rwr",
        " i ",
        'w', new ItemStack(Items.WATER_BUCKET),
        'm', new ItemStack(Blocks.MAGMA), //magma'm', new ItemStack(Blocks.MAGMA), 
        'r', "dyeRed",
        'i', "ingotIron");
  }
}
