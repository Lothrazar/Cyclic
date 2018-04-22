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

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemCharmBoat extends BaseCharm implements IHasRecipe {

  private static final int durability = 4096;

  public ItemCharmBoat() {
    super(durability);
  }

  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }

  @Override
  public void onTick(ItemStack stack, EntityPlayer entityIn) {
    if (!this.canTick(stack)) {
      return;
    }
    if (entityIn.getRidingEntity() instanceof EntityBoat) {
      EntityBoat boat = (EntityBoat) entityIn.getRidingEntity();
      if (entityIn.moveForward > 0) {
        float reduce = 0.08F;
        //pulled from private EntityBoat.controlBoat() fn
        boat.motionX += net.minecraft.util.math.MathHelper.sin(-boat.rotationYaw * 0.017453292F) * reduce;
        boat.motionZ += net.minecraft.util.math.MathHelper.cos(boat.rotationYaw * 0.017453292F) * reduce;
        if (entityIn.getEntityWorld().rand.nextDouble() < 0.1) {
          super.damageCharm(entityIn, stack);
        }
      }
    }
  }

  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.ARMOR_STAND);
  }
}
