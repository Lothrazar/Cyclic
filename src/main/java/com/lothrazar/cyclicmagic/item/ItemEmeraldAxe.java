package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldAxe extends ItemAxe implements IHasRecipe {
  public static final String name = "emerald_axe";
  /*
   * FOR DAMAGE AND SPEED: looks like diamond uses the final param private
   * static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F,
   * 6.0F}; private static final float[] ATTACK_SPEEDS = new float[] { -3.2F,
   * -3.2F, -3.1F, -3.0F, -3.0F};
   */
  public ItemEmeraldAxe() {
    // protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
    super(ItemRegistry.TOOL_MATERIAL_EMERALD, 6, -3);
  }
  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    if (net.minecraftforge.oredict.OreDictionary.itemMatches(new ItemStack(ItemRegistry.REPAIR_EMERALD), repair, false)) { return true; }
    return super.getIsRepairable(toRepair, repair);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), "ee ", "es ", " s ", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD), 's', new ItemStack(Items.STICK));
    GameRegistry.addShapedRecipe(new ItemStack(this), " ee", " se", " s ", 'e', new ItemStack(ItemRegistry.REPAIR_EMERALD), 's', new ItemStack(Items.STICK));
  }
}
