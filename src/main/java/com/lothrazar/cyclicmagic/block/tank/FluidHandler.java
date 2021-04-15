package com.lothrazar.cyclicmagic.block.tank;

import com.lothrazar.cyclicmagic.block.core.BlockBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandler extends FluidHandlerItemStack implements ICapabilityProvider {

  public FluidHandler(ItemStack stack) {
    super(stack, TileEntityFluidTank.CAPACITY);
  }

  @Override
  public int fill(FluidStack resource, boolean doFill) {
    return super.fill(resource, doFill);
  }

  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    return super.drain(resource, doDrain);
  }

  @Override
  public FluidStack getFluid() {
    NBTTagCompound tagCompound = container.getTagCompound();
    if (tagCompound == null) {
      return null;
    }
    return FluidStack.loadFluidStackFromNBT(tagCompound);
  }

  @Override
  protected void setFluid(FluidStack fluid) {
    if (!container.hasTagCompound()) {
      container.setTagCompound(new NBTTagCompound());
    }
    fluid.writeToNBT(container.getTagCompound());
  }

  @Override
  protected void setContainerToEmpty() {
    container.getTagCompound().removeTag(BlockBase.NBT_FLUIDSIZE);
    container.getTagCompound().removeTag(BlockBase.NBT_FLUIDTYPE);
  }
}
