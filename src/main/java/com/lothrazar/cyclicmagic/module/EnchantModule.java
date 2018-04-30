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
import com.lothrazar.cyclicmagic.core.EnchantBase;
import com.lothrazar.cyclicmagic.core.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.enchantment.EnchantAutoSmelt;
import com.lothrazar.cyclicmagic.enchantment.EnchantBeheading;
import com.lothrazar.cyclicmagic.enchantment.EnchantExcavation;
import com.lothrazar.cyclicmagic.enchantment.EnchantLaunch;
import com.lothrazar.cyclicmagic.enchantment.EnchantLifeLeech;
import com.lothrazar.cyclicmagic.enchantment.EnchantMagnet;
import com.lothrazar.cyclicmagic.enchantment.EnchantMultishot;
import com.lothrazar.cyclicmagic.enchantment.EnchantQuickdraw;
import com.lothrazar.cyclicmagic.enchantment.EnchantReach;
import com.lothrazar.cyclicmagic.enchantment.EnchantVenom;
import com.lothrazar.cyclicmagic.enchantment.EnchantWaterwalking;
import com.lothrazar.cyclicmagic.enchantment.EnchantXpBoost;
import net.minecraftforge.common.config.Configuration;

public class EnchantModule extends BaseModule implements IHasConfig {

  private boolean enablexpboost;
  private boolean enableLaunch;
  private boolean enableMagnet;
  private boolean enableVenom;
  private boolean enableLifeleech;
  private boolean enableautosmelt;
  private boolean enablereach;
  private boolean enablebeheading;
  private boolean enableQuickdraw;
  private boolean enablewaterwalk;
  private boolean enableExcavate;
  private boolean enableMultishot;

  @Override
  public void onPreInit() {
    super.onPreInit();
    if (enablewaterwalk) {
      EnchantWaterwalking waterwalk = new EnchantWaterwalking();
      EnchantRegistry.register(waterwalk);
    }
    if (enablereach) {
      EnchantRegistry.reach = new EnchantReach();
      EnchantRegistry.register(EnchantRegistry.reach);
    }
    if (enablexpboost) {
      EnchantXpBoost xpboost = new EnchantXpBoost();
      EnchantRegistry.register(xpboost);
    }
    if (enableautosmelt) {
      EnchantAutoSmelt autosmelt = new EnchantAutoSmelt();
      EnchantRegistry.register(autosmelt);
    }
    if (enableLaunch) {
      EnchantLaunch launch = new EnchantLaunch();
      EnchantRegistry.register(launch);
    }
    if (enableMagnet) {
      EnchantMagnet magnet = new EnchantMagnet();
      EnchantRegistry.register(magnet);
    }
    if (enableVenom) {
      EnchantVenom venom = new EnchantVenom();
      EnchantRegistry.register(venom);
    }
    if (enableLifeleech) {
      EnchantBase lifeleech = new EnchantLifeLeech();
      EnchantRegistry.register(lifeleech);
    }
    if (enablebeheading) {
      EnchantBase beheading = new EnchantBeheading();
      EnchantRegistry.register(beheading);
    }
    if (enableQuickdraw) {
      EnchantBase quickdraw = new EnchantQuickdraw();
      EnchantRegistry.register(quickdraw);
    }
    if (enableExcavate) {
      EnchantBase excavation = new EnchantExcavation();
      EnchantRegistry.register(excavation);
    }
    if (enableMultishot) {
      EnchantMultishot multishot = new EnchantMultishot();
      EnchantRegistry.register(multishot);
    }
  }

  @Override
  public void syncConfig(Configuration c) {
    enableMultishot = c.getBoolean("EnchantMultishot", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableExcavate = c.getBoolean("EnchantExcavation", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablewaterwalk = c.getBoolean("EnchantWaterwalk", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablereach = c.getBoolean("EnchantReach", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablexpboost = c.getBoolean("EnchantExpBoost", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableautosmelt = c.getBoolean("EnchantAutoSmelt", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLaunch = c.getBoolean("EnchantLaunch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = c.getBoolean("EnchantMagnet", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVenom = c.getBoolean("EnchantVenom", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLifeleech = c.getBoolean("EnchantLifeLeech", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablebeheading = c.getBoolean("EnchantBeheading", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableQuickdraw = c.getBoolean("EnchantQuickdraw", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    for (EnchantBase b : EnchantRegistry.enchants) {
      if (b instanceof IHasConfig) {
        ((IHasConfig) b).syncConfig(c);
      }
    }
  }
}
