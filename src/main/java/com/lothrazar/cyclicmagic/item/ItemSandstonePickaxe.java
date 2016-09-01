package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.module.SandstoneToolsModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSandstonePickaxe extends ItemPickaxe implements IHasRecipe {
  public static final String name = "sandstone_pickaxe";
  public ItemSandstonePickaxe() {
    super(SandstoneToolsModule.TOOL_MATERIAL);
  }
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(SandstoneToolsModule.REPAIR), repair, false)) { return true; }
    return super.getIsRepairable(toRepair, repair);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "eee", " s ", " s ", 'e', new ItemStack(SandstoneToolsModule.REPAIR), 's', new ItemStack(Items.STICK));
  }
}
