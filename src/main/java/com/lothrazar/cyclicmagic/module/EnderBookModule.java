package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemEnderBook;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class EnderBookModule extends BaseModule {
  private boolean moduleEnabled;
  public EnderBookModule() {
    super();
    ItemRegistry.book_ender = new ItemEnderBook();
  }
  @Override
  public void register() {
    ItemRegistry.addItem(ItemRegistry.book_ender, "book_ender");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("EnderBook", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
