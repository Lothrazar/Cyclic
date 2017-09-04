package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.plant.ItemMagicBean;
//import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class PlantsModule extends BaseModule implements IHasConfig {
  private boolean enableBeans;
  @Override
  public void onPreInit() {
    if (enableBeans) {
      BlockCropMagicBean sprout = new BlockCropMagicBean();
      BlockRegistry.registerBlock(sprout, "sprout", null);
      ItemMagicBean sprout_seed = new ItemMagicBean(sprout, Blocks.FARMLAND);
      ItemRegistry.register(sprout_seed, "sprout_seed");
      LootTableRegistry.registerLoot(sprout_seed);
      sprout.setSeed(sprout_seed);
      //      AchievementRegistry.registerItemAchievement(sprout_seed);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableBeans = config.getBoolean("MagicBean", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
