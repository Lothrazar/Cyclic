package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.item.gear.ItemPowerArmor;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class GearBootsModule extends BaseModule implements IHasConfig {
  private boolean enableWaterGear=true;

  @Override
  public void onInit() {
    if (enableWaterGear) {
 
 
      Item water_boots = new ItemPowerArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.addItem(water_boots, "water_boots"); 
    }
  }
  @Override
  public void syncConfig(Configuration config) {
//    enableEmeraldGear = config.getBoolean("Emerald Gear", Const.ConfigCategory.content, true, "Emerald armor and tools that are slightly weaker than diamond. " + Const.ConfigCategory.contentDefaultText);
  }
}
