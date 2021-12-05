package com.lothrazar.cyclic.item.datacard.filter;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class FilterCardItem extends ItemBaseCyclic {

  public static final int SLOT_FLUID = 8;
  private static final String NBTFILTER = "filter";

  public FilterCardItem(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (stack.hasTag()) {
      boolean isIgnore = getIsIgnoreList(stack);
      TranslatableComponent t = new TranslatableComponent("cyclic.screen.filter." + isIgnore);
      t.withStyle(isIgnore ? ChatFormatting.DARK_GRAY : ChatFormatting.DARK_BLUE);
      tooltip.add(t);
      // caps arent synced from server very well
      //
      CompoundTag stackTag = stack.getOrCreateTag();
      if (stackTag.contains("fluidTooltip")) {
        String fluidTooltip = stackTag.getString("fluidTooltip");
        tooltip.add(new TranslatableComponent(fluidTooltip).withStyle(ChatFormatting.AQUA));
      }
      if (stackTag.contains("itemTooltip")) {
        String itemTooltip = stackTag.getString("itemTooltip");
        tooltip.add(new TranslatableComponent(itemTooltip).withStyle(ChatFormatting.GRAY));
      }
      if (stackTag.contains("itemCount")) {
        int itemCount = stackTag.getInt("itemCount");
        t = new TranslatableComponent("cyclic.screen.filter.item.count");
        t.append("" + itemCount);
        t.withStyle(ChatFormatting.GRAY);
        tooltip.add(t);
      }
    }
    else {
      super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayer) playerIn, new ContainerProviderFilterCard(), playerIn.blockPosition());
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new CapabilityProviderFilterCard();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.filter_data, ScreenFilterCard::new);
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
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag nbt = stack.getOrCreateTag();
    FluidStack fluidStack = FilterCardItem.getFluidStack(stack);
    if (!fluidStack.isEmpty()) {
      nbt.putString("fluidTooltip", fluidStack.getDisplayName().getString());
    }
    IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
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

  @Override
  public void readShareTag(ItemStack stack, CompoundTag nbt) {
    if (nbt != null) {
      CompoundTag stackTag = stack.getOrCreateTag();
      stackTag.putString("itemTooltip", nbt.getString("itemTooltip"));
      stackTag.putString("fluidTooltip", nbt.getString("fluidTooltip"));
      stackTag.putInt("itemCount", nbt.getInt("itemCount"));
    }
    super.readShareTag(stack, nbt);
  }
}
