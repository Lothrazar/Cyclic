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
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantWaterwalking extends BaseEnchant {

  public EnchantWaterwalking() {
    super("waterwalking", Rarity.VERY_RARE, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList("")));
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        (stack.getItem() instanceof ItemArmor)
            && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    return yes;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) event.getEntity();
      ItemStack armor = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);
      int level = 0;
      if (armor.isEmpty() == false && EnchantmentHelper.getEnchantments(armor) != null
          && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
        //todo: maybe any armor?
        level = EnchantmentHelper.getEnchantments(armor).get(this);
      }
      if (level > 0) {
        setLiquidWalk(p);
      }
    }
  }

  private void setLiquidWalk(EntityPlayer player) {
    BlockPos belowPos = player.getPosition().down();
    if (player.world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && player.world.isAirBlock(player.getPosition()) && player.motionY < 0
        && !player.isSneaking()) {// let them slip down into it when sneaking
      double diff = player.posY - ((double) player.getPosition().getY());
      if (diff < 0.1) {
        player.motionY = 0;// stop falling
        player.onGround = true; // act as if on solid ground
      }
    }
  }
}
