package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockSprout;
import com.lothrazar.cyclicmagic.item.ItemSproutSeeds;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class MagicBeanModule extends BaseModule {
  private boolean moduleEnabled;
  public void onInit() {
    if(moduleEnabled){
      BlockSprout sprout = new BlockSprout();
      BlockRegistry.registerBlock(sprout, "sprout", true);
      ItemRegistry.sprout_seed = new ItemSproutSeeds(sprout, Blocks.FARMLAND);
      ItemRegistry.addItem(ItemRegistry.sprout_seed, "sprout_seed");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("MagicBean", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
