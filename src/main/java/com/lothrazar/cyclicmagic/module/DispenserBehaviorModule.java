package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.dispenser.BehaviorPlantSeed;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class DispenserBehaviorModule extends BaseModule implements IHasConfig {
  private boolean seedsEnabled;
  @Override
  public void onPostInit() {
    if (seedsEnabled) {
      for (Item item : Item.REGISTRY) { // GameData.getBlockItemMap().entrySet()){
        if (item != null && item instanceof IPlantable) {
          BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, new BehaviorPlantSeed());
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    Property prop = config.get(category, "Dispense Plants", true, "Dispensers can plant growable seeds");
    prop.setRequiresWorldRestart(true);
    seedsEnabled = prop.getBoolean();
  }
}
