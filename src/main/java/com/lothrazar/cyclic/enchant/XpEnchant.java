package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class XpEnchant extends EnchantmentCyclic {

  public static final String ID = "experience_boost";
  public static BooleanValue CFG;

  public XpEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... mainhand) {
    super(rarityIn, typeIn, mainhand);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isTradeable() {
    return isEnabled() && super.isTradeable();
  }

  @Override
  public boolean isDiscoverable() {
    return isEnabled() && super.isDiscoverable();
  }

  @Override
  public boolean isAllowedOnBooks() {
    return isEnabled() && super.isAllowedOnBooks();
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    if (isEnabled() && stack.getItem() instanceof SwordItem) {
      return true; //override even though digger type 
    }
    return isEnabled() && super.canEnchant(stack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @SubscribeEvent
  public void handleBlockBreakEvent(BlockEvent.BreakEvent event) {
    if (!isEnabled()) {
      return;
    }
    int level = getCurrentLevelTool(event.getPlayer().getMainHandItem());
    if (level <= 0) {
      return;
    }
    event.setExpToDrop(event.getExpToDrop() + getRandomExpAmount(level, event.getPlayer().level));
  }

  @SubscribeEvent
  public void handleEntityDropEvent(LivingExperienceDropEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (event.getAttackingPlayer() == null) {
      return;
    }
    int level = getCurrentLevelTool(event.getAttackingPlayer().getMainHandItem());
    if (level <= 0) {
      return;
    }
    event.setDroppedExperience(event.getDroppedExperience() + getRandomExpAmount(level, event.getAttackingPlayer().level));
  }

  private int getRandomExpAmount(int level, Level world) {
    return world.random.nextInt(getMaxLevel()) * (level + 1);
  }

  @Override
  public boolean checkCompatibility(Enchantment ench) {
    return super.checkCompatibility(ench) && ench != EnchantRegistry.EXCAVATE;
  }

  /**
   * Returns the maximum level that the enchantment can have.
   */
  @Override
  public int getMaxLevel() {
    return 3;
  }
}
