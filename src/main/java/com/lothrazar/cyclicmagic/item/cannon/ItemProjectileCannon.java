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
package com.lothrazar.cyclicmagic.item.cannon;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.item.cannon.EntityGolemLaser.VariantColors;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemProjectileCannon extends BaseTool implements IHasRecipe {

  public ItemProjectileCannon() {
    super(1000);
  }


  public void createBullet(World world, EntityPlayer player) {

    EntityGolemLaser bullet = new EntityGolemLaser(world);
    int colorIndex = MathHelper.getInt(world.rand, 0, VariantColors.values().length - 1);
    bullet.getDataManager().set(EntityGolemLaser.variant, colorIndex);
    float speed = 4.0F;
    bullet.initCustom(player.posX, player.posY + 1.52, player.posZ,
        player.getLookVec().x * 0.5, player.getLookVec().y * 0.5, player.getLookVec().z * 0.5,
        speed, player.getUniqueID());
    if (world.isRemote == false)
      world.spawnEntity(bullet);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " ct",
        "bbc",
        "qb ",
        't', Items.SHULKER_SHELL,
        'c', Items.FIRE_CHARGE,
        'b', new ItemStack(Items.DIAMOND),
        'q', new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.DARK_META));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    this.createBullet(world, player);
    UtilSound.playSound(player, player.getPosition(), SoundRegistry.fireball_staff_launch, SoundCategory.PLAYERS, 0.4F);
    //    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    super.onUse(player.getHeldItem(hand), player, world, hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return getRepairItem().isItemEqualIgnoreDurability(toRepair);
  }
}
