package com.lothrazar.cyclic.enchantment;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class EnchantXp extends EnchantBase {

  public EnchantXp(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... mainhand) {
    super(rarityIn, typeIn, mainhand);
  }

  @Override
  public int getMinEnchantability(int enchantmentLevel) {
    return 11 + (enchantmentLevel - 1) * 20;
  }

  @Override
  public void onEntityDamaged(LivingEntity user, Entity target, int level) {
    super.onEntityDamaged(user, target, level);
    if (user instanceof PlayerEntity && target != null && !target.isAlive()) {
      PlayerEntity p = (PlayerEntity) user;
      // ModCyclic.LOGGER.info("before " + p.experience);
      p.giveExperiencePoints(user.world.rand.nextInt(3) * (level + 1));
      ModCyclic.LOGGER.info("kil   " + p.experience);
    }
  }

  @Override
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 24;
  }

  @Override
  public boolean canApplyTogether(Enchantment ench) {
    return super.canApplyTogether(ench) && ench != Enchantments.LOOTING;
  }

  /**
   * Returns the maximum level that the enchantment can have.
   */
  @Override
  public int getMaxLevel() {
    return 3;
  }
}
