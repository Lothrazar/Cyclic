package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import com.lothrazar.cyclic.util.UtilFakePlayer;
import com.lothrazar.cyclic.util.UtilString;
import java.util.Collections;
import java.util.List;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;

public class EnchantGloom extends EnchantBase {

  private static final double BASE_ACTIVATION_CHANCE = 0.1;
  private static final double BASE_APPLY_CHANCE = 0.3;
  private static final double MIN_EFFECTS = 1;
  private static final double MAX_EFFECTS = 3;
  private static final int EFFECT_DURATION = 20 * 5;

  public EnchantGloom(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "curse";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public void onUserHurt(LivingEntity user, Entity attacker, int level) {
    if (user.world.isRemote || !(attacker instanceof LivingEntity)
        || UtilFakePlayer.isFakePlayer(attacker)) {
      //do nothing on clientside, server only
      //only trigger if attacker is alive and not a fakeplayer
      return;
    }
    LivingEntity livingAttacker = (LivingEntity) attacker;
    //only allow activation once
    int totalLevels = getCurrentArmorLevelSlot(user, EquipmentSlotType.HEAD)
        + getCurrentArmorLevelSlot(user, EquipmentSlotType.CHEST)
        + getCurrentArmorLevelSlot(user, EquipmentSlotType.LEGS)
        + getCurrentArmorLevelSlot(user, EquipmentSlotType.FEET);
    double adjustedActivationChance = BASE_ACTIVATION_CHANCE / totalLevels;
    //does it pass the chance
    if (adjustedActivationChance > user.world.rand.nextDouble()) {
      List<Effect> negativeEffects = UtilEnchant.getNegativeEffects();
      Collections.shuffle(negativeEffects);
      int appliedEffects = 0;
      for (Effect effect : negativeEffects) {
        if (effect == null) {
          continue;
          //should be impossible, but i had a random NPE crash log
        }
        if (UtilString.isInList(ConfigRegistry.getGloomIgnoreList(), effect.getRegistryName())) {
          ModCyclic.LOGGER.info("Gloom(curse) effect cannot apply " + effect.getRegistryName());
          continue;
        }
        if (appliedEffects < MIN_EFFECTS
            || BASE_APPLY_CHANCE > user.world.rand.nextDouble()) {
          //the OR means, if we are under minimum, always go thru
          //if we are beyond minimum, check the random chance
          livingAttacker.addPotionEffect(new EffectInstance(effect, EFFECT_DURATION));
          appliedEffects++;
          if (appliedEffects >= MAX_EFFECTS) {
            break;
          }
        }
      }
    }
    super.onUserHurt(user, attacker, level);
  }
}
