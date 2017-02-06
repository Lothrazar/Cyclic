package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.item.gear.ItemPowerArmor;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GearPurpleModule extends BaseEventModule implements IHasConfig {
  private static final String MATERIALNAME = "power";
  private boolean enableWaterGear;
  @Override
  public void onInit() {
    registerMaterial();
    if (enableWaterGear) {
      Item purple_boots = new ItemPowerArmor(MaterialRegistry.powerArmorMaterial, EntityEquipmentSlot.FEET);
      ItemRegistry.addItem(purple_boots, "purple_boots");
      Item purple_leggings = new ItemPowerArmor(MaterialRegistry.powerArmorMaterial, EntityEquipmentSlot.LEGS);
      ItemRegistry.addItem(purple_leggings, "purple_leggings");
      Item purple_chestplate = new ItemPowerArmor(MaterialRegistry.powerArmorMaterial, EntityEquipmentSlot.CHEST);
      ItemRegistry.addItem(purple_chestplate, "purple_chestplate");
      Item purple_helmet = new ItemPowerArmor(MaterialRegistry.powerArmorMaterial, EntityEquipmentSlot.HEAD);
      ItemRegistry.addItem(purple_helmet, "purple_helmet");
    }
  }
  private void registerMaterial() {
    ArmorMaterial mimicArmor = ArmorMaterial.IRON;
    MaterialRegistry.powerArmorMaterial = EnumHelper.addArmorMaterial(MATERIALNAME, Const.MODRES + MATERIALNAME,
        15, // affects DURABILITY . 15 is the same as iron
        new int[] {
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.FEET),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.LEGS),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.CHEST),
            mimicArmor.getDamageReductionAmount(EntityEquipmentSlot.HEAD)
        },
        mimicArmor.getEnchantability(),
        mimicArmor.getSoundEvent(),
        mimicArmor.getToughness());
    MaterialRegistry.powerArmorMaterial.customCraftingMaterial = Item.getItemFromBlock(Blocks.OBSIDIAN);
  }
  @Override
  public void syncConfig(Configuration config) {
    enableWaterGear = config.getBoolean("PurpleArmor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer) {//some of the items need an off switch
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      ItemPowerArmor.checkIfHelmOff(player);
      ItemPowerArmor.checkIfLegsOff(player);
    }
  }
}
