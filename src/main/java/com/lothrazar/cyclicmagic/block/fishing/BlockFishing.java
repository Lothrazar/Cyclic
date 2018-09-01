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
package com.lothrazar.cyclicmagic.block.fishing;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.core.IBlockHasTESR;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFishing extends BlockBaseHasTile implements IContent, IHasRecipe, IBlockHasTESR {

  public static int FUEL_COST;

  public BlockFishing() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_FISHER);
    this.setTranslucent();
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFishing();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFishing.class, new FishingTESR(0));
  }

  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
        'p', "chestTrapped");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
        'p', "chestTrapped");
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, "block_fishing", GuideCategory.BLOCK);
    GameRegistry.registerTileEntity(TileEntityFishing.class, Const.MODID + "block_fishing_te");
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    FUEL_COST = config.getInt(this.getRawName(), Const.ConfigCategory.fuelCost, 25, 0, 500000, Const.ConfigText.fuelCost);
  }
}
