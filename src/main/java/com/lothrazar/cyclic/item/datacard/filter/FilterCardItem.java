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
  // TODO: could match on tags, nbt exact match like enchants, 

  public FilterCardItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag()) {
      boolean isIgnore = getIsIgnoreList(stack);
      TranslationTextComponent t = new TranslationTextComponent("cyclic.screen.filter." + isIgnore);
      t.mergeStyle(isIgnore ? TextFormatting.DARK_GRAY : TextFormatting.DARK_BLUE);
      tooltip.add(t);
      // caps arent synced from server very well
      //
      CompoundNBT stackTag = stack.getOrCreateTag();
      if (stackTag.contains("fluidTooltip")) {
        String fluidTooltip = stackTag.getString("fluidTooltip");
        tooltip.add(new TranslationTextComponent(fluidTooltip).mergeStyle(TextFormatting.AQUA));
      }
      if (stackTag.contains("itemTooltip")) {
        String itemTooltip = stackTag.getString("itemTooltip");
        tooltip.add(new TranslationTextComponent(itemTooltip).mergeStyle(TextFormatting.GRAY));
      }
      if (stackTag.contains("itemCount")) {
        int itemCount = stackTag.getInt("itemCount");
        t = new TranslationTextComponent("cyclic.screen.filter.item.count");
        t.appendString("" + itemCount);
        t.mergeStyle(TextFormatting.GRAY);
        tooltip.add(t);
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

  public static boolean filterAllowsExtract(ItemStack filterStack, FluidStack fluidInTank) {
    if (filterStack.getItem() instanceof FilterCardItem == false) {
      return true; //filter is air, everything allowed
    }
    FluidStack fluidFilter = getFluidStack(filterStack);
    boolean isMatchingList = fluidFilter.getFluid() == fluidInTank.getFluid();
    boolean isIgnoreList = getIsIgnoreList(filterStack);
    // 
    if (isIgnoreList) {
      return !isMatchingList;
    }
    else { // allow list 
      return fluidFilter.isEmpty() || isMatchingList;
    }
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = stack.getOrCreateTag();
    FluidStack fluidStack = FilterCardItem.getFluidStack(stack);
    if (!fluidStack.isEmpty()) {
      nbt.putString("fluidTooltip", fluidStack.getDisplayName().getString());
    }
    IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (cap != null) {
      int count = 0;
      ITextComponent first = null;
      for (int i = 0; i < cap.getSlots(); i++) {
        if (!cap.getStackInSlot(i).isEmpty()) {
          //non empty stack eh
          count++;
          if (first == null) {
            first = cap.getStackInSlot(i).getDisplayName();
          }
        }
      }
      nbt.putInt("itemCount", count);
      if (first != null) {
        nbt.putString("itemTooltip", first.getString());
      }
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack stack, CompoundNBT nbt) {
    CompoundNBT stackTag = stack.getOrCreateTag();
    stackTag.putString("itemTooltip", nbt.getString("itemTooltip"));
    stackTag.putString("fluidTooltip", nbt.getString("fluidTooltip"));
    stackTag.putInt("itemCount", nbt.getInt("itemCount"));
    super.readShareTag(stack, nbt);
  }
}
