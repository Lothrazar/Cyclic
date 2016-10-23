package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.ItemStorageBag;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class StorageBagModule extends BaseModule {
  private boolean moduleEnabled;
  @Override
  public void onInit() {
    if (moduleEnabled) {
      ItemRegistry.storage_bag = new ItemStorageBag();
      ItemRegistry.addItem(ItemRegistry.storage_bag, "storage_bag");
      ModMain.instance.events.addEvent(ItemRegistry.storage_bag);
      LootTableRegistry.registerLoot(ItemRegistry.storage_bag);
      LootTableRegistry.registerLoot(ItemRegistry.storage_bag, ChestType.BONUS, 25);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("StorageBag", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
