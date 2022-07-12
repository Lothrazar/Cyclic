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

import java.util.Map;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class BeheadingEnchant extends EnchantmentCyclic {

  // TODO: config
  public static final int PERCDROP = 20;
  public static final int PERCPERLEVEL = 25;
  public static final String ID = "beheading";
  public static BooleanValue CFG;

  public BeheadingEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    if (isEnabled()) MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  private int percentForLevel(int level) {
    return PERCDROP + (level - 1) * PERCPERLEVEL;
  }

  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (event.getSource().getEntity() instanceof Player) {
      Player attacker = (Player) event.getSource().getEntity();
      int level = getCurrentLevelTool(attacker);
      if (level <= 0) {
        return;
      }
      Level world = attacker.level;
      if (Mth.nextInt(world.random, 0, 100) > percentForLevel(level)) {
        return;
      }
      LivingEntity target = (LivingEntity) event.getEntity();
      if (target == null) {
        return;
      } //probably wont happen just extra safe
      BlockPos pos = target.blockPosition();
      if (target instanceof Player) {
        //player head
        UtilItemStack.drop(world, pos, UtilNBT.buildNamedPlayerSkull((Player) target));
        return;
      }
      //else the random number was less than 10, so it passed the 10% chance req
      String key = target.getType().getRegistryName().toString();
      ////we allow all these, which include config, to override the vanilla skulls below 
      Map<String, String> mappedBeheading = ConfigRegistry.getMappedBeheading();
      if (target.getType() == EntityType.ENDER_DRAGON) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.DRAGON_HEAD));
      }
      else if (target.getType() == EntityType.CREEPER) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.CREEPER_HEAD));
      }
      else if (target.getType() == EntityType.ZOMBIE) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.ZOMBIE_HEAD));
      }
      else if (target.getType() == EntityType.SKELETON) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER_SKELETON) {
        UtilItemStack.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER) { //Drop number of heads equal to level of enchant [1,3] 
        UtilItemStack.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL, Math.max(level, 3)));
      }
      else if (ModList.get().isLoaded(CompatConstants.TCONSTRUCT)) {
        //tconstruct: drowned_head husk_head enderman_head cave_spider_head stray_head
        String id = CompatConstants.TCONSTRUCT;
        ItemStack tFound = ItemStack.EMPTY;
        if (target.getType() == EntityType.DROWNED) {
          tFound = UtilItemStack.findItem(id + ":drowned_head");
        }
        else if (target.getType() == EntityType.HUSK) {
          tFound = UtilItemStack.findItem(id + ":husk_head");
        }
        else if (target.getType() == EntityType.ENDERMAN) {
          tFound = UtilItemStack.findItem(id + ":enderman_head");
        }
        else if (target.getType() == EntityType.SPIDER) {
          tFound = UtilItemStack.findItem(id + ":spider_head");
        }
        else if (target.getType() == EntityType.CAVE_SPIDER) {
          tFound = UtilItemStack.findItem(id + ":cave_spider_head");
        }
        else if (target.getType() == EntityType.STRAY) {
          tFound = UtilItemStack.findItem(id + ":stray_head");
        }
        else if (target.getType() == EntityType.BLAZE) {
          tFound = UtilItemStack.findItem(id + ":blaze_head");
        }
        if (!tFound.isEmpty()) {
          UtilItemStack.drop(world, pos, tFound);
          return;
        }
      }
      else if (mappedBeheading.containsKey(key)) {
        //otherwise not a real mob, try the config last
        UtilItemStack.drop(world, pos, UtilNBT.buildNamedPlayerSkull(mappedBeheading.get(key)));
      }
    }
  }
}
