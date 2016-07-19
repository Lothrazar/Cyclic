package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.module.EmeraldArmorModule;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldPickaxe extends ItemPickaxe implements IHasRecipe {
  public static final String name = "emerald_pickaxe";
  public ItemEmeraldPickaxe() {
    super(EmeraldArmorModule.TOOL_MATERIAL_EMERALD);
  }
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(EmeraldArmorModule.REPAIR_EMERALD), repair, false)) { return true; }
    return super.getIsRepairable(toRepair, repair);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "eee", " s ", " s ", 'e', new ItemStack(EmeraldArmorModule.REPAIR_EMERALD), 's', new ItemStack(Items.STICK));
  }
}
