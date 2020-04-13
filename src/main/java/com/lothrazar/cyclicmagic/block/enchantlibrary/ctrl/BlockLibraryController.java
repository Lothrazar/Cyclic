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
package com.lothrazar.cyclicmagic.block.enchantlibrary.ctrl;

import java.util.List;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.enchantlibrary.EnchantStorageTarget;
import com.lothrazar.cyclicmagic.block.enchantlibrary.shelf.TileEntityLibrary;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.data.QuadrantEnum;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockLibraryController extends BlockBaseHasTile implements IHasRecipe {

  private static final int RANGE = 4;
  @GameRegistry.ObjectHolder(Const.MODRES + "block_library")
  static Block libraryInstance;

  public BlockLibraryController() {
    super(Material.WOOD);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityLibraryCtrl();
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack playerHeld = player.getHeldItem(hand);
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK) == false) {
      return false;
    }
    //HAX
    playerHeld.setCount(2);
    //it must be an enchanted book
    if (playerHeld.getCount() != 1) {
      if (world.isRemote)
        UtilChat.addChatMessage(player, "block_library.stacksize");
      return false;
    }
    //first look for the same enchant and level
    EnchantStorageTarget target = findMatchingTarget(world, pos, playerHeld);
    //now try insert here 
    if (target.isEmpty() == false) {
      ItemStack theThing = target.library.addEnchantmentToQuadrant(playerHeld, target.quad);
      player.setHeldItem(hand, ItemStack.EMPTY);
      if (theThing.isEmpty() == false) {
        player.addItemStackToInventory(theThing);
      }
      else {
        player.addItemStackToInventory(new ItemStack(Items.BOOK));
      }
      target.library.markDirty();
      world.markChunkDirty(target.library.getPos(), target.library);
      return true;
    }
    // UtilChat.sendStatusMessage(player,UtilChat.lang("enchantment_stack.empty"));
    return false;
  }

  public static EnchantStorageTarget findMatchingTarget(World world, BlockPos pos, ItemStack playerHeld) {
    EnchantStorageTarget target = new EnchantStorageTarget();
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK) == false) {
      return target;
    }
    List<BlockPos> connectors = UtilWorld.getMatchingInRange(world, pos, libraryInstance, RANGE);
    TileEntity te;
    TileEntityLibrary lib;
    for (BlockPos p : connectors) {
      te = world.getTileEntity(p);
      if (te instanceof TileEntityLibrary) {
        lib = (TileEntityLibrary) te;
        QuadrantEnum quad = lib.findMatchingQuadrant(playerHeld, lib);
        if (quad != null) {
          target.library = lib;
          target.quad = quad;
          break;
        }
      }
    }
    //just find the first empty slot
    if (target.library == null) {
      for (BlockPos p : connectors) {
        te = world.getTileEntity(p);
        if (te instanceof TileEntityLibrary) {
          lib = (TileEntityLibrary) te;
          QuadrantEnum quad = lib.findMatchingQuadrant(playerHeld, lib);
          if (quad == null) {
            quad = lib.findEmptyQuadrant();
          }
          if (quad != null) {
            target.library = lib;
            target.quad = quad;
            break;
          }
        }
      }
    }
    return target;
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
