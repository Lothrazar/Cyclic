package com.lothrazar.cyclic.item.bauble;

import java.util.List;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


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
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    MutableComponent t = Component.translatable("item.cyclic.bauble.on." + this.isOn(stack));
    t.withStyle(ChatFormatting.DARK_GRAY);
    tooltip.add(t);
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    net.minecraft.world.item.component.CustomData customData = held.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
    CompoundTag tag = customData.copyTag();
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
  }

  @Override
  public boolean isOn(ItemStack held) {
    net.minecraft.world.item.component.CustomData customData = held.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
    if (customData.isEmpty() || !customData.copyTag().contains(NBT_STATUS)) {
      return true; // Default to ON for newly crafted items
    }
    return customData.copyTag().getInt(NBT_STATUS) == 0; // 0 is ON, 1 is OFF
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack) {
    return isOn(stack);
  }
}
