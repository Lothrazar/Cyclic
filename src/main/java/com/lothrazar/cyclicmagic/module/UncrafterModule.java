package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class UncrafterModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    BlockRegistry.registerBlock(BlockRegistry.uncrafting_block, "uncrafting_block");
    BlockRegistry.uncrafting_block.addRecipe();
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, "Set false to delete");
    BlockRegistry.uncrafting_block.syncConfig(config);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
