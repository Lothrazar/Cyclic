package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.world.item.Item.Properties;

public class SoulstoneCharm extends ItemBaseToggle {

  public SoulstoneCharm(Properties properties) {
    super(properties);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  //from LivingEntity class
  public static boolean checkTotemDeathProtection(DamageSource damageSourceIn, Player player, ItemStack itemstack) {
    if (itemstack.getItem() != ItemRegistry.SOULSTONE.get()) { // != this
      return false;
    }
    if (damageSourceIn.isBypassInvul()) {
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
      player.removeAllEffects();
      player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
      player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
      player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
      player.level.broadcastEntityEvent(player, (byte) 35);
      UtilItemStack.damageItem(player, itemstack);
      return true;
    }
  }
}
