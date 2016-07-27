package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class EmeraldAppleModule extends BaseModule {
  private boolean moduleEnabled;
  @Override
  public void register() {
    ItemRegistry.apple_emerald = new ItemAppleEmerald();
    ItemRegistry.addItem(ItemRegistry.apple_emerald, "apple_emerald");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("EmeraldApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
