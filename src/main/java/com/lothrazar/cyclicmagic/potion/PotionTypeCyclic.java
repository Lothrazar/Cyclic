package com.lothrazar.cyclicmagic.potion;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionTypeCyclic extends PotionType {
  ItemStack recipeStack;
  public PotionType base;
  public PotionTypeCyclic(String name, PotionEffect[] potionEffects, ItemStack ingredient) {
    super(name, potionEffects);
    this.setRegistryName(new ResourceLocation(Const.MODID, name));
    recipeStack = ingredient;
    base = PotionTypes.AWKWARD;
  }
//  public void addMix(Item item) {
//    PotionHelper.addMix(PotionTypes.AWKWARD, item, this);
//  }
  public void addMix() {
   
    PotionHelper.addMix(base, Ingredient.fromStacks(recipeStack), this);
  }
}
