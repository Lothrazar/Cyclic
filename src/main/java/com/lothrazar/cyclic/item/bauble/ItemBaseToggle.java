package com.lothrazar.cyclic.item.bauble;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.core.IHasClickToggle;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    MutableComponent t = Component.translatable("item.cyclic.bauble.on." + this.isOn(stack));
    t.withStyle(ChatFormatting.DARK_GRAY);
    tooltip.add(t);
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    CompoundTag tag = held.getOrCreateTag();
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.setTag(tag);
  }

  @Override
  public boolean isOn(ItemStack held) {
    if (held.getTag() == null) {
      return false;
    }
    return held.getTag().getInt(NBT_STATUS) == 0; //its flipped as 0 on, 1 off becuase! because we want teh default to be ON. so player can craft and use right away. 
    //aka pickup and use instantly.  and then turning it off is optional later
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack) {
    return isOn(stack);
  }
}
