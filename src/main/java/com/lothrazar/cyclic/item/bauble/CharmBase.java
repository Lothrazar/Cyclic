package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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
  public static final UUID ID_SPEED = UUID.fromString("12230aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID ID_LUCK = UUID.fromString("acc30aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID ID_ATTACKSPEED = UUID.fromString("b4678aa2-eff2-4a81-b92b-a1cb95f115c6");
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
      EffectInstance eff = new EffectInstance(Effects.FIRE_RESISTANCE, fireProtSeconds * Const.TICKS_PER_SEC, Const.Potions.I);
      eff.showParticles = false;
      living.addPotionEffect(eff);
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

  private static void toggleAttribute(PlayerEntity player, Item charm, Attribute attr, UUID id, float factor, int flatIncrease) {
    ItemStack charmStack = CharmUtil.getIfEnabled(player, charm);
    ModifiableAttributeInstance attrPlayer = player.getAttribute(attr);
    AttributeModifier oldValue = attrPlayer.getModifier(id);
    if (charmStack.isEmpty()) {
      ///i am NOT holding it. remove my modifier
      if (oldValue != null) {
        attrPlayer.removeModifier(id);
      }
    }
    else { // im   holding it
      if (oldValue == null) {
        /// add new
        double baseSpeed = attrPlayer.getBaseValue();
        AttributeModifier newValue = new AttributeModifier(id, "Bonus from " + ModCyclic.MODID, baseSpeed * factor + flatIncrease, AttributeModifier.Operation.ADDITION);
        attrPlayer.applyPersistentModifier(newValue);
        //        ModCyclic.LOGGER.info(baseSpeed + " becinesNEW value " + newValue.getAmount() + " -> " + attrPlayer.getValue());
        UtilItemStack.damageItem(player, charmStack);
      }
    }
  }

  public static void charmSpeed(PlayerEntity player) {
    toggleAttribute(player, ItemRegistry.CHARM_SPEED.get(), Attributes.MOVEMENT_SPEED, ID_SPEED, 0.5F, 0);
  }

  public static void charmLuck(PlayerEntity player) {
    toggleAttribute(player, ItemRegistry.CHARM_LUCK.get(), Attributes.LUCK, ID_LUCK, 0.1F, 100);
  }

  public static void charmAttackSpeed(PlayerEntity player) {
    toggleAttribute(player, ItemRegistry.CHARM_ATTACKSPEED.get(), Attributes.ATTACK_SPEED, ID_ATTACKSPEED, 0.5F, 0);
  }

  public static void charmExpSpeed(PlayerEntity player) {
    ItemStack charmStack = CharmUtil.getIfEnabled(player, ItemRegistry.CHARM_XPSPEED.get());
    if (!charmStack.isEmpty()) {
      player.xpCooldown = 0;
    }
  }
}
