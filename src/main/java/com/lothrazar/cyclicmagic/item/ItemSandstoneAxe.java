package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.module.SandstoneToolsModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSandstoneAxe extends ItemAxe implements IHasRecipe {
  public static final String name = "sandstone_axe";
  public ItemSandstoneAxe() {
    // protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
    super(SandstoneToolsModule.TOOL_MATERIAL, 6, -3.2F);
  }
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(SandstoneToolsModule.REPAIR), repair, false)) { return true; }
    return super.getIsRepairable(toRepair, repair);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", "es ", " s ", 'e', new ItemStack(SandstoneToolsModule.REPAIR), 's', new ItemStack(Items.STICK));
    GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " se", " s ", 'e', new ItemStack(SandstoneToolsModule.REPAIR), 's', new ItemStack(Items.STICK));
  }
}
