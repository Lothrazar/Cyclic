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
package com.lothrazar.cyclicmagic.villager;

import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.module.BaseModule;
import com.lothrazar.cyclicmagic.registry.VillagerProfRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillagerCreateModule extends BaseModule implements IHasConfig {

  private boolean sageEnabled;
  private boolean druidEnabled;

  private void addVillager(String name, EntityVillager.ITradeList[][] trades) {
    VillagerProfession prof = new VillagerProfession(Const.MODRES + name,
        Const.MODRES + "textures/entity/villager/" + name + ".png",
        "minecraft:textures/entity/zombie_villager/zombie_villager.png");
    VillagerProfRegistry.register(prof);
    VillagerCareer villager = new VillagerCareer(prof, name);
    for (int i = 0; i < trades.length; i++) {
      villager.addTrade(i + 1, trades[i]);
    }
  }

  @Override
  public void onPreInit() {
    //TO TEST: /summon Villager ~ ~ ~ {Profession:5,Career:0}
    if (sageEnabled) {
      addVillager(VillagerSage.NAME, VillagerSage.buildTrades());
    }
    if (druidEnabled) {
      addVillager(VillagerDruid.NAME, VillagerDruid.buildTrades());
    }
  }

  @Override
  public void syncConfig(Configuration c) {
    String category = Const.ConfigCategory.villagers;
    c.addCustomCategoryComment(category, "Two new villagers with more trades");
    sageEnabled = c.getBoolean("SageVillagers", category, true, "Adds new villager type Sage.  Spawns naturally and from mob eggs. ");
    druidEnabled = c.getBoolean("DruidVillagers", category, true, "Adds new villager type Druid.  Spawns naturally and from mob eggs. ");
  }
}
