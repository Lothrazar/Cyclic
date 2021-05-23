package com.lothrazar.cyclic.item.datacard.filter;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.List;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class FilterCardItem extends ItemBase {

  public static final int SLOT_FLUID = 8;
  private static final String NBTFILTER = "filter";

  public FilterCardItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag()) {
      TranslationTextComponent t = new TranslationTextComponent("cyclic.screen.filter." + getIsIgnoreList(stack));
      t.mergeStyle(TextFormatting.GRAY);
      tooltip.add(t);
      int count = 0;
      ITextComponent first = null;
      IItemHandler myFilter = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
      if (myFilter != null) {
        for (int i = 0; i < myFilter.getSlots(); i++) {
          ItemStack filterPtr = myFilter.getStackInSlot(i);
          if (!filterPtr.isEmpty()) {
            count++;
            if (first == null) {
              first = filterPtr.getDisplayName();
            }
          }
        }
      }
      if (first != null) {
        tooltip.add(first);
      }
      if (count == 0) {
        t = new TranslationTextComponent("cyclic.screen.filter.item.empty");
        t.mergeStyle(TextFormatting.GRAY);
        tooltip.add(t);
      }
      else {
        t = new TranslationTextComponent("cyclic.screen.filter.item.count");
        t.appendString("" + count);
        t.mergeStyle(TextFormatting.GRAY);
        tooltip.add(t);
      }
      FluidStack fluidStack = FilterCardItem.getFluidStack(stack);
      if (fluidStack.isEmpty()) {
        t = new TranslationTextComponent("cyclic.screen.filter.fluid.empty");
        t.mergeStyle(TextFormatting.GRAY);
        tooltip.add(t);
      }
      else {
        StringTextComponent s = new StringTextComponent(fluidStack.getDisplayName().getString());
        s.mergeStyle(TextFormatting.GRAY);
        tooltip.add(s);
      }
    }
    else {
      super.addInformation(stack, worldIn, tooltip, flagIn);
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new ContainerProviderFilterCard(), playerIn.getPosition());
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
    return new CapabilityProviderFilterCard();
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.filter_data, ScreenFilterCard::new);
  }

  public static void toggleFilterType(ItemStack filter) {
    boolean prev = getIsIgnoreList(filter);
    filter.getTag().putBoolean(NBTFILTER, !prev);
  }

  public static FluidStack getFluidStack(ItemStack filterStack) {
    if (filterStack.getItem() instanceof FilterCardItem == false) {
      return FluidStack.EMPTY; //filter is air, everything allowed
    }
    IItemHandler myFilter = filterStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (myFilter != null) {
      ItemStack bucket = myFilter.getStackInSlot(SLOT_FLUID);
      IFluidHandler fluidInStack = bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).orElse(null);
      if (fluidInStack != null && fluidInStack.getFluidInTank(0) != null) {
        return fluidInStack.getFluidInTank(0);
      }
    }
    return FluidStack.EMPTY;
  }

  public static boolean filterAllowsExtract(ItemStack filterStack, ItemStack itemTarget) {
    if (filterStack.getItem() instanceof FilterCardItem == false) {
      return true; //filter is air, everything allowed
    }
    //does my filter allow extract
    boolean isEmpty = false;
    boolean isMatchingList = false;
    boolean isIgnoreList = getIsIgnoreList(filterStack);
    IItemHandler myFilter = filterStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (myFilter != null) {
      for (int i = 0; i < myFilter.getSlots(); i++) {
        ItemStack filterPtr = myFilter.getStackInSlot(i);
        if (!filterPtr.isEmpty()) {
          isEmpty = false; //at least one thing is in the filter 
          //does it match
          if (UtilItemStack.matches(itemTarget, filterPtr)) {
            isMatchingList = true;
            break;
          }
        }
      }
    }
    //    ModCyclic.LOGGER.info(isMatchingList + "=isMatchingList " + itemTarget);
    if (isIgnoreList) {
      // we are allowed to filter if it doesnt match
      return !isMatchingList;
    }
    else {
      //its an Allow list. filter if in the list
      //but if its empty, allow just lets everything
      return isEmpty || isMatchingList;
    }
  }

  private static boolean getIsIgnoreList(ItemStack filterStack) {
    return filterStack.getOrCreateTag().getBoolean(NBTFILTER);
  }
}
