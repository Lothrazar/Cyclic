package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CharmBase extends ItemBaseToggle {

  private static final int yLowest = -30;
  private static final int yDest = 255;
  private static final int fireProtSeconds = 10;
  boolean fireProt;
  boolean poisonProt;
  boolean witherProt;
  boolean voidProt;

  public CharmBase(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    tryVoidTick(stack, worldIn, entityIn);
    if (entityIn instanceof LivingEntity == false) {
      return;
    }
    LivingEntity living = (LivingEntity) entityIn;
    tryPoisonTick(stack, entityIn, living);
    tryWitherTick(stack, entityIn, living);
    tryFireTick(stack, living);
  }

  private void tryFireTick(ItemStack stack, LivingEntity living) {
    if (this.fireProt && living.isBurning() && !living.isPotionActive(Effects.FIRE_RESISTANCE)) { // do nothing if you already have
      living.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, fireProtSeconds * Const.TICKS_PER_SEC, Const.Potions.I));
      UtilItemStack.damageItem(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
      UtilParticle.spawnParticle(living.world, ParticleTypes.DRIPPING_WATER, living.getPosition(), 9);
    }
  }

  private void tryWitherTick(ItemStack stack, Entity entityIn, LivingEntity living) {
    if (this.witherProt && living.isPotionActive(Effects.WITHER)) {
      living.removeActivePotionEffect(Effects.WITHER);
      UtilItemStack.damageItem(living, stack);
      UtilSound.playSound(entityIn, entityIn.getPosition(), SoundEvents.ENTITY_GENERIC_DRINK);
    }
  }

  private void tryPoisonTick(ItemStack stack, Entity entityIn, LivingEntity living) {
    if (this.poisonProt && living.isPotionActive(Effects.POISON)) {
      living.removeActivePotionEffect(Effects.POISON);
      UtilItemStack.damageItem(living, stack);
      UtilSound.playSound(entityIn, entityIn.getPosition(), SoundEvents.ENTITY_GENERIC_DRINK);
    }
  }

  private void tryVoidTick(ItemStack stack, World worldIn, Entity entityIn) {
    if (this.voidProt && entityIn.getPosition().getY() < yLowest && entityIn instanceof LivingEntity) {
      UtilEntity.enderTeleportEvent((LivingEntity) entityIn, worldIn,
          new BlockPos(entityIn.getPosition().getX(), yDest, entityIn.getPosition().getZ()));
      if (entityIn instanceof LivingEntity) {
        UtilItemStack.damageItem((LivingEntity) entityIn, stack);
      }
      UtilSound.playSound(entityIn, entityIn.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT);
    }
  }
}
