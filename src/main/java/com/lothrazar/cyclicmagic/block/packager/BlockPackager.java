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
package com.lothrazar.cyclicmagic.block.packager;

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockPackager extends BlockBaseHasTile implements IHasConfig, IHasRecipe {

  public static int FUEL_COST = 0;

  public BlockPackager() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setTickRandomly(true);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_PACKAGER);
    RecipePackage.initAllRecipes();
  }

  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPackager();
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pcp",
        "x x",
        "pcp",
        'x', new ItemStack(Blocks.FURNACE),
        'c', "workbench",
        'p', "dyeLightBlue");
  }

  //  @Override
  //  public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
  //    TileEntity te = world.getTileEntity(pos);
  //    if (te instanceof TileEntityPackager) {
  //      float fill = ((TileEntityPackager) te).getFillRatio();
  //      return (int) (15 * fill);
  //    }
  //    return 0;
  //  }
  @Override
  public void syncConfig(Configuration config) {
    TileEntityPackager.TIMER_FULL = config.getInt(this.getRawName(), Const.ConfigCategory.machineTimer,
        35, 1, 9000, Const.ConfigText.machineTimer);
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 350, 0, 500000, Const.ConfigText.fuelCost);
  }
}
