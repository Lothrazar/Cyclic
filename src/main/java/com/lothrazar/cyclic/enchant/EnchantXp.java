package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantXp extends EnchantBase {

  public EnchantXp(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... mainhand) {
    super(rarityIn, typeIn, mainhand);
  }

  @Override
  public int getMinEnchantability(int enchantmentLevel) {
    return 11 + (enchantmentLevel - 1) * 20;
  }

  @SubscribeEvent
  public void handleBlockBreakEvent(BlockEvent.BreakEvent event) {
    int level = getCurrentLevelTool(event.getPlayer());
    if (level <= 0) {
      return;
    }
    giveRandomExp(level, event.getPlayer());
  }

  @Override
  public void onEntityDamaged(LivingEntity user, Entity target, int level) {
    super.onEntityDamaged(user, target, level);
    if (user instanceof PlayerEntity && target != null && !target.isAlive()) {
      giveRandomExp(level, (PlayerEntity) user);
    }
  }

  private void giveRandomExp(int level, PlayerEntity p) {
    p.giveExperiencePoints(p.world.rand.nextInt(3) * (level + 1));
  }

  @Override
  public boolean canApply(ItemStack stack) {
    if (stack.getItem() instanceof SwordItem) {
      return true;//override even though digger type 
    }
    return super.canApply(stack);
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
