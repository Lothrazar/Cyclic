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
package com.lothrazar.cyclicmagic.component.enchanter;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnchanter extends BlockBaseHasTile implements IHasRecipe, IBlockHasTESR, IHasConfig {

  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.75, 1);
  public static int FUEL_COST = 0;

  public BlockEnchanter() {
    super(Material.ROCK);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_ENCHANTER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.GLASS);
    this.setTranslucent();
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityEnchanter();
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnchanter.class, new EnchanterTESR(TileEntityEnchanter.SLOT_INPUT));
  }

  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " b ",
        "eoe",
        "ooo",
        'b', Blocks.ENCHANTING_TABLE,
        'e', "gemEmerald",
        'o', "obsidian");
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityEnchanter te = (TileEntityEnchanter) world.getTileEntity(pos);
    boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    if (te != null) {
      if (!world.isRemote) {
        int currentFluid = te.getCurrentFluidStackAmount();
        UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.amount") + currentFluid);
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }

  @Override
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 900, 0, 500000, Const.ConfigText.fuelCost);
    TileEntityEnchanter.FLUID_COST = config.getInt(this.getRawName() + "_xpjuice", Const.ConfigCategory.fuelCost, 100, 1, 1000, "Experience fluid cost per damage unit");
  }
}
