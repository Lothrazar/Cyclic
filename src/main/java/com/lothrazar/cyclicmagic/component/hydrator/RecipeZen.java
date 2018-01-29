package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.ModCyclic;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.cyclicmagic.Hydrator")
@ZenRegister
public class RecipeZen {
  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void removeShapedRecipe(IItemStack output) {
    ItemStack out = toStack(output);
    for (RecipeHydrate rec : BlockHydrator.recipesShaped) {
      if (rec.getRecipeOutput().isItemEqual(out)) {
        BlockHydrator.recipesShaped.remove(rec);
        ModCyclic.logger.info("removed hydrator recipe for " + output.getDisplayName());
      }
    }
  }
  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void removeShapelessRecipe(IItemStack output) {
    ItemStack out = toStack(output);
    for (RecipeHydrate rec : BlockHydrator.recipesShapeless) {
      if (rec.getRecipeOutput().isItemEqual(out)) {
        BlockHydrator.recipesShapeless.remove(rec);
        ModCyclic.logger.info("removed hydrator recipe for " + output.getDisplayName());
      }
    }
  }
  @ZenMethod
  public static void addRecipe(IItemStack output, IItemStack[] inputs, int water) {
    BlockHydrator.addRecipe(new RecipeHydrate(toStacks(inputs), toStack(output), water));
  }
  /**
   * THANKS TO https://github.com/jaredlll08/MTLib/blob/1.12/src/main/java/com/blamejared/mtlib/helpers/InputHelper.java @ https://github.com/jaredlll08/MTLib which is MIT license
   * https://github.com/jaredlll08/MTLib/blob/1.12/LICENSE.md
   */
  @Optional.Method(modid = "crafttweaker")
  public static ItemStack toStack(IItemStack iStack) {
    if (iStack == null) {
      return ItemStack.EMPTY;
    }
    else {
      Object internal = iStack.getInternal();
      if (!(internal instanceof ItemStack)) {
        return ItemStack.EMPTY;
      }
      return (ItemStack) internal;
    }
  }
  /**
   * THANKS TO https://github.com/jaredlll08/MTLib/blob/1.12/src/main/java/com/blamejared/mtlib/helpers/InputHelper.java @ https://github.com/jaredlll08/MTLib which is MIT license
   * https://github.com/jaredlll08/MTLib/blob/1.12/LICENSE.md
   */
  @Optional.Method(modid = "crafttweaker")
  public static ItemStack[] toStacks(IItemStack[] iStack) {
    if (iStack == null) {
      return null;
    }
    else {
      ItemStack[] output = new ItemStack[iStack.length];
      for (int i = 0; i < iStack.length; i++) {
        output[i] = toStack(iStack[i]);
      }
      return output;
    }
  }
}
