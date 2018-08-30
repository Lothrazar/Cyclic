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
package com.lothrazar.cyclicmagic.item.firemagic;

import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.item.core.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ItemProjectileBlaze extends BaseItemChargeScepter implements IHasRecipe {

  public ItemProjectileBlaze() {
    super(1000);
  }

  @Override
  public EntityBlazeBolt createBullet(World world, EntityPlayer player, float dmg) {
    EntityBlazeBolt s = new EntityBlazeBolt(world, player);
    return s;
  }

  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedOreRecipe(new ItemStack(getRepairItem().getItem(), 4),
        "cb",
        "bc",
        'c', new ItemStack(Items.FIRE_CHARGE),
        'b', "dyeLightBlue");
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " cc",
        "fbc",
        "ff ",
        'c', getRepairItem(),
        'b', new ItemStack(Items.BLAZE_POWDER),
        'f', new ItemStack(Items.FLINT));
  }

  @Override
  public SoundEvent getSound() {
    return SoundRegistry.fireball_staff_launch;//fireball_staff_launch
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return getRepairItem().isItemEqualIgnoreDurability(toRepair);
  }
}
