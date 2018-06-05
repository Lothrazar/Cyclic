package com.lothrazar.cyclicmagic.item.slingshot;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemPebble extends BaseItem implements IHasRecipe {

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this, 4),
        " ss",
        " ss",
        "f  ",
        's', "cobblestone",
        'f', new ItemStack(Items.FLINT));
  }

}
