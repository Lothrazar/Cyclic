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
    // bonus easter egg for anyone who does not shift click. not documented
    if (this.armorType == EntityEquipmentSlot.CHEST && EnchantModule.reach != null) {
      stack.addEnchantment(EnchantModule.reach, EnchantModule.reach.getMaxLevel());
    }
  }
  private ItemStack addEnchantment(ItemStack stack) {
    switch (this.armorType) {
      case CHEST:
        stack.addEnchantment(Enchantments.PROTECTION, Enchantments.PROTECTION.getMaxLevel());
      break;
      case FEET:
        stack.addEnchantment(Enchantments.FEATHER_FALLING, Enchantments.FEATHER_FALLING.getMaxLevel());
        stack.addEnchantment(Enchantments.DEPTH_STRIDER, Enchantments.DEPTH_STRIDER.getMaxLevel());
      break;
      case HEAD:
        stack.addEnchantment(Enchantments.AQUA_AFFINITY, Enchantments.AQUA_AFFINITY.getMaxLevel());
        stack.addEnchantment(Enchantments.RESPIRATION, Enchantments.RESPIRATION.getMaxLevel());
      break;
      case LEGS:
        stack.addEnchantment(Enchantments.MENDING, Enchantments.MENDING.getMaxLevel());
      break;
    }
    return stack;
  }
  @Override
  public IRecipe addRecipe() {
    switch (this.armorType) {
      case CHEST:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "p p",
            "odo",
            "ooo",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
      case FEET:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "p p",
            "odo",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
      case HEAD:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "odo",
            "p p",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
      case LEGS:
        return RecipeRegistry.addShapedRecipe(this.addEnchantment(new ItemStack(this)),
            "odo",
            "p p",
            "o o",
            'o', "obsidian",
            'd', "gemDiamond",
            'p', Items.CHORUS_FRUIT);
    }
    return null;
  }
}
