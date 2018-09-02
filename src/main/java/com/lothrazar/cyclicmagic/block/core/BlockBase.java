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
package com.lothrazar.cyclicmagic.block.core;

import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBase extends Block {

  public static final String NBT_FLUIDSIZE = "fluidtotal";
  public static final String NBT_FLUIDTYPE = "fluidtype";

  public BlockBase(Material materialIn) {
    super(materialIn);
    this.setHardness(2.0F).setResistance(2.0F);//of course can/will be overwritten in most cases, but at least have a nonzero default
  }

  protected boolean isTransp = false;
  protected String myTooltip = null;

  protected void setTranslucent() {
    this.translucent = true;
    this.isTransp = true;
    this.setLightOpacity(0);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    if (myTooltip == null) {
      myTooltip = this.getUnlocalizedName() + ".tooltip";
    }
    tooltip.add(UtilChat.lang(myTooltip));
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return !this.isTransp; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    if (this.isTransp)
      return BlockRenderLayer.TRANSLUCENT;
    else
      return super.getBlockLayer(); // SOLID
  }
}
