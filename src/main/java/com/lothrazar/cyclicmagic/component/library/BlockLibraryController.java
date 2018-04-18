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
package com.lothrazar.cyclicmagic.component.library;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLibraryController extends BlockBase implements IHasRecipe {

  private static final int RANGE = 4;
  Block libraryInstance;

  public BlockLibraryController(Block lib) {
    super(Material.WOOD);
    libraryInstance = lib;
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    List<BlockPos> connectors = UtilWorld.getMatchingInRange(world, pos, libraryInstance, RANGE);
    TileEntity te;
    TileEntityLibrary lib;
    ItemStack playerHeld = player.getHeldItem(hand);
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK) == false) {
      return false;
    }
    for (BlockPos p : connectors) {
      te = world.getTileEntity(p);
      if (te instanceof TileEntityLibrary) {
        lib = (TileEntityLibrary) te;
        QuadrantEnum quad = lib.findMatchingQuadrant(playerHeld);
        if (quad == null) {
          quad = lib.findEmptyQuadrant();
        }
        if (quad != null) {
          //now try insert here 
          if (lib.addEnchantmentFromPlayer(player, hand, quad)) {
            lib.markDirty();
            return true;
          }
        }
      }
    }
    // UtilChat.sendStatusMessage(player,UtilChat.lang("enchantment_stack.empty"));
    return false;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " r ",
        "rgr",
        " r ",
        'g', "chestEnder",
        'r', libraryInstance);
  }
}
