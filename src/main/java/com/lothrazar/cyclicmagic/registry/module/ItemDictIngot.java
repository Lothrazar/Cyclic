package com.lothrazar.cyclicmagic.registry.module;

import com.lothrazar.cyclicmagic.data.IHasOreDict;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemDictIngot extends Item implements IHasOreDict, IHasRecipe {

  @Override
  public String[] getOreDict() {
    return new String[] { "gemObsidian" };
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this, 2),
        "ofo",
        "beb",
        "ofo",
        'e', "gemEmerald",
        'b', Items.BLAZE_ROD,
        'o', "obsidian",
        'f', Blocks.CHORUS_FLOWER);
  }
}
