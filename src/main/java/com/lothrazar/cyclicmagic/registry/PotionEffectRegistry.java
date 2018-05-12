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
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.potion.PotionBase;
import com.lothrazar.cyclicmagic.potion.PotionBounce;
import com.lothrazar.cyclicmagic.potion.PotionDropItems;
import com.lothrazar.cyclicmagic.potion.PotionEnder;
import com.lothrazar.cyclicmagic.potion.PotionFrostWalker;
import com.lothrazar.cyclicmagic.potion.PotionMagnet;
import com.lothrazar.cyclicmagic.potion.PotionSlowfall;
import com.lothrazar.cyclicmagic.potion.PotionSnow;
import com.lothrazar.cyclicmagic.potion.PotionSwimSpeed;
import com.lothrazar.cyclicmagic.potion.PotionWaterwalk;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEffectRegistry {

  public static ArrayList<Potion> potions = new ArrayList<Potion>();

  public static enum PotionSize {
    NORMAL, POWERED, LONG//, SPLASH, LINGER // todo: these last two
  }

  public static final PotionBase SLOWFALL = new PotionSlowfall();
  public static final PotionBase MAGNET = new PotionMagnet();
  public static final PotionBase ENDER = new PotionEnder();
  public static final PotionBase WATERWALK = new PotionWaterwalk();
  public static final PotionBase SNOW = new PotionSnow();
  public static final PotionBase SWIMSPEED = new PotionSwimSpeed();
  public static final PotionBase BOUNCE = new PotionBounce();
  public static final PotionBase FROSTW = new PotionFrostWalker();
  public static final PotionBase DROPS = new PotionDropItems();
  public static ArrayList<PotionBase> potionEffects = new ArrayList<PotionBase>();

  private static void register() {
    //  PotionType t http://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/2842885-solved-how-can-i-add-my-own-potion-with-my-own
    /* Assuming you've created and registered the Potion instances, you then need to create and register a PotionType for each brewable potion. For most vanilla Potions there are one to three
     * PotionTypes: regular (e.g. Regeneration, 0:45) , long (e.g. Regeneration, 1:30) and strong (e.g. Regeneration II, 0:22).
     * 
     * Both Potion and PotionType are implementations of IForgeRegistryEntry, so they should be registered during the appropriate registry events.
     * 
     * Once you've created and registered the Potions and PotionTypes, use PotionHelper.addMix to add the brewing recipes (e.g. Awkward to Regular X, Regular X to Long X and Regular X to Strong X).
     * For more advanced brewing recipes, you can use Forge's BrewingRecipeRegistry. */
    registerPotionEffect(MAGNET);
    registerPotionEffect(ENDER);
    registerPotionEffect(WATERWALK);
    registerPotionEffect(SLOWFALL);
    registerPotionEffect(SNOW);
    registerPotionEffect(SWIMSPEED);
    registerPotionEffect(BOUNCE);
    registerPotionEffect(FROSTW);
    registerPotionEffect(DROPS);
  }

  private static void registerPotionEffect(PotionBase effect) {
    effect.setIcon(effect.getIcon());
    effect.setRegistryName(new ResourceLocation(Const.MODID, effect.getName()));
    potions.add(effect);
    potionEffects.add(effect);
    ModCyclic.instance.events.register(effect);
  }

  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Potion> event) {
    PotionEffectRegistry.register();
    for (Potion b : potions) {
      event.getRegistry().register(b);
    }
  }

  public static String getStrForLevel(int lvl) {
    return UtilChat.lang("enchantment.level." + lvl);
  }
}
