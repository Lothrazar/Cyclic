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
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.item.BaseCharm;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilPlaceBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class ItemAutoTorch extends BaseCharm implements IHasRecipe, IHasConfig {

  private static final int durability = 256;
  private static int lightLimit = 7;

  public ItemAutoTorch() {
    super(durability);
    this.repairedBy = new ItemStack(Items.COAL);
  }

  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) {
      return;
    }
    World world = living.world;
    BlockPos pos = living.getPosition();
    if (world.getLight(pos, true) < lightLimit
        && living.isSpectator() == false
        && world.isSideSolid(pos.down(), EnumFacing.UP)
        && world.isAirBlock(pos)) { // dont overwrite liquids 
      if (UtilPlaceBlocks.placeStateSafe(world, living, pos, Blocks.TORCH.getDefaultState())) {
        super.damageCharm(living, stack);
      }
    }
  }

  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE), "blockCoal", "blockCoal", "blockCoal");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "cic",
        " i ",
        "cic",
        'c', "blockCoal",
        'i', Blocks.IRON_BARS);
  }

  @Override
  public void syncConfig(Configuration config) {
    lightLimit = config.getInt("AutoTorchLightLevel", Const.ConfigCategory.modpackMisc, 7, 1, 14, "At which light level will auto torch place.  Set to 7 means it will place a torch 7 or darker.  (15 is full light, 0 is full dark)");
  }
}
