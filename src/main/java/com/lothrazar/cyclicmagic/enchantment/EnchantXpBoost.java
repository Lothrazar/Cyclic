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
package com.lothrazar.cyclicmagic.enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantXpBoost extends EnchantBase {

  private static final int XP_PER_LVL = 8;

  public EnchantXpBoost() {
    super("expboost", Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList(XP_PER_LVL + "")));
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (event.getSource() == null) {
      return;
    }
    if (event.getSource().getTrueSource() instanceof EntityPlayer && event.getEntity() instanceof EntityLivingBase) {
      EntityPlayer attacker = (EntityPlayer) event.getSource().getTrueSource();
      int level = getCurrentLevelTool(attacker);
      if (level <= 0) {
        return;
      }
      EntityLivingBase target = (EntityLivingBase) event.getEntity();
      World world = attacker.getEntityWorld();
      BlockPos pos = target.getPosition();
      dropExp(world, pos, XP_PER_LVL * level);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onBreakEvent(BreakEvent event) {
    World world = event.getWorld();
    EntityPlayer player = event.getPlayer();
    if (player == null) {
      return;
    }
    BlockPos pos = event.getPos();
    int level = this.getCurrentLevelTool(player);
    if (level <= 0) {
      return;
    }
    Block block = event.getState().getBlock();
    int xpDropped = block.getExpDrop(event.getState(), world, pos, 0);
    int bonus = xpDropped * XP_PER_LVL * level;
    UtilExperience.incrementExp(player, bonus);
    //    dropExp(world, pos, xpDropped * XP_PER_LVL * level);
  }

  private void dropExp(World world, BlockPos pos, int xp) {
    if (world.isRemote == false) {
      EntityXPOrb orb = new EntityXPOrb(world);
      orb.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
      orb.xpValue = xp;
      world.spawnEntity(orb);
    }
  }
}
