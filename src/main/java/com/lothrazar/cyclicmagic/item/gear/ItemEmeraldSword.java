package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;

public class ItemEmeraldSword extends ItemSword implements IHasRecipe {
  public ItemEmeraldSword() {
    super(MaterialRegistry.emeraldToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), " e ", " e ", " s ", 'e', "gemEmerald", 's', "stickWood");
  }
}
