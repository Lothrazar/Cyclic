package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldArmor;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldPickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldSpade;
import com.lothrazar.cyclicmagic.item.gear.ItemEmeraldSword;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

public class GearEmeraldModule extends BaseModule implements IHasConfig {
  private boolean enableEmeraldGear;
  // thanks for help:
  // http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  private static final int maxDamageFactorDiamond = 33;
  private static final String emeraldName = "emerald";
  //from ArmorMaterial.DIAMOND, second constuctor param
  //used as a ratio for durability
  // only because theyre private, with no getters
  //  private static final int    diamondDurability       = 33;
  //private static final int[]  diamondreductionAmounts = new int[] { 3, 6, 8, 3 };
  private void registerEmeraldMaterial() {
    MaterialRegistry.emeraldArmorMaterial = EnumHelper.addArmorMaterial(emeraldName, Const.MODRES + emeraldName,
        maxDamageFactorDiamond, //was -2 affects DURABILITY 
        new int[] {
            ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.FEET), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.LEGS), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.CHEST), ArmorMaterial.DIAMOND.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        ArmorMaterial.GOLD.getEnchantability(),
        ArmorMaterial.DIAMOND.getSoundEvent(),
        ArmorMaterial.DIAMOND.getToughness());//was  / 2
    MaterialRegistry.emeraldArmorMaterial.customCraftingMaterial = Items.EMERALD;
    //max uses is durability ex The number of uses this material allows.
    //as of 1.9.4 :  (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
    MaterialRegistry.emeraldToolMaterial = EnumHelper.addToolMaterial(emeraldName,
        ToolMaterial.DIAMOND.getHarvestLevel(),
        ToolMaterial.DIAMOND.getMaxUses(), //was  - 261
        ToolMaterial.DIAMOND.getEfficiencyOnProperMaterial(),
        ToolMaterial.DIAMOND.getDamageVsEntity(), //was  - 0.25F
        ToolMaterial.GOLD.getEnchantability());
    MaterialRegistry.emeraldToolMaterial.setRepairItem(new ItemStack(MaterialRegistry.emeraldArmorMaterial.customCraftingMaterial));
  }
  @Override
  public void onInit() {
    if (enableEmeraldGear) {
      this.registerEmeraldMaterial();
      Item emerald_head = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
      ItemRegistry.addItem(emerald_head, "emerald_helmet");
      Item emerald_chest = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
      ItemRegistry.addItem(emerald_chest, "emerald_chestplate");
      Item emerald_legs = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
      ItemRegistry.addItem(emerald_legs, "emerald_leggings");
      Item emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.addItem(emerald_boots, "emerald_boots");
      Item emerald_sword = new ItemEmeraldSword();
      ItemRegistry.addItem(emerald_sword, ItemEmeraldSword.name);
      Item emerald_pickaxe = new ItemEmeraldPickaxe();
      ItemRegistry.addItem(emerald_pickaxe, ItemEmeraldPickaxe.name);
      Item emerald_axe = new ItemEmeraldAxe();
      ItemRegistry.addItem(emerald_axe, ItemEmeraldAxe.name);
      Item emerald_shovel = new ItemEmeraldSpade();
      ItemRegistry.addItem(emerald_shovel, ItemEmeraldSpade.name);
      Item emerald_hoe = new ItemEmeraldHoe();
      ItemRegistry.addItem(emerald_hoe, ItemEmeraldHoe.name);
      LootTableRegistry.registerLoot(emerald_pickaxe);
      LootTableRegistry.registerLoot(emerald_sword);
      LootTableRegistry.registerLoot(emerald_boots);
      LootTableRegistry.registerLoot(emerald_chest);
      LootTableRegistry.registerLoot(emerald_head);
      LootTableRegistry.registerLoot(emerald_legs);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableEmeraldGear = config.getBoolean("Emerald Gear", Const.ConfigCategory.content, true, "Emerald armor and tools that are slightly weaker than diamond. " + Const.ConfigCategory.contentDefaultText);
  }
}
