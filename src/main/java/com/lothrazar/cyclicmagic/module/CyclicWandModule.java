package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class CyclicWandModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    ItemRegistry.cyclic_wand_build = new ItemCyclicWand();
    ItemRegistry.addItem(ItemRegistry.cyclic_wand_build, "cyclic_wand_build");

    SpellRegistry.register();
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("CyclicWand", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
