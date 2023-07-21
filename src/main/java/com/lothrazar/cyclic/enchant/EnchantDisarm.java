package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilString;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.MinecraftForge;

public class EnchantDisarm extends EnchantBase {

  public EnchantDisarm(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static IntValue PERCENTPERLEVEL;
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
    float baseChance = PERCENTPERLEVEL.get() / 100F;
    return baseChance + (baseChance * (level - 1));
  }

  @Override
  public void onEntityDamaged(LivingEntity user, Entity target, int level) {
    if (target instanceof LivingEntity == false) {
      return;
    }
    LivingEntity livingTarget = (LivingEntity) target;
    if (!canDisarm(livingTarget)) {
      return;
    }
    List<ItemStack> toDisarm = new ArrayList<>();
    target.getHeldEquipment().forEach(itemStack -> {
      double pct = getChanceToDisarm(level);
      if (pct > user.world.rand.nextDouble()) {
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

  private boolean canDisarm(LivingEntity target) {
    String id = EntityType.getKey(target.getType()).toString();
    if (UtilString.isInList(ConfigRegistry.getDisarmIgnoreList(), EntityType.getKey(target.getType()))) {
      ModCyclic.LOGGER.info("disenchant ignored by: CONFIG LIST" + id);
      return false;
    }
    //default yes, its not in ignore list so canDisarm=true
    return true;
  }
}
