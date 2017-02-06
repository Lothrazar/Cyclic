package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.item.gear.ItemPowerArmor;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GearBootsModule extends BaseModule implements IHasConfig {
  private static final int maxDamageFactorLeather = 5;
  private static final String materialName = "power";
  private boolean enableWaterGear = true;
  private boolean enableLegs;
  @Override
  public void onInit() {
    MaterialRegistry.powerArmorMaterial = EnumHelper.addArmorMaterial(materialName, Const.MODRES + materialName,
        maxDamageFactorLeather, // affects DURABILITY 
        new int[] {
            ArmorMaterial.LEATHER.getDamageReductionAmount(EntityEquipmentSlot.FEET), ArmorMaterial.LEATHER.getDamageReductionAmount(EntityEquipmentSlot.LEGS), ArmorMaterial.LEATHER.getDamageReductionAmount(EntityEquipmentSlot.CHEST), ArmorMaterial.LEATHER.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        ArmorMaterial.LEATHER.getEnchantability(),
        ArmorMaterial.LEATHER.getSoundEvent(),
        ArmorMaterial.LEATHER.getToughness());
    MaterialRegistry.powerArmorMaterial.customCraftingMaterial = Items.LEATHER;
    if (enableWaterGear) {
      Item purple_boots = new ItemPowerArmor(MaterialRegistry.powerArmorMaterial, EntityEquipmentSlot.FEET);
      ItemRegistry.addItem(purple_boots, "purple_boots");
      GameRegistry.addShapedRecipe(new ItemStack(purple_boots),
          "   ", "p p", "o o",
          'o', Blocks.OBSIDIAN,
          'p', new ItemStack(Items.DYE, 1, EnumDyeColor.MAGENTA.getDyeDamage()));
    }
    if (enableLegs) {
      Item purple_leggings = new ItemPowerArmor(MaterialRegistry.powerArmorMaterial, EntityEquipmentSlot.LEGS);
      ItemRegistry.addItem(purple_leggings, "purple_leggings");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableWaterGear = config.getBoolean("HermeticBoots", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLegs = config.getBoolean("SilkweaveLeggings", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
