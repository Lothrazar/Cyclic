package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemSandstoneAxe extends ItemAxe implements IHasRecipe {
  public ItemSandstoneAxe() {
    // protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
    super(MaterialRegistry.sandstoneToolMaterial, 6, -3.2F);
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this), "ee ", "es ", " s ", 'e', "sandstone", 's', "stickWood");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), " ee", " se", " s ", 'e', "sandstone", 's', "stickWood");
  }
}
