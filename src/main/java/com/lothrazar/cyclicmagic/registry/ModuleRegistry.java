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
import com.lothrazar.cyclicmagic.module.BlockModule;
import com.lothrazar.cyclicmagic.module.CommandModule;
import com.lothrazar.cyclicmagic.module.DispenserBehaviorModule;
import com.lothrazar.cyclicmagic.module.EnchantModule;
import com.lothrazar.cyclicmagic.module.EntityMinecartModule;
import com.lothrazar.cyclicmagic.module.EnvironmentTweaksModule;
import com.lothrazar.cyclicmagic.module.FluidsModule;
import com.lothrazar.cyclicmagic.module.FragileTorchesModule;
import com.lothrazar.cyclicmagic.module.FuelAdditionModule;
import com.lothrazar.cyclicmagic.module.GearModule;
import com.lothrazar.cyclicmagic.module.GuiTerrariaButtonsModule;
import com.lothrazar.cyclicmagic.module.ICyclicModule;
import com.lothrazar.cyclicmagic.module.ItemModule;
import com.lothrazar.cyclicmagic.module.ItemPotionModule;
import com.lothrazar.cyclicmagic.module.KeyInventoryShiftModule;
import com.lothrazar.cyclicmagic.module.LootTableModule;
import com.lothrazar.cyclicmagic.module.MobDropChangesModule;
import com.lothrazar.cyclicmagic.module.MountedTweaksModule;
import com.lothrazar.cyclicmagic.module.PlantsModule;
import com.lothrazar.cyclicmagic.module.PlayerAbilitiesModule;
import com.lothrazar.cyclicmagic.module.RecipeChangerModule;
import com.lothrazar.cyclicmagic.module.StackSizeModule;
import com.lothrazar.cyclicmagic.module.TextInfoModule;
import com.lothrazar.cyclicmagic.module.VillagerCreateModule;
import com.lothrazar.cyclicmagic.module.WorldModule;

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
    register(new BlockModule());
    register(new CommandModule());
    register(new ItemModule());
    register(new ItemPotionModule());
    register(new DispenserBehaviorModule());
    register(new GearModule());
    register(new EnchantModule());
    register(new PlayerAbilitiesModule());
    register(new TextInfoModule());
    register(new FragileTorchesModule());
    register(new FuelAdditionModule());
    register(new GuiTerrariaButtonsModule());
    register(new KeyInventoryShiftModule());
    register(new LootTableModule());
    register(new PlantsModule());
    register(new MobDropChangesModule());
    register(new MountedTweaksModule());
    register(new RecipeChangerModule());
    register(new EnvironmentTweaksModule());
    register(new StackSizeModule());
    register(new VillagerCreateModule());
    register(new WorldModule());
    register(new EntityMinecartModule());
    register(new FluidsModule());
  }
}
