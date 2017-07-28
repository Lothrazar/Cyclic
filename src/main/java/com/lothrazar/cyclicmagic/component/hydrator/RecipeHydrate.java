package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHydrate extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static int id = 0;
  private ItemStack[] input = new ItemStack[4];
  private ItemStack resultItem = ItemStack.EMPTY;
  public RecipeHydrate(ItemStack in, ItemStack out) {
    this(new ItemStack[] { in, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY }, out);
  }
  public RecipeHydrate(ItemStack[] in, ItemStack out) {
    if (in.length != 4) { throw new IllegalArgumentException("Input array must be length 4"); }
    this.input = in;
    this.resultItem = out;
    this.setRegistryName(new ResourceLocation(Const.MODID, "hydrate_" + id + out.getUnlocalizedName()));
    id++;
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    ItemStack s0 = inv.getStackInSlot(0);
    ItemStack s1 = inv.getStackInSlot(1);
    ItemStack s2 = inv.getStackInSlot(2);
    ItemStack s3 = inv.getStackInSlot(3);
    //hacky lame way but easier to read and debug with all these lines
    return OreDictionary.itemMatches(s0, input[0], false) &&
        OreDictionary.itemMatches(s1, input[1], false) &&
        OreDictionary.itemMatches(s2, input[2], false) &&
        OreDictionary.itemMatches(s3, input[3], false);
  }
  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    return resultItem.copy();
  }
  @Override
  public boolean canFit(int width, int height) {
    return (width <=2 && height <=2);
  }
  @Override
  public ItemStack getRecipeOutput() {
    return resultItem.copy();
  }
  public ItemStack[] getRecipeInput() {
    return input.clone();
  }
}
