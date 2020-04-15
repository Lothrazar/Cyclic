package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.Const;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CharmBase extends ItemBase {

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

  private boolean canUse(ItemStack stack) {
    return stack.getDamage() < stack.getMaxDamage();
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (this.voidProt && entityIn.getPosition().getY() < yLowest) {
      UtilEntity.teleportWallSafe(entityIn, worldIn,
          new BlockPos(entityIn.getPosition().getX(), yDest, entityIn.getPosition().getZ()));
      UtilItemStack.damageItem(stack);
      UtilSound.playSound(entityIn, entityIn.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT);
      // UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL,
      // living.getPosition());
    }
    if (entityIn instanceof LivingEntity == false) {
      return;
    }
    LivingEntity living = (LivingEntity) entityIn;
    if (this.poisonProt && living.isPotionActive(Effects.POISON)) {
      living.removeActivePotionEffect(Effects.POISON);
      UtilItemStack.damageItem(stack);
      UtilSound.playSound(entityIn, entityIn.getPosition(), SoundEvents.ENTITY_GENERIC_DRINK);
    }
    else if (this.witherProt && living.isPotionActive(Effects.WITHER)) {
      living.removeActivePotionEffect(Effects.WITHER);
      UtilItemStack.damageItem(stack);
      UtilSound.playSound(entityIn, entityIn.getPosition(), SoundEvents.ENTITY_GENERIC_DRINK);
    }
    else if (this.fireProt && living.isBurning() && !living.isPotionActive(Effects.FIRE_RESISTANCE)) { // do nothing if you already have
      //      World worldIn = living.getEntityWorld();
      living.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, fireProtSeconds * Const.TICKS_PER_SEC, Const.Potions.I));
      //      super.damageCharm(living, stack);
      UtilItemStack.damageItem(stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
      //      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_WAKE, living.getPosition());
      //      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.WATER_WAKE, living.getPosition().up());
    }
  }
}
