package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemEmeraldHoe extends ItemHoe implements IHasRecipe {
  public ItemEmeraldHoe() {
    super(MaterialRegistry.emeraldToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this), "ee ", " s ", " s ", 'e', "gemEmerald", 's', "stickWood");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), " ee", " s ", " s ", 'e', "gemEmerald", 's', "stickWood");
  }
}
