package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSandstoneAxe extends ItemAxe implements IHasRecipe {
  public static final String name = "sandstone_axe";
  public ItemSandstoneAxe() {
    // protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
    super(MaterialRegistry.sandstoneToolMaterial, 6, -3.2F);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", "es ", " s ", 'e', new ItemStack(Blocks.SANDSTONE), 's', new ItemStack(Items.STICK));
    GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " se", " s ", 'e', new ItemStack(Blocks.SANDSTONE), 's', new ItemStack(Items.STICK));
  }
}
