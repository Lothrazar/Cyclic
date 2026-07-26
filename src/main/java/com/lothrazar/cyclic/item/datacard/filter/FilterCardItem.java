package com.lothrazar.cyclic.item.datacard.filter;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ItemStackUtil;
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
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import com.lothrazar.cyclic.util.CapabilityUtil;

public class FilterCardItem extends ItemBaseCyclic {

  private static final String NBTFILTER = "filter";
  private static final String NBTTAGMATCH = "tagmatch";

  public FilterCardItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    if (stack.has(DataComponents.CUSTOM_DATA)) {
      boolean isIgnore = getIsIgnoreList(stack);
      MutableComponent t = Component.translatable("cyclic.screen.filter." + isIgnore);
      t.withStyle(isIgnore ? ChatFormatting.DARK_GRAY : ChatFormatting.DARK_BLUE);
      tooltip.accept(t);
      // caps arent synced from server very well
      CompoundTag stackTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (stackTag.contains("itemCount")) {
        int itemCount = stackTag.getIntOr("itemCount", 0);
        if (itemCount > 0) {
          if (stackTag.contains("itemTooltip")) {
            String itemTooltip = stackTag.getStringOr("itemTooltip", "");
            tooltip.accept(Component.translatable(itemTooltip).withStyle(ChatFormatting.GRAY));
          }
          tooltip.accept(Component.translatable("cyclic.screen.filter.item.count").append("" + itemCount).withStyle(ChatFormatting.GRAY));
        }
      }
    }
    else {
      super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    }
  }

  @Override
  public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide() && !playerIn.isCrouching()) {
      playerIn.openMenu(new ContainerProviderFilterCard(), playerIn.blockPosition());
    }
    return super.use(worldIn, playerIn, handIn);
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

  public static boolean getIsTagMatch(ItemStack filterStack) {
    return filterStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBTTAGMATCH, false);
  }

  public static boolean filterAllowsExtract(ItemStack filterStack, ItemStack itemTarget) {
    if (!(filterStack.getItem() instanceof FilterCardItem)) {
      return true; //filter is air, everything allowed
    }
    //does my filter allow extract
    boolean isEmpty = false;
    boolean isMatchingList = false;
    boolean isIgnoreList = getIsIgnoreList(filterStack);
    boolean isTagMatch = getIsTagMatch(filterStack);
    IItemHandler myFilter = CapabilityUtil.item(filterStack);
    if (myFilter != null) {
      for (int i = 0; i < myFilter.getSlots(); i++) {
        ItemStack filterPtr = myFilter.getStackInSlot(i);
        if (!filterPtr.isEmpty()) {
          isEmpty = false; //at least one thing is in the filter
          //does it match
          if (isTagMatch) {
            //share any item tag with filter entry
            // 26.1: ItemStack#getTags() removed - tags now live on the Item's own registry Holder
            if (filterPtr.getItem().builtInRegistryHolder().tags().anyMatch(itemTarget::is)) {
              isMatchingList = true;
              break;
            }
          }
          else if (ItemStackUtil.matches(itemTarget.getItem().getDefaultInstance(), filterPtr)) {
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

  public static boolean getIsIgnoreList(ItemStack filterStack) {
    return filterStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBooleanOr(NBTFILTER, false);
  }

}
