package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldAxe extends ItemAxe implements IHasRecipe {
  public ItemEmeraldAxe() {
    // protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
    super(MaterialRegistry.emeraldToolMaterial, 8, -3);
  }
  @Override
  public IRecipe addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", "es ", " s ", 'e', new ItemStack(Items.EMERALD), 's', new ItemStack(Items.STICK));
    return GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " se", " s ", 'e', new ItemStack(Items.EMERALD), 's', new ItemStack(Items.STICK));
  }
}
