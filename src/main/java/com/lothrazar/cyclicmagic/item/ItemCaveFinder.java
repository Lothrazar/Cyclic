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

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemCaveFinder extends BaseTool implements IHasRecipe, IHasConfig {

  private static final int DURABILITY = 2000;
  private static final int COOLDOWN = 12;
  private static int range = 32;

  public ItemCaveFinder() {
    super(DURABILITY);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos posIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (side == null || posIn == null) {
      return super.onItemUse(player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
    }
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    if (!worldObj.isRemote) {
      EnumFacing direction = side.getOpposite();
      BlockPos pos = posIn.offset(direction);
      BlockPos current = pos;
      for (int i = 1; i <= range; i++) {
        current = current.offset(direction);
        if (worldObj.isAirBlock(current)) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.cave") + i);
          found = true;
        }
        else if (worldObj.getBlockState(current) == Blocks.WATER.getDefaultState()
            || worldObj.getBlockState(current) == Blocks.FLOWING_WATER.getDefaultState()) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.water") + i);
          found = true;
        }
        else if (worldObj.getBlockState(current) == Blocks.LAVA.getDefaultState()
            || worldObj.getBlockState(current) == Blocks.FLOWING_LAVA.getDefaultState()) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.lava") + i);
          found = true;
        }
        if (found) {
          break;//stop looping
        }
      }
      if (found == false) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.none") + range);
      }
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', "stickWood",
        's', new ItemStack(Items.FLINT),
        'g', "dyeBlue");
  }

  @Override
  public void syncConfig(Configuration config) {
    range = config.getInt("CavefinderRange", Const.ConfigCategory.modpackMisc, 32, 2, 256, "Block Range it will search onclick");
  }
}
