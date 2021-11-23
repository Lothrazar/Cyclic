package com.lothrazar.cyclic.item.apple;

import java.util.List;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class AppleBuffs extends ItemBaseCyclic {

  public AppleBuffs(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    if (this.getFoodProperties() != null && this.getFoodProperties().getEffects() != null) {
      List<Pair<MobEffectInstance, Float>> eff = this.getFoodProperties().getEffects();
      for (Pair<MobEffectInstance, Float> entry : eff) {
        MobEffectInstance effCurrent = entry.getFirst();
        if (effCurrent == null || effCurrent.getEffect() == null) {
          continue;
        }
        TranslatableComponent t = new TranslatableComponent(effCurrent.getEffect().getDescriptionId());
        t.append(" " + UtilChat.lang("potion.potency." + effCurrent.getAmplifier()));
        t.withStyle(ChatFormatting.DARK_GRAY);
        tooltip.add(t);
      }
    }
    //    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof Player) {
      // TOOD
      ((Player) entityLiving).getCooldowns().addCooldown(this, 30);
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }
}
