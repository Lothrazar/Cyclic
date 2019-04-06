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
package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.module.CommandModule;
import com.lothrazar.cyclicmagic.module.ICyclicModule;
import com.lothrazar.cyclicmagic.module.KeyInventoryShiftModule;
import com.lothrazar.cyclicmagic.module.LootTableModule;
import com.lothrazar.cyclicmagic.module.WorldModule;
import com.lothrazar.cyclicmagic.playerupgrade.PlayerAbilitiesModule;
import com.lothrazar.cyclicmagic.tweak.EnvironmentTweaksModule;
import com.lothrazar.cyclicmagic.tweak.FragileTorchesModule;
import com.lothrazar.cyclicmagic.tweak.FuelAdditionModule;
import com.lothrazar.cyclicmagic.tweak.MobChangesModule;
import com.lothrazar.cyclicmagic.tweak.MountedTweaksModule;
import com.lothrazar.cyclicmagic.tweak.StackSizeModule;
import com.lothrazar.cyclicmagic.tweak.TextInfoModule;
import com.lothrazar.cyclicmagic.tweak.dispenser.DispenserBehaviorModule;
import com.lothrazar.cyclicmagic.tweak.recipe.RecipeChangerModule;
import com.lothrazar.cyclicmagic.villager.VillagerCreateModule;

public class ModuleRegistry {

  public static List<ICyclicModule> modules = new ArrayList<ICyclicModule>();

  public static void init() {
    modules = new ArrayList<ICyclicModule>();
  }

  public static void register(ICyclicModule m) {
    modules.add(m);
    if (m instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) m);
    }
  }

  public static void registerAll() {
    register(new CommandModule());
    register(new DispenserBehaviorModule());
    register(new PlayerAbilitiesModule());
    register(new TextInfoModule());
    register(new FragileTorchesModule());
    register(new FuelAdditionModule());
    register(new KeyInventoryShiftModule());
    register(new LootTableModule());
    register(new MobChangesModule());
    register(new MountedTweaksModule());
    register(new RecipeChangerModule());
    register(new EnvironmentTweaksModule());
    register(new StackSizeModule());
    register(new VillagerCreateModule());
    register(new WorldModule());
  }
}
