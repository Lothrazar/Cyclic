package com.lothrazar.cyclic.enchant;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.library.enchant.EnchantmentFlib;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;

public class DisarmEnchant extends EnchantmentFlib {
  //halves the desired base chance to activate because onEntityDamage gets called twice and it's currently a 
  // "won't fix" situation for Forge https://github.com/MinecraftForge/MinecraftForge/issues/6556#issuecomment-596441220

  public static IntValue PERCENTPERLEVEL;

  public DisarmEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
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
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  public double getChanceToDisarm(int level) {
    float baseChance = PERCENTPERLEVEL.get() / 100F;
    return baseChance + (baseChance * (level - 1));
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && stack.getItem() instanceof SwordItem;
  }

  @Override
  public void doPostAttack(LivingEntity user, Entity target, int level) {
    if (target instanceof LivingEntity == false) {
      return;
    }
    LivingEntity livingTarget = (LivingEntity) target;
    if (!canDisarm(livingTarget)) {
      return;
    }
    List<ItemStack> toDisarm = new ArrayList<>();
    target.getHandSlots().forEach(itemStack -> {
      if (getChanceToDisarm(level) > user.level().random.nextDouble()) {
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
        user.level().addFreshEntity(new ItemEntity(user.level(), livingTarget.getX(),
            livingTarget.getY(), livingTarget.getZ(), itemStack));
      }
    });
    super.doPostAttack(user, target, level);
  }

  private boolean canDisarm(LivingEntity target) {
    String id = EntityType.getKey(target.getType()).toString();
    if (StringParseUtil.isInList(ConfigRegistry.getDisarmIgnoreList(), EntityType.getKey(target.getType()))) {
      ModCyclic.LOGGER.info("disenchant ignored by: CONFIG LIST" + id);
      return false;
    }
    //default yes, its not in ignore list so canDisarm=true
    return true;
  }
}
