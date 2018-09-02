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

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.item.core.BaseCharm;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class ItemAutoTorch extends BaseCharm implements IHasRecipe, IContent {

  private static final int durability = 256;
  private static int lightLimit = 7;

  public ItemAutoTorch() {
    super(durability);
    this.repairedBy = new ItemStack(Items.COAL);
  }

  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) {
      return;
    }

    World world = player.world;
    BlockPos pos = player.getPosition();
    if (world.getLight(pos, true) < lightLimit
        && player.isSpectator() == false
        && world.isSideSolid(pos.down(), EnumFacing.UP)
        && world.isAirBlock(pos)) { // dont overwrite liquids 
      if (UtilPlaceBlocks.placeStateSafe(world, player, pos, Blocks.TORCH.getDefaultState())) {
        super.damageCharm(player, stack);
      }
    }
    else if (stack.isItemDamaged()) {
      ItemStack torches = this.findAmmo(player, Item.getItemFromBlock(Blocks.TORCH));
      if (!torches.isEmpty()) {
        torches.shrink(1);
        UtilItemStack.repairItem(player, stack);
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
  public void register() {
    ItemRegistry.register(this, "tool_auto_torch", GuideCategory.ITEMBAUBLES);
    ModCyclic.instance.events.register(this);
    LootTableRegistry.registerLoot(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("AutomaticTorch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    lightLimit = config.getInt("AutoTorchLightLevel", Const.ConfigCategory.modpackMisc, 7, 1, 14, "At which light level will auto torch place.  Set to 7 means it will place a torch 7 or darker.  (15 is full light, 0 is full dark)");
  }
}
