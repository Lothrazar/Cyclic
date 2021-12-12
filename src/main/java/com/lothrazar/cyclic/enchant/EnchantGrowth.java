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

import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.block.harvester.TileHarvester;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantGrowth extends EnchantBase {

  private static final double ODDS_ROTATE = 0.04;

  public EnchantGrowth(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static BooleanValue CFG;
  public static final String ID = "growth";

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
  public boolean canApply(ItemStack stack) {
    return canApplyAtEnchantingTable(stack);
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) entity;
      if (p.isSpectator() || !p.isAlive()) {
        return;
      }
    }
    //Ticking
    int level = getCurrentLevelTool(entity.getHeldItem(Hand.MAIN_HAND));
    if (level > 0 && !entity.world.isRemote) {
      if (entity.world.rand.nextDouble() > ODDS_ROTATE / level) {
        return; //slow the dice down
      }
      final int growthLimit = level * 2 + (entity.world.isRaining() ? 4 : 1); //faster when raining too 
      int grown = 0;
      List<BlockPos> shape = UtilShape.squareHorizontalFull(entity.getPosition().down(), level + 2);
      shape = UtilShape.repeatShapeByHeight(shape, 2);
      Collections.shuffle(shape);
      for (BlockPos blockPos : shape) {
        if (grown >= growthLimit) {
          break;
        }
        //do one
        BlockState target = entity.world.getBlockState(blockPos);
        IntegerProperty propAge = TileHarvester.getAgeProp(target);
        if (propAge == null) {
          continue;
        }
        int maxAge = Collections.max(propAge.getAllowedValues());
        int currentAge = target.get(propAge);
        if (currentAge < maxAge) {
          if (entity.world.setBlockState(blockPos,
              target.with(propAge, currentAge + 1))) {
            grown++;
          }
        }
      }
      if (grown > 0) {
        UtilItemStack.damageItem(entity, entity.getHeldItem(Hand.MAIN_HAND));
      }
    }
  }
}
