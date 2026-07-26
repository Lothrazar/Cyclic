package com.lothrazar.cyclic.item.bauble;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.core.IHasClickToggle;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;


public class ItemBaseToggle extends ItemBaseCyclic implements IHasClickToggle {

  public ItemBaseToggle(Properties properties) {
    super(properties);
  }

  /**
   * ElytraItem::isUsable static
   *
   * @param stack
   * @return
   */
  public boolean canUse(ItemStack stack) {
    return stack.getDamageValue() < stack.getMaxDamage() - 1;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltipDisplay, tooltip, flagIn);
    MutableComponent t = Component.translatable("item.cyclic.bauble.on." + this.isOn(stack));
    t.withStyle(ChatFormatting.DARK_GRAY);
    tooltip.accept(t);
  }

  @Override
  public InteractionResult use(Level level, Player player, InteractionHand hand) {
    ItemStack itemstack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      this.toggle(player, itemstack);
    }
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    CustomData customData = held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    CompoundTag tag = customData.copyTag();
    tag.putInt(NBT_STATUS, (tag.getIntOr(NBT_STATUS, 0) + 1) % 2);
    held.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  @Override
  public boolean isOn(ItemStack held) {
    CustomData customData = held.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    if (customData.isEmpty() || !customData.copyTag().contains(NBT_STATUS)) {
      return true; // Default to ON for newly crafted items
    }
    return customData.copyTag().getIntOr(NBT_STATUS, 0) == 0; // 0 is ON, 1 is OFF
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack) {
    return isOn(stack);
  }
}
