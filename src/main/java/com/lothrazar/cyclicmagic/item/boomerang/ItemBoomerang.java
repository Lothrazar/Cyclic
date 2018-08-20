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
package com.lothrazar.cyclicmagic.item.boomerang;

import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBoomerang extends BaseItemChargeScepter implements IHasRecipe {

  public ItemBoomerang() {
    super(1);
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    if (entity instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    float percentageCharged = ItemBow.getArrowVelocity(charge);//never zero, its from [0.03,1];
    float amountCharged = percentageCharged * MAX_CHARGE;
    float velocityFactor = percentageCharged * 1.5F;//flat upscale
    //between 0.3 and 5.1 roughly
    //UtilChat.sendStatusMessage(player, amountCharged + "");
    float damage = MathHelper.floor(amountCharged) / 2;//so its an even 3 or 2.5
    shootMain(world, player, velocityFactor, damage);
    player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
  }

  @Override
  public EntityBoomerang createBullet(World world, EntityPlayer player, float dmg) {
    EntityBoomerang e = new EntityBoomerang(world, player);
    e.setOwner(player);
    return e;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " bs",
        " fb",
        "b  ",
        's', Items.STRING,
        'b', new ItemStack(Items.STICK),
        'f', new ItemStack(Items.LEATHER));
  }

  @Override
  public SoundEvent getSound() {
    return SoundEvents.ENTITY_SNOWBALL_THROW;
  }
}
