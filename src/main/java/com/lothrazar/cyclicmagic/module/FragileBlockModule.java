package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.item.itemblock.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class FragileBlockModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {

    BlockRegistry.block_fragile = new BlockScaffolding();
    BlockRegistry.registerBlock(BlockRegistry.block_fragile, new ItemBlockScaffolding(BlockRegistry.block_fragile), BlockScaffolding.name);
    BlockRegistry.block_fragile.addRecipe();
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
