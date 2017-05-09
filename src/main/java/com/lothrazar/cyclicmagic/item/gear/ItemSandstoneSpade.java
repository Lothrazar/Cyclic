package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSandstoneSpade extends ItemSpade implements IHasRecipe {
 
  public ItemSandstoneSpade() {
    super(MaterialRegistry.sandstoneToolMaterial);
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " s ", " s ", 'e', new ItemStack(Blocks.SANDSTONE), 's', new ItemStack(Items.STICK));
  }
}
