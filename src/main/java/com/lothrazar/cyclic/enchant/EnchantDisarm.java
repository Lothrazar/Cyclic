package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class EnchantDisarm extends EnchantBase {

  //halves the desired base chance to activate because onEntityDamage gets called twice and it's currently a 
  // "won't fix" situation for Forge https://github.com/MinecraftForge/MinecraftForge/issues/6556#issuecomment-596441220
  private static final double BASE_CHANCE = 0.08 / 2;

  public EnchantDisarm(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "disarm";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  public double getChanceToDisarm(int level) {
    return BASE_CHANCE + (BASE_CHANCE / 2 * (level - 1));
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return stack.getItem() instanceof SwordItem;
  }

  @Override
  public void doPostAttack(LivingEntity user, Entity target, int level) {
    if (target instanceof LivingEntity == false) {
      return;
    }
    LivingEntity livingTarget = (LivingEntity) target;
    List<ItemStack> toDisarm = new ArrayList<>();
    target.getHandSlots().forEach(itemStack -> {
      if (getChanceToDisarm(level) > user.level.random.nextDouble()) {
        toDisarm.add(itemStack);
      }
    });
    toDisarm.forEach(itemStack -> {
      boolean dropHeld = false;
      if (itemStack.equals(livingTarget.getMainHandItem())) {
        livingTarget.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        dropHeld = true;
      }
      else if (itemStack.equals(livingTarget.getOffhandItem())) {
        livingTarget.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        dropHeld = true;
      }
      if (dropHeld) {
        user.level.addFreshEntity(new ItemEntity(user.level, livingTarget.getX(),
            livingTarget.getY(), livingTarget.getZ(), itemStack));
      }
    });
    super.doPostAttack(user, target, level);
  }
}
