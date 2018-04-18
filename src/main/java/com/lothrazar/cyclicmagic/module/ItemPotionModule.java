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
package com.lothrazar.cyclicmagic.module;

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPotionModule extends BaseEventModule implements IHasConfig {

  public boolean cancelPotionInventoryShift;
  public static boolean enableEnder;
  public static boolean enableMagnet;
  public static boolean enableWaterwalk;
  public static boolean enableSlowfall;
  public static boolean enableSnow;
  public static boolean enableHaste;
  public static boolean enableResist;
  public static boolean enableLuck;
  public static boolean enableLevit;
  public static boolean enableHBoost;
  public static boolean enableSwimspeed;
  public static boolean enableBounce;
  public static boolean enableWither;
  public static boolean enableBlindness;
  public static boolean enableSaturation;
  public static boolean enableFrostw;

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPotionShiftEvent(GuiScreenEvent.PotionShiftEvent event) {
    event.setCanceled(cancelPotionInventoryShift);
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.inventory;
    cancelPotionInventoryShift = config.getBoolean("Potion Inventory Shift", category, true,
        "When true, this blocks the potions moving the inventory over");
    category = Const.ConfigCategory.content;
    enableBounce = config.getBoolean("PotionBounce", category, true, Const.ConfigCategory.contentDefaultText);
    enableSwimspeed = config.getBoolean("PotionSwimSpeed", category, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("PotionMagnet", category, true, Const.ConfigCategory.contentDefaultText);
    enableWaterwalk = config.getBoolean("PotionWaterwalk", category, true, Const.ConfigCategory.contentDefaultText);
    enableSlowfall = config.getBoolean("PotionSlowfall", category, true, Const.ConfigCategory.contentDefaultText);
    enableSnow = config.getBoolean("PotionSnow", category, true, Const.ConfigCategory.contentDefaultText);
    enableEnder = config.getBoolean("PotionEnder", category, true, Const.ConfigCategory.contentDefaultText);
    enableHaste = config.getBoolean("Potionhaste", category, true, Const.ConfigCategory.contentDefaultText);
    enableResist = config.getBoolean("PotionResistance", category, true, Const.ConfigCategory.contentDefaultText);
    enableLuck = config.getBoolean("PotionLuck", category, true, Const.ConfigCategory.contentDefaultText);
    enableLevit = config.getBoolean("PotionLevitation", category, true, Const.ConfigCategory.contentDefaultText);
    enableHBoost = config.getBoolean("PotionHealthBoost", category, true, Const.ConfigCategory.contentDefaultText);
    enableWither = config.getBoolean("PotionWither", category, true, Const.ConfigCategory.contentDefaultText);
    enableBlindness = config.getBoolean("PotionBlindness", category, true, Const.ConfigCategory.contentDefaultText);
    enableSaturation = config.getBoolean("PotionSaturation", category, true, Const.ConfigCategory.contentDefaultText);
    enableFrostw = config.getBoolean("PotionFrostWalker", category, true, Const.ConfigCategory.contentDefaultText);
  }
}
