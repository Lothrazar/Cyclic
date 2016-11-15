package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.block.BlockCropMagicBean;
import com.lothrazar.cyclicmagic.item.ItemMagicBean;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class PlantsModule extends BaseModule  implements IHasConfig{
  private boolean enableBeans;
  public void onInit() {
    if (enableBeans) {
      BlockCropMagicBean sprout = new BlockCropMagicBean();
      BlockRegistry.registerBlock(sprout, "sprout", true);
      ItemMagicBean sprout_seed = new ItemMagicBean(sprout, Blocks.FARMLAND);
      ItemRegistry.addItem(sprout_seed, "sprout_seed");
      LootTableRegistry.registerLoot(sprout_seed);
      sprout.setSeed(sprout_seed);
      AchievementRegistry.registerItemAchievement(sprout_seed);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableBeans = config.getBoolean("MagicBean", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
