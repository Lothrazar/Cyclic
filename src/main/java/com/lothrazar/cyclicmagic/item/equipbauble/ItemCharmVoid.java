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
package com.lothrazar.cyclicmagic.item.equipbauble;

import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.core.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCharmVoid extends BaseCharm implements IHasRecipe {

  private static final int durability = 16;
  private static final int yLowest = -30;
  private static final int yDest = 255;

  public ItemCharmVoid() {
    super(durability);
  }

  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.ENDER_EYE);
  }

  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) {
      return;
    }
    World worldIn = living.getEntityWorld();
    if (living.getPosition().getY() < yLowest) {
      UtilEntity.teleportWallSafe(living, worldIn, new BlockPos(living.getPosition().getX(), yDest, living.getPosition().getZ()));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, living.getSoundCategory());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL, living.getPosition());
    }
  }
}
