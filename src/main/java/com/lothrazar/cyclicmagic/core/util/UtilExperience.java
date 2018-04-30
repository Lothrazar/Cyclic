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
package com.lothrazar.cyclicmagic.core.util;

import net.minecraft.entity.player.EntityPlayer;

public class UtilExperience {

  public static double getExpTotal(EntityPlayer player) {
    validateExpPositive(player);
    int level = player.experienceLevel;
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    double totalExp = getXpForLevel(level);
    double progress = Math.round(player.xpBarCap() * player.experience);
    totalExp += (int) progress;
    return totalExp;
  }

  public static boolean drainExp(EntityPlayer player, float f) {
    double totalExp = getExpTotal(player);
    if (totalExp - f < 0) {
      return false;
    }
    int result = (int) (totalExp - f);
    setXp(player, result);
    return true;
  }

  public static int getXpToGainLevel(int level) {
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    // so if our current level is 5, we pass in5 here and find out
    // how much exp to get from 5 to 6
    int nextLevelExp = 0;
    if (level <= 15)
      nextLevelExp = 2 * level + 7;
    else if (level <= 30)
      nextLevelExp = 5 * level - 38;
    else
      // level >= 31
      nextLevelExp = 9 * level - 158;
    return nextLevelExp;
  }

  public static int getXpForLevel(int level) {
    // numeric reference:
    // http://minecraft.gamepedia.com/Experience#Leveling_up
    int totalExp = 0;
    if (level <= 15)
      totalExp = level * level + 6 * level;
    else if (level <= 30)
      totalExp = (int) (2.5 * level * level - 40.5 * level + 360);
    else
      // level >= 31
      totalExp = (int) (4.5 * level * level - 162.5 * level + 2220);
    return totalExp;
  }

  public static int getLevelForXp(int xp) {
    int lev = 0;
    while (getXpForLevel(lev) < xp) {
      lev++;
    }
    return lev - 1;
  }

  public static void incrementExp(EntityPlayer player, int xp) {
    setXp(player, (int) getExpTotal(player) + xp);
  }

  public static void setXp(EntityPlayer player, int xp) {
    if (xp < 0) {
      xp = 0;
    }
    player.experienceTotal = xp;
    player.experienceLevel = getLevelForXp(xp);
    int next = getXpForLevel(player.experienceLevel);
    if (player.experienceTotal == 0 || player.experienceLevel == 0) {
      player.experience = 0;
    }
    else {
      player.experience = (float) (player.experienceTotal - next) / (float) player.xpBarCap();
    }
    //previous versions had bugs and set to a bad state
    //so backfill and sanity check all values
    validateExpPositive(player);
  }

  private static void validateExpPositive(EntityPlayer player) {
    if (player.experience < 0) {
      player.experience = 0;
    }
    if (player.experienceTotal < 0) {
      player.experienceTotal = 0;
    }
    if (player.experienceLevel < 0) {
      player.experienceLevel = 0;
    }
  }
}
