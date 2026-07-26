package com.lothrazar.cyclic.block.expcollect;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.cyclic.block.tank.ItemBlockTank;
import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.util.FluidHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ItemBlockPylon extends BlockItem {

  public ItemBlockPylon(Block blockIn, Properties builder) {
    super(blockIn, builder);
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    FluidStack fstack = ItemBlockTank.copyFluidFromStack(stack);
    return fstack != null && fstack.getAmount() > 0; //  stack.hasCapability(ForgeCapabilities.FLUID_HANDLER, null);
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
      float max = TileExpPylon.CAPACITY;
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
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    IFluidHandler storage = CapabilityUtil.fluid(stack); //stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null).orElse(null);
    if (storage != null) {
      FluidStack fs = storage.getFluidInTank(0);
      if (fs != null && !fs.isEmpty()) {
        MutableComponent t = Component.translatable(
            fs.getHoverName().getString() // do not use .getDisplayName()
                + " " + fs.getAmount()
                + "/" + storage.getTankCapacity(0));
        t.withStyle(ChatFormatting.GRAY);
        tooltip.accept(t);
        return;
      }
    }
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
  }

}
