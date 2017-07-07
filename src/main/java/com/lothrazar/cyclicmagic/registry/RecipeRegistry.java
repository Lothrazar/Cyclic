package com.lothrazar.cyclicmagic.registry;
import java.util.List;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeRegistry {
  public static final boolean WRITE_JSON = true;
  /**
   * wrapper for Forge addShapeless recipe, except the difference is this
   * returns it after registering it
   * 
   * @param output
   * @param recipeComponents
   * @return
   */
  public static IRecipe addShapelessRecipe(ItemStack output, Object... recipeComponents) {
  if(WRITE_JSON) { RecipeFileWriter.addShapelessRecipe(output, recipeComponents);}
    List<ItemStack> list = Lists.<ItemStack> newArrayList();
    for (Object object : recipeComponents) {
      if (object instanceof String) { return addShapelessOreRecipe(output, recipeComponents); }
      if (object instanceof ItemStack) {
        list.add(((ItemStack) object).copy());
      }
      else if (object instanceof Item) {
        list.add(new ItemStack((Item) object));
      }
      else {
        if (!(object instanceof Block)) { throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!"); }
        list.add(new ItemStack((Block) object));
      }
    }
    IRecipe recipe = new ShapelessRecipes(output, list);
    GameRegistry.addRecipe(recipe);
    return recipe;
  }
  /**
   * thin wrapper for addShapedRecipe
   * 
   * @param output
   * @param params
   * @return
   */
  public static IRecipe addShapedRecipe(@Nonnull ItemStack output, Object... recipeComponents) {
    if(WRITE_JSON){ RecipeFileWriter.addShapedRecipe(output, recipeComponents);}
    for (Object object : recipeComponents) {
      if (object instanceof String) { return addShapedOreRecipe(output, recipeComponents); }
    }
    return GameRegistry.addShapedRecipe(output, recipeComponents);
  }
  private static IRecipe addShapedOreRecipe(ItemStack stack, Object... recipeComponents) {
    IRecipe r = new ShapedOreRecipe(stack, recipeComponents);
    GameRegistry.addRecipe(r);
    return r;
  }
  private static IRecipe addShapelessOreRecipe(ItemStack output, Object... recipeComponents) {
    IRecipe r = new ShapelessOreRecipe(output, recipeComponents);
    GameRegistry.addRecipe(r);
    return r;
  }
}
