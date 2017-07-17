package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHydrate extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private ItemStack inputItem = ItemStack.EMPTY;
  private ItemStack resultItem = ItemStack.EMPTY;
  public RecipeHydrate(ItemStack in, ItemStack out) {
    this.inputItem = in;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "hydrate_" + in.getUnlocalizedName() + out.getUnlocalizedName()));
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    ItemStack s = inv.getStackInSlot(0);
    return (OreDictionary.itemMatches(s, inputItem, false));
  }
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return resultItem.copy();
  }
  @Override
  public boolean canFit(int width, int height) {
    return (width == 1 && height == 1);
  }
  @Override
  public ItemStack getRecipeOutput() {
    return resultItem.copy();
  }
}
