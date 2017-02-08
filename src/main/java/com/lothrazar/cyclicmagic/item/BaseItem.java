package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class BaseItem extends Item {
  private boolean hideFromCreativeTab = false;
  public BaseItem setHidden() {
    this.hideFromCreativeTab = true;
    return this;
  }
  public void register(String name) {
    ItemRegistry.registerItem(this, name, this.hideFromCreativeTab);
  }
  protected String getTooltip() {
    return this.getUnlocalizedName() + ".tooltip";
  }
  @Override
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    if (getTooltip() != null) {
      list.add(UtilChat.lang(getTooltip()));
    }
  }
}
