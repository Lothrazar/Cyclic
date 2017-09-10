package com.lothrazar.cyclicmagic.item.gear;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.module.EnchantModule;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

@SuppressWarnings("incomplete-switch")
public class ItemPowerArmor extends ItemArmor implements IHasRecipe {
  public ItemPowerArmor(EntityEquipmentSlot armorType) {
    super(MaterialRegistry.powerArmorMaterial, 0, armorType);
  }
  @Override
  public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    //    stack.addEnchantment(Enchantments.UNBREAKING, Enchantments.UNBREAKING.getMaxLevel());
    switch (this.armorType) {
      case CHEST:
        if (EnchantModule.reach != null)
          stack.addEnchantment(EnchantModule.reach, EnchantModule.reach.getMaxLevel());
      break;
      case FEET:
        stack.addEnchantment(Enchantments.PROTECTION, Enchantments.DEPTH_STRIDER.getMaxLevel());
      break;
      case HEAD:
        stack.addEnchantment(Enchantments.AQUA_AFFINITY, Enchantments.AQUA_AFFINITY.getMaxLevel());
        stack.addEnchantment(Enchantments.RESPIRATION, Enchantments.RESPIRATION.getMaxLevel());
      break;
      case LEGS:
        stack.addEnchantment(Enchantments.PROTECTION, Enchantments.PROTECTION.getMaxLevel());
      break;
    }
  }
  @Override
  public IRecipe addRecipe() {
    switch (this.armorType) {
      case CHEST:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "p p",
            "o o",
            "ooo",
            'o', "obsidian",
            'p', Items.CHORUS_FRUIT);
      case FEET:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "   ",
            "p p",
            "ooo",
            'o', "obsidian",
            'p', Items.CHORUS_FRUIT);
      case HEAD:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "ooo",
            "p p",
            "   ",
            'o', "obsidian",
            'p', Items.CHORUS_FRUIT);
      case LEGS:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "ooo",
            "p p",
            "o o",
            'o', "obsidian",
            'p', Items.CHORUS_FRUIT);
    }
    return null;
  }
}
