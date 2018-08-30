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
package com.lothrazar.cyclicmagic.liquid.exp;

import com.lothrazar.cyclicmagic.block.core.BlockFluidBase;
import com.lothrazar.cyclicmagic.liquid.FluidsRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidExp extends BlockFluidBase {

  public static FluidStack stack;//= new FluidStack(FluidsRegistry.fluid_milk,1000);

  public BlockFluidExp() {
    super(FluidsRegistry.fluid_exp, Material.WATER);
    FluidsRegistry.fluid_exp.setBlock(this);
    stack = new FluidStack(FluidsRegistry.fluid_exp, Fluid.BUCKET_VOLUME);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    Block block = FluidsRegistry.block_exp;
    Item item = Item.getItemFromBlock(block);
    ModelBakery.registerItemVariants(item);
    final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Const.MODID + ":fluid", stack.getFluid().getName());
    ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    ModelLoader.setCustomStateMapper(block, new StateMapperBase() {

      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState bs) {
        return modelResourceLocation;
      }
    });
  }
}
