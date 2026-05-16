package com.lothrazar.cyclic.item.datacard.filter;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

public class FilterCardItem extends ItemBaseCyclic {

  public static final int SLOT_FLUID = 8;
  private static final String NBTFILTER = "filter";

  public FilterCardItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      boolean isIgnore = getIsIgnoreList(stack);
      MutableComponent t = Component.translatable("cyclic.screen.filter." + isIgnore);
      t.withStyle(isIgnore ? ChatFormatting.DARK_GRAY : ChatFormatting.DARK_BLUE);
      tooltip.add(t);
      // caps arent synced from server very well
      //
      CompoundTag stackTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (stackTag.contains("fluidTooltip")) {
        String fluidTooltip = stackTag.getString("fluidTooltip");
        tooltip.add(Component.translatable(fluidTooltip).withStyle(ChatFormatting.AQUA));
      }
      if (stackTag.contains("itemCount")) {
        int itemCount = stackTag.getInt("itemCount");
        if (itemCount > 0) {
          if (stackTag.contains("itemTooltip")) {
            String itemTooltip = stackTag.getString("itemTooltip");
            tooltip.add(Component.translatable(itemTooltip).withStyle(ChatFormatting.GRAY));
          }
          tooltip.add(Component.translatable("cyclic.screen.filter.item.count").append("" + itemCount).withStyle(ChatFormatting.GRAY));
        }
      }
    }
    else {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && !playerIn.isCrouching()) {
      /* playerIn.openMenu(new ContainerProviderFilterCard(), playerIn.blockPosition()); */
    }
    return super.use(worldIn, playerIn, handIn);
  }



  public static void toggleFilterType(ItemStack filter) {
    boolean prev = getIsIgnoreList(filter);
    CompoundTag tag = filter.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag(); tag.putBoolean(NBTFILTER, !prev); filter.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public static FluidStack getFluidStack(ItemStack filterStack) {
    if (filterStack.getItem() instanceof FilterCardItem == false) {
      return FluidStack.EMPTY; //filter is air, everything allowed
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

  public static boolean filterAllowsExtract(ItemStack filterStack, ItemStack itemTarget) {
    if (filterStack.getItem() instanceof FilterCardItem == false) {
      return true; //filter is air, everything allowed
    }
    //does my filter allow extract
    boolean isEmpty = false;
    boolean isMatchingList = false;
    boolean isIgnoreList = getIsIgnoreList(filterStack);
    IItemHandler myFilter = filterStack.getCapability(Capabilities.ItemHandler.ITEM);
    if (myFilter != null) {
      for (int i = 0; i < myFilter.getSlots(); i++) {
        ItemStack filterPtr = myFilter.getStackInSlot(i);
        if (!filterPtr.isEmpty()) {
          isEmpty = false; //at least one thing is in the filter 
          //does it match
          if (ItemStackUtil.matches(itemTarget.getItem().getDefaultInstance(), filterPtr)) {
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
    return filterStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(NBTFILTER);
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
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    FluidStack fluidStack = FilterCardItem.getFluidStack(stack);
    if (!fluidStack.isEmpty()) {
      nbt.putString("fluidTooltip", fluidStack.getHoverName().getString());
    }
    IItemHandler cap = stack.getCapability(Capabilities.ItemHandler.ITEM);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (cap != null) {
      int count = 0;
      Component first = null;
      for (int i = 0; i < cap.getSlots(); i++) {
        if (!cap.getStackInSlot(i).isEmpty()) {
          //non empty stack eh
          count++;
          if (first == null) {
            first = cap.getStackInSlot(i).getHoverName();
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

  public void readShareTag(ItemStack stack, CompoundTag nbt) {
    if (nbt != null) {
      CompoundTag stackTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      stackTag.putString("itemTooltip", nbt.getString("itemTooltip"));
      stack.set(DataComponents.CUSTOM_DATA, CustomData.of(stackTag));
      stackTag.putString("fluidTooltip", nbt.getString("fluidTooltip"));
      stackTag.putInt("itemCount", nbt.getInt("itemCount"));
    }
    
  }
}
