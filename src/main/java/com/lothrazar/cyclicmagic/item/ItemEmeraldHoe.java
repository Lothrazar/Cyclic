package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.module.EmeraldArmorModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldHoe extends ItemHoe implements IHasRecipe {
  public static final String name = "emerald_hoe";
  public ItemEmeraldHoe() {
    super(EmeraldArmorModule.TOOL_MATERIAL);
  }
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(EmeraldArmorModule.REPAIR), repair, false)) { return true; }
    return super.getIsRepairable(toRepair, repair);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", " s ", " s ", 'e', new ItemStack(EmeraldArmorModule.REPAIR), 's', new ItemStack(Items.STICK));
    GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " s ", " s ", 'e', new ItemStack(EmeraldArmorModule.REPAIR), 's', new ItemStack(Items.STICK));
  }
}
