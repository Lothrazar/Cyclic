package com.lothrazar.cyclic.item;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AppleBuffs extends ItemBase {

  public AppleBuffs(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (this.getFood() != null && this.getFood().getEffects() != null) {
      List<Pair<EffectInstance, Float>> eff = this.getFood().getEffects();
      for (Pair<EffectInstance, Float> entry : eff) {
        EffectInstance effCurrent = entry.getFirst();
        if (effCurrent == null || effCurrent.getPotion() == null) {
          continue;
        }
        TranslationTextComponent t = new TranslationTextComponent(effCurrent.getPotion().getName());
        t.appendString(" " + UtilChat.lang("potion.potency." + effCurrent.getAmplifier()));
        t.mergeStyle(TextFormatting.DARK_GRAY);
        tooltip.add(t);
      }
    }
    //    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 30);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
