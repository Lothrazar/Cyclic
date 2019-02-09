package com.lothrazar.cyclicmagic.registry.module;

import com.lothrazar.cyclicmagic.data.IHasOreDict;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemDictIngot extends Item implements IHasOreDict, IHasRecipe {

  @Override
  public String[] getOreDict() {
    return new String[] { "ingotCrystal" };
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 2),
        "sds",
        "ggg",
        "ses",
        'd', "diamond",
        'e', "emerald",
        'g', "ingotGold",
        's', Blocks.CHORUS_FLOWER);
  }
}
