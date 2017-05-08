package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldArmor extends ItemArmor implements IHasRecipe {
  public ItemEmeraldArmor(EntityEquipmentSlot armorType) {
    super(MaterialRegistry.emeraldArmorMaterial, 0, armorType);
  }
  @Override
  public IRecipe addRecipe() {
    switch (this.armorType) {
      case CHEST:
        return GameRegistry.addShapedRecipe(new ItemStack(this), "e e", "eee", "eee", 'e', new ItemStack(Items.EMERALD));
      case FEET:
        GameRegistry.addShapedRecipe(new ItemStack(this), "e e", "e e", "   ", 'e', new ItemStack(Items.EMERALD));
        return GameRegistry.addShapedRecipe(new ItemStack(this), "   ", "e e", "e e", 'e', new ItemStack(Items.EMERALD));
      case HEAD:
        GameRegistry.addShapedRecipe(new ItemStack(this), "eee", "e e", "   ", 'e', new ItemStack(Items.EMERALD));
        return GameRegistry.addShapedRecipe(new ItemStack(this), "   ", "eee", "e e", 'e', new ItemStack(Items.EMERALD));
      case LEGS:
        return GameRegistry.addShapedRecipe(new ItemStack(this), "eee", "e e", "e e", 'e', new ItemStack(Items.EMERALD));
      case MAINHAND:
      break;
      case OFFHAND:
      break;
      default:
      break;
    }
    return null;
  }
}
