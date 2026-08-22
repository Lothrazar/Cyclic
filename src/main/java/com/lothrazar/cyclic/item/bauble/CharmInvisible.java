package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class CharmInvisible extends ItemBaseToggle {

  private static final int SECONDS_DEFAULT = 30;
  public static IntValue SECONDS;

  public CharmInvisible(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, ServerLevel worldIn, Entity entityIn, EquipmentSlot slot) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (worldIn.getGameTime() % Const.TICKS_PER_SEC == 0 && entityIn instanceof LivingEntity) {
      LivingEntity living = (LivingEntity) entityIn;
      if (living.getEffect(MobEffects.INVISIBILITY) == null) {
        int sec = SECONDS == null ? SECONDS_DEFAULT : SECONDS.get();
        MobEffectInstance eff = new MobEffectInstance(MobEffects.INVISIBILITY, Const.TICKS_PER_SEC * sec, 0, false, false, false);
        living.addEffect(eff);
        ItemStackUtil.damageItem(living, stack);
      }
    }
  }
}
