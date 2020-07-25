package com.lothrazar.cyclic.block.tankcask;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.capability.FluidHandlerCapabilityStack;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemCask extends BlockItem {

  public ItemCask(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean showDurabilityBar(ItemStack stack) {
    FluidStack fstack = copyFluidFromStack(stack);
    return fstack != null && fstack.getAmount() > 0;//  stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
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
      float qty = fstack.getAmount();
      float ratio = qty / (TileCask.CAPACITY);
      return 1 - ratio;
    }
    catch (Throwable e) {} //lazy 
    return 1;
  }

  public static FluidStack copyFluidFromStack(ItemStack stack) {
    if (stack.getTag() != null) {
      FluidHandlerCapabilityStack handler = new FluidHandlerCapabilityStack(stack, TileCask.CAPACITY);
      FluidStack fstack = handler.getFluid();
      if (fstack == null) {
        return null;
      }
      return handler.getFluid();
    }
    return null;
  }

  @Override
  public int getRGBDurabilityForDisplay(ItemStack stack) {
    return 0xADD8E6;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    IFluidHandler storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
    if (storage != null) {
      FluidStack fs = storage.getFluidInTank(0);
      if (fs != null && !fs.isEmpty()) {// + 
        TranslationTextComponent t = new TranslationTextComponent(
            fs.getDisplayName().getString()
                + " " + fs.getAmount()
                + "/" + storage.getTankCapacity(0));
        t.mergeStyle(TextFormatting.GRAY);
        tooltip.add(t);
        return;
      }
    }
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
    return new FluidHandlerCapabilityStack(stack, TileCask.CAPACITY);
  }
}
