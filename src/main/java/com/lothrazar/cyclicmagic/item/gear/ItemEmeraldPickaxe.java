package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemEmeraldPickaxe extends ItemPickaxe implements IHasRecipe {
  public ItemEmeraldPickaxe() {
    super(MaterialRegistry.emeraldToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), "eee", " s ", " s ", 'e', new ItemStack(Items.EMERALD), 's', new ItemStack(Items.STICK));
  }
}
