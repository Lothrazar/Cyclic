package com.lothrazar.cyclic.util;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

public class AttributesUtil {

  public static final UUID DEFAULT_ID = UUID.fromString("06d30aa2-eff2-4a81-b92b-a1cb95f115c6");
  public static final UUID MULT_ID = UUID.fromString("c6d30aa2-eff2-4a81-b92b-a1cb95f115cd");

  public static int add(Attribute attribute, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, integer);
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
  public static void removePlayerReach(UUID ENCHANTMENT_REACH_ID, Player playerIn) {
    AttributeInstance attr = playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get());
    attr.removeModifier(ENCHANTMENT_REACH_ID);
  }

  // ench
  public static void setPlayerReach(UUID ENCHANTMENT_REACH_ID, Player playerIn, int reachBoost) {
    removePlayerReach(ENCHANTMENT_REACH_ID, playerIn);
    AttributeInstance attr = playerIn.getAttribute(ForgeMod.REACH_DISTANCE.get());
    //vanilla is 5, so +11 it becomes 16
    AttributeModifier enchantment = new AttributeModifier(ENCHANTMENT_REACH_ID, "ReachEnchantmentCyclic", reachBoost, AttributeModifier.Operation.ADDITION);
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

  //legacy
  public static int setHearts(Collection<ServerPlayer> players, int finalHearts) {
    int modifiedHearts = finalHearts - 10;
    for (ServerPlayer playerIn : players) {
      AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
      healthAttribute.removeModifier(DEFAULT_ID);
      //just remove and replace the modifier
      AttributeModifier healthModifier = new AttributeModifier(DEFAULT_ID, "HP Bonus from Cyclic", (modifiedHearts * 2), AttributeModifier.Operation.ADDITION);
      healthAttribute.addPermanentModifier(healthModifier);
      if (playerIn.getHealth() > healthAttribute.getValue()) {
        //        System.out.println("forceheal to go down");
        playerIn.setHealth((float) healthAttribute.getValue());
      }
      //finish up
    }
    return 0;
  }
}
