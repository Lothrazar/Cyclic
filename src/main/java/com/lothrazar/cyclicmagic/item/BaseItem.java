package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.item.Item;

public class BaseItem extends Item {
  private boolean hideFromCreativeTab = false;
  public BaseItem setHidden() {
    this.hideFromCreativeTab = true;
    return this;
  }
  public void register(String name) {
    ItemRegistry.registerItem(this, name, this.hideFromCreativeTab);
  }
}
