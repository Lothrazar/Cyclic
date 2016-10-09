package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockConveyor;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ConveyorPlateModule extends BaseModule {
  private boolean enableConveyor;
  public void onInit() {
    if (enableConveyor) {
      BlockConveyor plate_push = new BlockConveyor(0.16F, SoundEvents.BLOCK_ANVIL_BREAK);
      BlockRegistry.registerBlock(plate_push, "plate_push");
      plate_push.addRecipe();
      AchievementRegistry.registerItemAchievement(Item.getItemFromBlock(plate_push));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
