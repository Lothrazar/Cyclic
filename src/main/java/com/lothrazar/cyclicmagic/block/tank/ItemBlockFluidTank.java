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
package com.lothrazar.cyclicmagic.block.tank;

import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockFluidTank extends ItemBlock {

  // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1432714-forge-using-addinformation-on-a-block
  public ItemBlockFluidTank(Block block) {
    super(block);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    FluidStack fstack = copyFluidFromStack(stack);
    return fstack != null && fstack.amount > 0;//  stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xADD8E6;
  }

  /**
   * Queries the percentage of the 'Durability' bar that should be drawn.
   *
   * @param stack
   * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
   */
  @Override
  public double getDurabilityForDisplay(ItemStack stack) {
    try {
      //this is always null
      //   IFluidHandler storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
      FluidStack fstack = copyFluidFromStack(stack);
      float qty = fstack.amount;
      float ratio = qty / (TileEntityFluidTank.CAPACITY);
      return 1 - ratio;
    }
    catch (Throwable e) {} //lazy 
    return 1;
  }

  public static FluidStack copyFluidFromStack(ItemStack stack) {
    if (stack.getTagCompound() != null) {
      FluidHandler handler = new FluidHandler(stack);
      FluidStack fstack = handler.getFluid();
      if (fstack == null) {
        return null;
      }
      return handler.getFluid();
    }
    return null;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
    return new FluidHandler(stack);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack item, World player, List<String> tooltip, ITooltipFlag advanced) {
    FluidStack fstack = copyFluidFromStack(item);
    if (fstack != null && fstack.amount > 0) {
      int amt = fstack.amount;//   item.getTagCompound().getInteger(BlockFluidTank.NBT_FLUIDSIZE);
      String rsc = fstack.getLocalizedName();
      tooltip.add(amt + " " + rsc);
    }
    else
      tooltip.add(UtilChat.lang("tile.block_storeempty.tooltip"));
  }
}
