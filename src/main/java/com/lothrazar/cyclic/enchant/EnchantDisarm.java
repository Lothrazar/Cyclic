package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;

public class EnchantDisarm extends EnchantBase {

  //halves the desired base chance to activate because onEntityDamage gets called twice and it's currently a 
  // "won't fix" situation for Forge https://github.com/MinecraftForge/MinecraftForge/issues/6556#issuecomment-596441220
  private static final double BASE_CHANCE = 0.08 / 2;

  public EnchantDisarm(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
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
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof SwordItem;
  }

  @Override
  public void onEntityDamaged(LivingEntity user, Entity target, int level) {
    if (!(target instanceof LivingEntity)) {
      return;
    }
    LivingEntity livingTarget = (LivingEntity) target;
    List<ItemStack> toDisarm = new ArrayList<>();
    target.getHeldEquipment().forEach(itemStack -> {
      if (getChanceToDisarm(level) > user.world.rand.nextDouble()) {
        toDisarm.add(itemStack);
      }
    });
    toDisarm.forEach(itemStack -> {
      boolean dropHeld = false;
      if (itemStack.equals(livingTarget.getHeldItemMainhand())) {
        livingTarget.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        dropHeld = true;
      }
      else if (itemStack.equals(livingTarget.getHeldItemOffhand())) {
        livingTarget.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);
        dropHeld = true;
      }
      if (dropHeld) {
        user.world.addEntity(new ItemEntity(user.world, livingTarget.getPosX(),
            livingTarget.getPosY(), livingTarget.getPosZ(), itemStack));
      }
    });
    super.onEntityDamaged(user, target, level);
  }
}
