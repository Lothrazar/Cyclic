package com.lothrazar.cyclicmagic.item.slingshot;

import com.lothrazar.cyclicmagic.core.IHasOreDict;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemPebble extends BaseItem implements IHasRecipe, IHasOreDict {

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this, 32),
        "cfc",
        "f f",
        "cfc",
        'c', "cobblestone",
        'f', new ItemStack(Blocks.DIRT, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
  }

  @Override
  public String getOreDict() {
    return "rock";
  }
}
