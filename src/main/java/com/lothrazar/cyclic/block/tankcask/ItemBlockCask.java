package com.lothrazar.cyclic.block.tankcask;

import java.util.List;
import com.lothrazar.cyclic.block.tank.ItemBlockTank;
import com.lothrazar.cyclic.capabilities.FluidHandlerCapabilityStack;
import com.lothrazar.cyclic.util.FluidHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ItemBlockCask extends BlockItem {

  public ItemBlockCask(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    FluidStack fstack = ItemBlockTank.copyFluidFromStack(stack);
    return fstack != null && fstack.getAmount() > 0; //  stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
  }

  /**
   * Queries the percentage of the 'Durability' bar that should be drawn.
   *
   * @param stack
   * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
   */
  @Override
  public int getBarWidth(ItemStack stack) {
    try {
      //this is always null 
      FluidStack fstack = ItemBlockTank.copyFluidFromStack(stack);
      float current = fstack.getAmount();
      float max = TileCask.CAPACITY;
      return Math.round(13.0F * current / max);
    }
    catch (Throwable e) {
      //lazy 
    }
    return 1;
  }

  @Override
  public int getBarColor(ItemStack stack) {
    FluidStack fstack = ItemBlockTank.copyFluidFromStack(stack);
    return FluidHelpers.getColorFromFluid(fstack);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    IFluidHandler storage = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
    if (storage != null) {
      FluidStack fs = storage.getFluidInTank(0);
      if (fs != null && !fs.isEmpty()) {
        TranslatableComponent t = new TranslatableComponent(
            fs.getDisplayName().getString()
                + " " + fs.getAmount()
                + "/" + storage.getTankCapacity(0));
        t.withStyle(ChatFormatting.GRAY);
        tooltip.add(t);
        return;
      }
    }
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, net.minecraft.nbt.CompoundTag nbt) {
    return new FluidHandlerCapabilityStack(stack, TileCask.CAPACITY);
  }
}
