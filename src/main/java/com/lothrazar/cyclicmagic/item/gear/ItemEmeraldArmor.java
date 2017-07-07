package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemEmeraldArmor extends ItemArmor implements IHasRecipe {
  public ItemEmeraldArmor(EntityEquipmentSlot armorType) {
    super(MaterialRegistry.emeraldArmorMaterial, 0, armorType);
  }
  @Override
  public IRecipe addRecipe() {
    switch (this.armorType) {
      case CHEST:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this), "e e", "eee", "eee", 'e', "gemEmerald");
      case FEET:
        RecipeRegistry.addShapedRecipe(new ItemStack(this), "e e", "e e", "   ", 'e', "gemEmerald");
        return RecipeRegistry.addShapedRecipe(new ItemStack(this), "   ", "e e", "e e", 'e', "gemEmerald");
      case HEAD:
        RecipeRegistry.addShapedRecipe(new ItemStack(this), "eee", "e e", "   ", 'e', "gemEmerald");
        return RecipeRegistry.addShapedRecipe(new ItemStack(this), "   ", "eee", "e e", 'e', "gemEmerald");
      case LEGS:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this), "eee", "e e", "e e", 'e', "gemEmerald");
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
