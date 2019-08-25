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
package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCharmVoid extends ItemBase {

  public ItemCharmVoid(Properties properties) {
    super(properties);
    // TODO Auto-generated constructor stub
  }

  private static final int durability = 16;
  private static final int yLowest = -30;
  private static final int yDest = 255;
  //  private static final ItemStack craftItem = new ItemStack(Items.ENDER_EYE);
  //
  //

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {}
  //  @Override
  //  public void onTick(ItemStack stack, EntityPlayer living) {
  //    if (!this.canTick(stack)) {
  //      return;
  //    }
  //    World worldIn = living.getEntityWorld();
  //    if (living.getPosition().getY() < yLowest) {
  //      UtilEntity.teleportWallSafe(living, worldIn, new BlockPos(living.getPosition().getX(), yDest, living.getPosition().getZ()));
  //      super.damageCharm(living, stack);
  //      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, living.getSoundCategory());
  //      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL, living.getPosition());
  //    }
  //  }
}
