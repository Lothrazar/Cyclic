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
package com.lothrazar.cyclic.enchant;

import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclic.util.HarvestUtil;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowthEnchant extends EnchantmentCyclic {

  //TODO config
  public static final double ODDS_ROTATE = 0.04;
  public static final String ID = "growth";
  public static BooleanValue CFG;

  public GrowthEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return stack.getItem() instanceof HoeItem;
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return canApplyAtEnchantingTable(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (!isEnabled()) {
      return;
    }
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof Player) {
      Player p = (Player) entity;
      if (p.isSpectator() || !p.isAlive()) {
        return;
      }
    }
    //Ticking
    int level = getCurrentLevelTool(entity.getItemInHand(InteractionHand.MAIN_HAND));
    if (level > 0 && !entity.level.isClientSide) {
      if (entity.level.random.nextDouble() > ODDS_ROTATE / level) {
        return; //slow the dice down
      }
      final int growthLimit = level * 2 + (entity.level.isRaining() ? 4 : 1); //faster when raining too 
      int grown = 0;
      List<BlockPos> shape = UtilShape.squareHorizontalFull(entity.blockPosition().below(), level + 2);
      shape = UtilShape.repeatShapeByHeight(shape, 2);
      Collections.shuffle(shape);
      for (int i = 0; i < shape.size(); i++) {
        if (grown >= growthLimit) {
          break;
        }
        //do one
        BlockPos pos = shape.get(i);
        BlockState target = entity.level.getBlockState(pos);
        IntegerProperty propAge = HarvestUtil.getAgeProp(target);
        if (propAge == null) {
          continue;
        }
        int maxAge = Collections.max(propAge.getPossibleValues());
        Integer currentAge = target.getValue(propAge);
        if (currentAge < maxAge) {
          if (entity.level.setBlockAndUpdate(pos, target.setValue(propAge, currentAge + 1))) {
            grown++;
          }
        }
      }
      if (grown > 0) {
        UtilItemStack.damageItem(entity, entity.getItemInHand(InteractionHand.MAIN_HAND));
      }
    }
  }
}
