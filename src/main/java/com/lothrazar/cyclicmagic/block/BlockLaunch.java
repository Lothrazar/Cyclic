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
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFlat;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLaunch extends BlockBaseFlat implements IHasRecipe {

  private final static float ANGLE = 90;
  private final static int RECIPE_OUT = 6;

  public static enum LaunchType {
    SMALL, MEDIUM, LARGE;
  }

  public static boolean sneakPlayerAvoid;
  private LaunchType type;
  private float power;
  private SoundEvent sound;

  public BlockLaunch(LaunchType t, SoundEvent s) {
    super(Material.IRON);//same as BlockSlime 
    this.setSoundType(SoundType.SLIME);
    sound = s;
    type = t;
    switch (type) {
      case LARGE:
        this.power = 1.8F;
      break;
      case MEDIUM:
        this.power = 1.3F;
      break;
      case SMALL:
        this.power = 0.8F;
      break;
      default:
      break;
    }
  }

  protected void playClickOnSound(World worldIn, BlockPos pos) {
    worldIn.playSound((EntityPlayer) null, pos, this.sound, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }

  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    if (sneakPlayerAvoid && entity instanceof EntityPlayer && ((EntityPlayer) entity).isSneaking()) {
      return;
    }
    UtilEntity.launch(entity, ANGLE, power);
    this.playClickOnSound(worldIn, pos);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    int fakePower = (int) Math.round(this.power * 10); //  String.format("%.1f", this.power))
    tooltip.add(UtilChat.lang("tile.plate_launch.tooltip" + fakePower));
  }

  @Override
  public IRecipe addRecipe() {
    switch (type) {
      case LARGE:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUT),
            "sss", "ggg", "iii",
            's', "slimeball",
            'g', Blocks.STONE_PRESSURE_PLATE,
            'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      break;
      case MEDIUM:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUT),
            "sss", "ggg", "iii",
            's', "slimeball",
            'g', Blocks.WOODEN_PRESSURE_PLATE,
            'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      break;
      case SMALL:
        RecipeRegistry.addShapedRecipe(new ItemStack(this, RECIPE_OUT),
            "sss", "ggg", "iii",
            's', "slimeball",
            'g', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
            'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      break;
    }
    return null;
  }
}
