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

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.item.core.BaseTool;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemWarpSurface extends BaseTool implements IHasRecipe, IContent {

  public ItemWarpSurface() {
    super(32);
  }

  @Override
  public void register() {
    ItemRegistry.register(this, "tool_elevate", GuideCategory.TRANSPORT);
    LootTableRegistry.registerLoot(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("RodElevation", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    World world = entity.getEntityWorld();
    BlockPos dest = UtilWorld.getFirstBlockAbove(world, entity.getPosition());
    if (dest != null) {//teleport the target entity
      UtilSound.playSound(player, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
      UtilEntity.teleportWallSafe(entity, world, dest);
      super.onUse(stack, player, world, hand);
      return true;
    }
    UtilSound.playSound(player, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    return false;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    BlockPos dest = UtilWorld.getFirstBlockAbove(world, player.getPosition());
    if (dest != null) {
      UtilSound.playSound(player, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
      UtilEntity.teleportWallSafe(player, world, dest);
      super.onUse(stack, player, world, hand);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    UtilSound.playSound(player, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " de",
        " gd",
        "s  ",
        'e', Items.ENDER_EYE,
        'd', "blockQuartz",
        'g', Blocks.REDSTONE_LAMP,
        's', Blocks.REDSTONE_LAMP);
  }
}
