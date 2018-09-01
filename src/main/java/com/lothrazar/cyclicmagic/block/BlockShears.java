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
package com.lothrazar.cyclicmagic.block;

import java.util.List;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.core.BlockBase;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class BlockShears extends BlockBase implements IHasRecipe, IContent {

  private static final double OFFSET = 0.0625D;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(OFFSET, 0.0D, OFFSET, 1 - OFFSET, 1 - OFFSET, 1 - OFFSET);
  final static int FORTUNE = 10;// f yeah why not
  // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/FarmingBlocks/src/main/java/com/lothrazar/samsfarmblocks/BlockShearWool.java

  public BlockShears() {
    super(Material.CLOTH);
    this.setSoundType(SoundType.CLOTH);
    this.setTranslucent();
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, "block_shears", GuideCategory.BLOCK);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.content;
    enabled = config.getBoolean("ShearingBlock", category, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "sos",
        " s ",
        's', new ItemStack(Items.SHEARS, 1, OreDictionary.WILDCARD_VALUE),
        'o', new ItemStack(Blocks.WOOL, 1, EnumDyeColor.BLACK.getMetadata()));
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    if (entity instanceof IShearable) {
      IShearable sheep = (IShearable) entity;
      ItemStack fake = new ItemStack(Items.SHEARS);
      if (sheep.isShearable(fake, world, pos)) {
        List<ItemStack> drops = sheep.onSheared(fake, world, pos, FORTUNE);//since iShearable doesnt do drops, but DOES do sound/make sheep naked
        UtilItemStack.dropItemStacksInWorld(world, pos, drops);
      }
    }
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return AABB;
  }
}
