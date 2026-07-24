package com.lothrazar.cyclic.item.bauble;

import java.util.UUID;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.ParticleUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public abstract class CharmBase extends ItemBaseToggle {

  private static final int FIREPROTSECONDS_DEFAULT = 10;
  private static final int FALLDISTANCESECONDS_DEFAULT = 5;
  private static final int FALLDISTANCELIMIT_DEFAULT = 5; // was 6 in 1.12.2
  private static final double SWIM_MULTIPLIER_DEFAULT = 3;
  public static IntValue FIREPROT_SECONDS;
  public static IntValue WING_FALL_SLOWFALL_SECONDS;
  public static IntValue WING_FALL_DISTANCE_LIMIT;
  public static DoubleValue FLIPPERS_SWIM_MULTIPLIER;
  public static final UUID ID_SPEED = UUID.fromString("12230aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID ID_SWIMMING = UUID.fromString("92230aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID ID_LUCK = UUID.fromString("acc30aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID ID_ATTACKSPEED = UUID.fromString("b4678aa2-eff2-4a81-b92b-a1cb95f115c6");
  boolean fireProt;
  boolean poisonProt;
  boolean witherProt;
  boolean voidProt;
  boolean wingCharm;
  boolean sailboatCharm;

  public CharmBase(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    tryVoidTick(stack, worldIn, entityIn);
    if (entityIn instanceof LivingEntity == false) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    LivingEntity living = (LivingEntity) entityIn;
    tryPoisonTick(stack, entityIn, living);
    tryWitherTick(stack, entityIn, living);
    tryWingTick(stack, entityIn, living);
    tryFireTick(stack, living);
  }

  private void tryWingTick(ItemStack stack, Entity entityIn, LivingEntity living) {
    int fallLimit = WING_FALL_DISTANCE_LIMIT == null ? FALLDISTANCELIMIT_DEFAULT : WING_FALL_DISTANCE_LIMIT.get();
    int slowfallSeconds = WING_FALL_SLOWFALL_SECONDS == null ? FALLDISTANCESECONDS_DEFAULT : WING_FALL_SLOWFALL_SECONDS.get();
    if (this.wingCharm && living.fallDistance > fallLimit && !living.hasEffect(MobEffects.SLOW_FALLING)) {
      MobEffectInstance eff = new MobEffectInstance(MobEffects.SLOW_FALLING, slowfallSeconds * Const.TICKS_PER_SEC, Const.Potions.I, false, false, false);
      living.addEffect(eff);
      ItemStackUtil.damageItem(living, stack);
      SoundUtil.playSound(living, SoundEvents.LADDER_FALL);
    }
  }

  private void tryFireTick(ItemStack stack, LivingEntity living) {
    if (this.fireProt && living.isOnFire() && !living.hasEffect(MobEffects.FIRE_RESISTANCE)) { // do nothing if you already have
      int firePotSec = FIREPROT_SECONDS == null ? FIREPROTSECONDS_DEFAULT : FIREPROT_SECONDS.get();
      MobEffectInstance eff = new MobEffectInstance(MobEffects.FIRE_RESISTANCE, firePotSec * Const.TICKS_PER_SEC, Const.Potions.I, false, false, false);
      living.addEffect(eff);
      ItemStackUtil.damageItem(living, stack);
      SoundUtil.playSound(living, SoundEvents.FIRE_EXTINGUISH);
      ParticleUtil.spawnParticle(living.level(), ParticleTypes.DRIPPING_WATER, living.blockPosition(), 9);
    }
  }

  private void tryWitherTick(ItemStack stack, Entity entityIn, LivingEntity living) {
    if (this.witherProt && living.hasEffect(MobEffects.WITHER)) {
      living.removeEffectNoUpdate(MobEffects.WITHER);
      ItemStackUtil.damageItem(living, stack);
      SoundUtil.playSound(entityIn, SoundEvents.GENERIC_DRINK);
    }
  }

  private void tryPoisonTick(ItemStack stack, Entity entityIn, LivingEntity living) {
    if (this.poisonProt && living.hasEffect(MobEffects.POISON)) {
      living.removeEffectNoUpdate(MobEffects.POISON);
      ItemStackUtil.damageItem(living, stack);
      SoundUtil.playSound(entityIn, SoundEvents.GENERIC_DRINK);
    }
  }

  private void tryVoidTick(ItemStack stack, Level worldIn, Entity entityIn) {
    int minY = worldIn.dimensionType().minY();
    int maxY = worldIn.dimensionType().logicalHeight();
    if (this.voidProt && entityIn.blockPosition().getY() < (minY - 40) && entityIn instanceof LivingEntity) {
      LivingEntity entity = (LivingEntity) entityIn;
      EntityUtil.enderTeleportEvent(entity, worldIn, new BlockPos(entityIn.blockPosition().getX(), maxY, entityIn.blockPosition().getZ()));
      ItemStackUtil.damageItem(entity, stack);
      SoundUtil.playSound(entityIn, SoundEvents.ENDERMAN_TELEPORT);
    }
  }

  private static void toggleAttribute(Player player, Item charm, Holder<Attribute> attr, UUID id, float factor, int flatIncrease, Operation op) {
    ItemStack charmStack = CharmUtil.getIfEnabled(player, charm);
    AttributeInstance attrPlayer = player.getAttribute(attr);
    AttributeModifier oldValue = attrPlayer.getModifier(Identifier.fromNamespaceAndPath("cyclic", id.toString().replace("-","_").substring(0,20)));
    if (charmStack.isEmpty()) {
      ///i am NOT holding it. OR im holding but its OFF
      //remove my modifier
      if (oldValue != null) {
        attrPlayer.removeModifier(Identifier.fromNamespaceAndPath("cyclic", id.toString().replace("-","_").substring(0,20)));
      }
    }
    else { // im   holding it AND its enabled
      if (oldValue == null) {
        /// add new
        double baseVal = attrPlayer.getBaseValue();
        AttributeModifier newValue = new AttributeModifier(Identifier.fromNamespaceAndPath("cyclic", id.toString().replace("-","_").substring(0,20)), baseVal * factor + flatIncrease, op);
        attrPlayer.addPermanentModifier(newValue);
        ItemStackUtil.damageItem(player, charmStack);
      }
      //not newly triggered so countdown tick damage  
      ItemStackUtil.damageItemRandomly(player, charmStack);
    }
  }

  static final AttributeModifier.Operation ADD = Operation.ADD_VALUE;
  static final AttributeModifier.Operation MUL = Operation.ADD_MULTIPLIED_BASE;

  static void charmSpeed(Player player) {
    toggleAttribute(player, ItemRegistry.CHARM_SPEED.get(), Attributes.MOVEMENT_SPEED, ID_SPEED, ConfigRegistry.CHARM_SPEED.get().floatValue(), 0, ADD);
  }

  static void charmLuck(Player player) {
    toggleAttribute(player, ItemRegistry.CHARM_LUCK.get(), Attributes.LUCK, ID_LUCK, 0, ConfigRegistry.CHARM_LUCK.get(), ADD);
  }

  static void charmAttackSpeed(Player player) {
    toggleAttribute(player, ItemRegistry.CHARM_ATTACK_SPEED.get(), Attributes.ATTACK_SPEED, ID_ATTACKSPEED, ConfigRegistry.CHARM_ATTACK_SPEED.get().floatValue(), 0, ADD);
  }

  static void charmSwimming(Player player) {
    float mult = FLIPPERS_SWIM_MULTIPLIER == null ? (float) SWIM_MULTIPLIER_DEFAULT : FLIPPERS_SWIM_MULTIPLIER.get().floatValue();
    toggleAttribute(player, ItemRegistry.FLIPPERS.get(), NeoForgeMod.SWIM_SPEED, ID_SPEED, mult, 0, MUL);
  }

  static void charmExpSpeed(Player player) {
    ItemStack charmStack = CharmUtil.getIfEnabled(player, ItemRegistry.CHARM_XP_SPEED.get());
    if (!charmStack.isEmpty()) {
      player.takeXpDelay = 0;
    }
  }

  public static void onEntityUpdate(Player player) {
    CharmBase.charmSwimming(player);
    CharmBase.charmSpeed(player);
    CharmBase.charmLuck(player);
    CharmBase.charmAttackSpeed(player);
    CharmBase.charmExpSpeed(player);
  }
}
