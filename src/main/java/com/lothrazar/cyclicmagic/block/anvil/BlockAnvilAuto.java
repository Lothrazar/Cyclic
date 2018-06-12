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
package com.lothrazar.cyclicmagic.block.anvil;

import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAnvilAuto extends BlockBaseHasTile implements IHasConfig, IHasRecipe, IBlockHasTESR {

  public static final AxisAlignedBB Z_AXIS_AABB = new AxisAlignedBB(0.185D, 0.0D, 0.0D, 0.815D, 1.0D, 1.0D);
  public static int FUEL_COST = 0;
  private Block center;

  //block rotation in json http://www.minecraftforge.net/forum/index.php?topic=32753.0
  public BlockAnvilAuto(@Nonnull Block center) {
    super(Material.ANVIL);
    this.setSoundType(SoundType.ANVIL);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_ANVIL);
    this.setHardness(3.0F).setResistance(3.0F);
    this.setTranslucent();
    this.center = center;
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return Z_AXIS_AABB;
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityAnvilAuto();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAnvilAuto.class, new AnvilAutoTESR(TileEntityAnvilAuto.SLOT_INPUT));
  }

  @Override
  public void syncConfig(Configuration config) {
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 900, 0, 500000, Const.ConfigText.fuelCost);
    String category = Const.ConfigCategory.modpackMisc + ".block_anvil";
    // @formatter:off
    String[] deflist = new String[] {
        "galacticraftcore:battery" 
        , "galacticraftcore:oxygen_tank_heavy_full" 
        , "galacticraftcore:oxygen_tank_med_full" 
        , "galacticraftcore:oil_canister_partial" 
        , "galacticraftcore:oxygen_tank_light_full"
        ,"pneumaticcraft:*"
    };
    // @formatter:on
    String[] blacklist = config.getStringList("RepairBlacklist",
        category, deflist, "These cannot be repaired. Use star syntax to lock out an entire mod, otherwise use the standard modid:itemid for singles.  Applies to both diamond and magma anvil");
    TileEntityAnvilAuto.blacklistBlockIds = NonNullList.from("", blacklist);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ddd",
        "rer",
        "iii",
        'r', "dustRedstone",
        'i', "blockIron",
        'e', center,
        'd', "gemDiamond");
  }
}
