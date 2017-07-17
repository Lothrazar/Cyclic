package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
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
    this.setRegistryName(new ResourceLocation(Const.MODID,"hydrate_"+in.getUnlocalizedName()+out.getUnlocalizedName()));
  }
  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    ItemStack s = inv.getStackInSlot(0);
    
   // System.out.println("FIND MATCH?? FOR!" + s.getDisplayName()+" IS "+ this.inputItem.getDisplayName());
    if (OreDictionary.itemMatches(s, inputItem, false)) {
      return true;
    }
//    else if (OreDictionary.itemMatches(s, new ItemStack(Blocks.HARDENED_CLAY), false)) {
//      return true;
//    }
//    else if (s.isItemEqual(new ItemStack(Blocks.CONCRETE_POWDER, 1, EnumDyeColor.BLACK.getMetadata()))) { return true; }
    return false;
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
