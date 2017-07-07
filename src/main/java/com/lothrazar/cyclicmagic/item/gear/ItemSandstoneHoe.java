package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemSandstoneHoe extends ItemHoe implements IHasRecipe {
  public ItemSandstoneHoe() {
    super(MaterialRegistry.sandstoneToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this), "ee ", " s ", " s ", 'e', "sandstone", 's', "stickWood");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), " ee", " s ", " s ", 'e', "sandstone", 's', "stickWood");
  }
}
