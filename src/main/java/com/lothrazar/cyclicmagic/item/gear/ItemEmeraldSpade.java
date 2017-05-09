package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldSpade extends ItemSpade implements IHasRecipe {
  public ItemEmeraldSpade() {
    super(MaterialRegistry.emeraldToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " s ", " s ", 'e', new ItemStack(Items.EMERALD), 's', new ItemStack(Items.STICK));
  }
}
