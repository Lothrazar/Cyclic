package com.lothrazar.cyclicmagic.module;

import com.lothrazar.cyclicmagic.item.ItemInventoryStorage;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class StorageBagModule extends BaseModule{
  private boolean moduleEnabled;
  public StorageBagModule(){
    super();
    ItemRegistry.storage_bag = new ItemInventoryStorage();
  }
  @Override
  public void register() {

    ItemRegistry.addItem(ItemRegistry.storage_bag, "storage_bag");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("StorageBag", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
