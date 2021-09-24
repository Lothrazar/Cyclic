package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

public class SoulstoneCharm extends ItemBaseToggle {

  public SoulstoneCharm(Properties properties) {
    super(properties);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  //from LivingEntity class
  public static boolean checkTotemDeathProtection(DamageSource damageSourceIn, PlayerEntity player, ItemStack itemstack) {
    if (itemstack.getItem() != ItemRegistry.SOULSTONE.get()) { // != this
      return false;
    }
    if (damageSourceIn.canHarmInCreative()) {
      return false;
    }
    else {
      //       for(Hand hand : Hand.values()) {
      //          ItemStack itemstack1 = this.getHeldItem(hand);
      //          if (itemstack1.getItem() == Items.TOTEM_OF_UNDYING) {
      //             itemstack = itemstack1.copy();
      //             itemstack1.shrink(1);
      //             break;
      //          }
      //       }
      //      if (!itemstack != null) {
      //      if (player instanceof ServerPlayerEntity) {
      //        ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) player;
      //          serverplayerentity.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
      //        CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, itemstack);
      //                }
      player.setHealth(1.0F);
      player.clearActivePotions();
      player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
      player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
      player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
      player.world.setEntityState(player, (byte) 35);
      UtilItemStack.damageItem(player, itemstack);
      return true;
    }
  }
}
