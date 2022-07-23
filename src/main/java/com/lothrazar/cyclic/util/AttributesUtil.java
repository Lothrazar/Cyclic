package com.lothrazar.cyclic.util;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

public class AttributesUtil {

  static final Random RAND = new Random();
  public static final UUID DEFAULT_ID = UUID.fromString("06d30aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID MULT_ID = UUID.fromString("c6d30aa2-eff2-4a81-b92b-a1cb95f115cd");
  public static final UUID ID_STEP_HEIGHT = UUID.fromString("66d30aa2-eaa2-4a81-b92b-a1cb95f115ca");
  static final float VANILLA = 0.6F;

  //    player.maxUpStep = 0.6F; // LivingEntity.class constructor defaults to this
  public static void disableStepHeight(Player player) {
    AttributeInstance attr = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
    attr.removeModifier(ID_STEP_HEIGHT);
  }

  public static void enableStepHeight(Player player) {
    float newVal;
    if (player.isCrouching()) {
      //make sure that, when sneaking, dont fall off!!
      newVal = 0.9F - VANILLA;
    }
    else {
      newVal = 1.0F + (1F / 16F) - VANILLA; //PATH BLOCKS etc are 1/16th downif MY feature turns this on, then do it
    }
    //    player.maxUpStep = newVal; // Deprecated
    AttributeInstance attr = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
    AttributeModifier oldModifier = attr.getModifier(AttributesUtil.ID_STEP_HEIGHT);
    double old = oldModifier == null ? 0 : oldModifier.getAmount();
    if (newVal != old) {
      AttributesUtil.setStepHeightInternal(player, newVal);
    }
  }

  private static void setStepHeightInternal(Player player, double newVal) {
    //    player.maxUpStep = 0.6F; // LivingEntity.class constructor defaults to this
    AttributeInstance attr = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
    attr.removeModifier(ID_STEP_HEIGHT);
    if (newVal != 0) {
      AttributeModifier healthModifier = new AttributeModifier(ID_STEP_HEIGHT, ModCyclic.MODID, newVal, AttributeModifier.Operation.ADDITION);
      attr.addPermanentModifier(healthModifier);
    }
  }

  public static int add(Attribute attribute, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, integer);
    }
    return 0;
  }

  public static int addRandom(Attribute attribute, Collection<ServerPlayer> players, int min, int max) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, RAND.nextInt(min, max));
    }
    return 0;
  }

  public static int multiply(Attribute attribute, Collection<ServerPlayer> players, double integer) {
    for (ServerPlayer playerIn : players) {
      multiplyAttrModifierBy(attribute, playerIn, integer);
    }
    return 0;
  }

  public static int reset(Attribute attribute, Collection<ServerPlayer> players) {
    for (ServerPlayer playerIn : players) {
      AttributeInstance attr = playerIn.getAttribute(attribute);
      attr.removeModifier(DEFAULT_ID);
      attr.removeModifier(MULT_ID);
    }
    return 0;
  }

  //ench
  public static void removePlayerReach(UUID id, Player player) {
    AttributeInstance attr = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
    attr.removeModifier(id);
  }

  // ench
  public static void setPlayerReach(UUID id, Player player, int reachBoost) {
    removePlayerReach(id, player);
    AttributeInstance attr = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
    //vanilla is 5, so +11 it becomes 16
    AttributeModifier enchantment = new AttributeModifier(id, "ReachEnchantmentCyclic", reachBoost, AttributeModifier.Operation.ADDITION);
    attr.addPermanentModifier(enchantment);
  }

  public static void updateAttrModifierBy(Attribute attr, UUID id, Player playerIn, int value) {
    AttributeInstance healthAttribute = playerIn.getAttribute(attr);
    AttributeModifier oldHealthModifier = healthAttribute.getModifier(id);
    //what is our value
    double old = oldHealthModifier == null ? 0 : oldHealthModifier.getAmount();
    double newVal = value + old;
    healthAttribute.removeModifier(id);
    AttributeModifier healthModifier = new AttributeModifier(id, "Bonus from Cyclic", newVal, AttributeModifier.Operation.ADDITION);
    healthAttribute.addPermanentModifier(healthModifier);
    if (attr == Attributes.MAX_HEALTH
        && playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }

  public static void multiplyAttrModifierBy(Attribute attr, Player playerIn, double value) {
    AttributeInstance healthAttribute = playerIn.getAttribute(attr);
    //what is our value 
    healthAttribute.removeModifier(MULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(MULT_ID, "Bonus from Cyclic", value, AttributeModifier.Operation.MULTIPLY_BASE);
    healthAttribute.addPermanentModifier(healthModifier);
    if (attr == Attributes.MAX_HEALTH
        && playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }

  public static int setHearts(Collection<ServerPlayer> players, int finalHearts) {
    for (ServerPlayer playerIn : players) {
      setHearts(finalHearts, playerIn);
    }
    return 0;
  }

  private static void setHearts(int finalHearts, ServerPlayer playerIn) {
    int modifiedHearts = finalHearts - 10;
    AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    healthAttribute.removeModifier(DEFAULT_ID);
    //just remove and replace the modifier
    AttributeModifier healthModifier = new AttributeModifier(DEFAULT_ID, "HP Bonus from Cyclic", (modifiedHearts * 2), AttributeModifier.Operation.ADDITION);
    healthAttribute.addPermanentModifier(healthModifier);
    if (playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }
}
