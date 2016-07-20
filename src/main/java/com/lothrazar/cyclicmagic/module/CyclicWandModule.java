package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class CyclicWandModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    ItemRegistry.addItem(ItemRegistry.cyclic_wand_build, "cyclic_wand_build");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("CyclicWand", Const.ConfigCategory.content, true, "Set false to delete this");
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
