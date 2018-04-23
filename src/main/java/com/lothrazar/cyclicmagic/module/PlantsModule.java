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

import com.lothrazar.cyclicmagic.block.bean.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.block.bean.ItemMagicBean;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.core.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.core.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class PlantsModule extends BaseModule implements IHasConfig {

  private boolean enableBeans;

  @Override
  public void onPreInit() {
    if (enableBeans) {
      BlockCropMagicBean sprout = new BlockCropMagicBean();
      BlockRegistry.registerBlock(sprout, "sprout", null);
      ItemMagicBean sprout_seed = new ItemMagicBean(sprout, Blocks.FARMLAND);
      ItemRegistry.register(sprout_seed, "sprout_seed");
      LootTableRegistry.registerLoot(sprout_seed);
      sprout.setSeed(sprout_seed);
      //      AchievementRegistry.registerItemAchievement(sprout_seed);
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    enableBeans = config.getBoolean("MagicBean", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
