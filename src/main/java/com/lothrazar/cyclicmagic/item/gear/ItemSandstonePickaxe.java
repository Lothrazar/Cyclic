package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemSandstonePickaxe extends ItemPickaxe implements IHasRecipe {
  public ItemSandstonePickaxe() {
    super(MaterialRegistry.sandstoneToolMaterial);
  }
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(Blocks.SANDSTONE), repair, false)) { return true; }
    return super.getIsRepairable(toRepair, repair);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), "eee", " s ", " s ",
        'e', "sandstone",
        's', "stickWood");
  }
}
