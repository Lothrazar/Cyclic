package com.lothrazar.cyclic.enchant;

import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.util.UtilEnchant;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.MinecraftForge;

public class EnchantCurse extends EnchantBase {

  private static final double BASE_ACTIVATION_CHANCE = 0.1;
  private static final double BASE_APPLY_CHANCE = 0.3;
  private static final double MIN_EFFECTS = 1;
  private static final double MAX_EFFECTS = 3;
  private static final int EFFECT_DURATION = 20 * 5;

  public EnchantCurse(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof ArmorItem;
  }

  @Override
  public void onUserHurt(LivingEntity user, Entity attacker, int level) {
    if (!(attacker instanceof LivingEntity))
      return;
    //only allow activation once
    int totalLevels = getCurrentArmorLevelSlot(user, EquipmentSlotType.HEAD)
        + getCurrentArmorLevelSlot(user, EquipmentSlotType.CHEST)
        + getCurrentArmorLevelSlot(user, EquipmentSlotType.LEGS)
        + getCurrentArmorLevelSlot(user, EquipmentSlotType.FEET);
    double adjustedActivationChance = BASE_ACTIVATION_CHANCE / totalLevels;
    if (adjustedActivationChance > user.world.rand.nextDouble()) {
      LivingEntity livingAttacker = (LivingEntity) attacker;
      List<Effect> negativeEffects = UtilEnchant.getNegativeEffects();
      Collections.shuffle(negativeEffects);
      int appliedEffects = 0;
      for (Effect effect : negativeEffects) {
        if (appliedEffects < MIN_EFFECTS
            || BASE_APPLY_CHANCE > user.world.rand.nextDouble()) {
          livingAttacker.addPotionEffect(new EffectInstance(effect, EFFECT_DURATION));
          if (++appliedEffects >= MAX_EFFECTS)
            break;
        }
      }
    }
    super.onUserHurt(user, attacker, level);
  }
}
