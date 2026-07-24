package com.lothrazar.cyclic.item.datacard.fluid;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Fluid-side counterpart to FilterCardItem. Single bucket slot that defines
 * which fluids are allowed/ignored. Supports allow/ignore and tag-match modes
 * mirroring FilterCardItem.
 */
public class FluidFilterCardItem extends ItemBaseCyclic {

  public static final int SLOT_FLUID = 0;
  private static final String NBTFILTER = "filter";
  private static final String NBTTAGMATCH = "tagmatch";

  public FluidFilterCardItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide() && !playerIn.isCrouching()) {
      playerIn.openMenu(new ContainerProviderFluidFilterCard(), playerIn.blockPosition());
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      boolean isIgnore = getIsIgnoreList(stack);
      MutableComponent t = Component.translatable("cyclic.screen.filter." + isIgnore);
      t.withStyle(isIgnore ? ChatFormatting.DARK_GRAY : ChatFormatting.DARK_BLUE);
      tooltip.add(t);
      CompoundTag stackTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      int count = stackTag.getIntOr("fluidCount", 0);
      if (count > 0) {
        if (stackTag.contains("fluidTooltip")) {
          tooltip.add(Component.translatable(stackTag.getString("fluidTooltip")).withStyle(ChatFormatting.AQUA));
        }
        tooltip.add(Component.translatable("cyclic.screen.filter.item.count").append("" + count).withStyle(ChatFormatting.AQUA));
      }
    }
    else {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
  }

  public static void toggleFilterType(ItemStack filter) {
    boolean prev = getIsIgnoreList(filter);
    CompoundTag tag = filter.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putBoolean(NBTFILTER, !prev);
    filter.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public static void toggleTagMatch(ItemStack filter) {
    boolean prev = getIsTagMatch(filter);
    CompoundTag tag = filter.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putBoolean(NBTTAGMATCH, !prev);
    filter.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public static boolean getIsIgnoreList(ItemStack filterStack) {
    return filterStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBTFILTER, false);
  }

  public static boolean getIsTagMatch(ItemStack filterStack) {
    return filterStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBTTAGMATCH, false);
  }

  /**
   * Pulls the FluidStack held in the bucket slot of this card. Empty if no card / no bucket / no fluid.
   */
  public static FluidStack getFluidStack(ItemStack filterStack) {
    if (!(filterStack.getItem() instanceof FluidFilterCardItem)) {
      return FluidStack.EMPTY;
    }
    IItemHandler myFilter = filterStack.getCapability(Capabilities.ItemHandler.ITEM);
    if (myFilter != null) {
      ItemStack bucket = myFilter.getStackInSlot(SLOT_FLUID);
      IFluidHandlerItem fluidInStack = bucket.getCapability(Capabilities.FluidHandler.ITEM);
      if (fluidInStack != null && fluidInStack.getFluidInTank(0) != null) {
        return fluidInStack.getFluidInTank(0);
      }
    }
    return FluidStack.EMPTY;
  }

  /**
   * Allow / ignore semantics + optional tag-match. If the card is not a FluidFilterCardItem (e.g. empty
   * filter slot), everything is allowed.
   */
  public static boolean filterAllowsExtract(ItemStack filterStack, FluidStack fluidInTank) {
    if (!(filterStack.getItem() instanceof FluidFilterCardItem)) {
      return true;
    }
    IItemHandler handler = filterStack.getCapability(Capabilities.ItemHandler.ITEM);
    if (handler == null) {
      return true;
    }
    boolean isIgnoreList = getIsIgnoreList(filterStack);
    boolean isTagMatch = getIsTagMatch(filterStack);
    boolean anyFilled = false;
    for (int i = 0; i < handler.getSlots(); i++) {
      ItemStack bucket = handler.getStackInSlot(i);
      if (bucket.isEmpty()) {
        continue;
      }
      IFluidHandlerItem fluidCap = bucket.getCapability(Capabilities.FluidHandler.ITEM);
      if (fluidCap == null) {
        continue;
      }
      FluidStack fluidFilter = fluidCap.getFluidInTank(0);
      if (fluidFilter == null || fluidFilter.isEmpty()) {
        continue;
      }
      anyFilled = true;
      boolean matches;
      if (isTagMatch) {
        matches = fluidFilter.getFluid().builtInRegistryHolder().tags().anyMatch(fluidInTank::is);
      }
      else {
        matches = fluidFilter.getFluid() == fluidInTank.getFluid();
      }
      if (matches) {
        return !isIgnoreList;
      }
    }
    if (isIgnoreList) {
      return true;
    }
    //allow list: empty filter passes everything
    return !anyFilled;
  }

}
