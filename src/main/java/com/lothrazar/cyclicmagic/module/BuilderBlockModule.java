package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class BuilderBlockModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    BlockRegistry.registerBlock(BlockRegistry.builder_block, "builder_block");
    BlockRegistry.builder_block.addRecipe();
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockRegistry.builder_block.syncConfig(config);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
