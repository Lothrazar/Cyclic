package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.base.ItemBase;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBaseToggle extends ItemBase implements IHasClickToggle {

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
    return stack.getDamage() < stack.getMaxDamage() - 1;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    TranslationTextComponent t = new TranslationTextComponent("item.cyclic.bauble.on." + this.isOn(stack));
    t.mergeStyle(TextFormatting.DARK_GRAY);
    tooltip.add(t);
  }

  @Override
  public void toggle(PlayerEntity player, ItemStack held) {
    CompoundNBT tag = held.getOrCreateTag();
    tag.putInt(NBT_STATUS, (tag.getInt(NBT_STATUS) + 1) % 2);
    held.setTag(tag);
  }

  @Override
  public boolean isOn(ItemStack held) {
    if (held.getTag() == null) {
      return false;
    }
    return held.getTag().getInt(NBT_STATUS) == 0;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return isOn(stack);
  }
}
