/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.enchant;

import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantReach extends BaseEnchant {

  private static final String NBT_REACH_ON = "reachon";
  private static final int REACH_VANILLA = 5;
  private static final int REACH_BOOST = 16;
  private static final Rarity RARITY = Rarity.VERY_RARE;

  public EnchantReach() {
    super("reach", RARITY, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList(REACH_BOOST + "")));
  }

  @Override
  public void register() {
    EnchantRegistry.register(this);
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("EnchantReach", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        stack.getItem() == Items.ELYTRA ||
        (stack.getItem() instanceof ItemArmor)
            && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
    return yes;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }

  private void turnReachOff(EntityPlayer player) {
    player.getEntityData().setBoolean(NBT_REACH_ON, false);
    ModCyclic.proxy.setPlayerReach(player, REACH_VANILLA);
  }

  private void turnReachOn(EntityPlayer player) {
    player.getEntityData().setBoolean(NBT_REACH_ON, true);//.setInteger(NBT_REACH_ON, 1);
    ModCyclic.proxy.setPlayerReach(player, REACH_BOOST);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    //check if NOT holding this harm
    if (event.getEntityLiving() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    //Ticking
    ItemStack armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
    int level = 0;
    if (armor.isEmpty() == false && EnchantmentHelper.getEnchantments(armor) != null
        && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
      //todo: maybe any armor?
      level = EnchantmentHelper.getEnchantments(armor).get(this);
    }
    if (level > 0) {
      turnReachOn(player);
    }
    else {
      //was it on before, do we need to do an off hit
      if (player.getEntityData().hasKey(NBT_REACH_ON) && player.getEntityData().getBoolean(NBT_REACH_ON)) {
        turnReachOff(player);
      }
    }
  }
}
