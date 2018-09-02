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
package com.lothrazar.cyclicmagic.potion;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionTypeRegistry {

  public static List<PotionTypeCyclic> potions = new ArrayList<PotionTypeCyclic>();

  public static PotionTypeCyclic addPotionType(PotionEffect eff, String name, ItemStack item) {
    PotionTypeCyclic pot = new PotionTypeCyclic(name, new PotionEffect[] { eff }, item);
    potions.add(pot);
    return pot;
  }

  public static PotionTypeCyclic addPotionType(PotionEffect eff, String name, Item item) {
    PotionTypeCyclic pot = addPotionType(eff, name, new ItemStack(item));
    potions.add(pot);
    return pot;
  }

  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<PotionType> event) {
    //    PotionTypeRegistry.register();
    for (PotionTypeCyclic pt : potions) {
      event.getRegistry().register(pt);
      pt.addMix();
    }
    //    //    PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE,PotionTypes.THICK);
    //    PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE, potionTypeSlowfall);
    //    RecipeRegistry.addShapedOreRecipe(
    //
    //        BrewingRecipeRegistry.addRecipe(
    //            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), 
    //            new ItemStack(Items.APPLE), 
    //            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypeRegistry.potionTypeSlowfall))
    //        
    //        );
  }
  //  @SubscribeEvent
  //  public static void onDrink(LivingEntityUseItemEvent.Finish event) {
  //    List<PotionEffect> effects = PotionUtils.getEffectsFromStack(event.getItem());
  //    Item item = event.getItem().getItem();
  //    //cant double up because vanilla addpotioneffect just merges times, does not add them
  //    //WHAT DOES THIS FIX? Well, when i create a custom PotionType, it works with vanilla potions but not mine
  //    //so. lol. yep. brute force it is then eh? yup.
  //    if (item instanceof ItemPotion) {
  //      for (PotionEffect effect : effects) {
  //        ResourceLocation potionReg = effect.getPotion().getRegistryName();
  //        if (potionReg != null && potionReg.getResourceDomain().equals(Const.MODID)) {
  //          event.getEntityLiving().addPotionEffect(new PotionEffect(effect));
  //        }
  //      }
  //    }
  //  }
}
