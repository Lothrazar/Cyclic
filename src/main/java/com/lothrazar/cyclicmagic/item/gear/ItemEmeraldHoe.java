package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldHoe extends ItemHoe implements IHasRecipe {
  
  public ItemEmeraldHoe() {
    super(MaterialRegistry.emeraldToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", " s ", " s ", 'e', new ItemStack(Items.EMERALD), 's', new ItemStack(Items.STICK));
    return GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " s ", " s ", 'e', new ItemStack(Items.EMERALD), 's', new ItemStack(Items.STICK));
  }
}
