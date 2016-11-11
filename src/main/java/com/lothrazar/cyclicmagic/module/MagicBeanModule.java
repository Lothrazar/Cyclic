package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.block.BlockSprout;
import com.lothrazar.cyclicmagic.item.ItemSproutSeeds;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class MagicBeanModule extends BaseModule  implements IHasConfig{
  private boolean enableBeans;
  public void onInit() {
    if (enableBeans) {
      BlockSprout sprout = new BlockSprout();
      BlockRegistry.registerBlock(sprout, "sprout", true);
      ItemSproutSeeds sprout_seed = new ItemSproutSeeds(sprout, Blocks.FARMLAND);
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
