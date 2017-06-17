package com.lothrazar.cyclicmagic.item.base;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class BaseItem extends Item {
  //  private boolean hideFromCreativeTab = false;
  //  public BaseItem setHidden() {
  //    this.hideFromCreativeTab = true;
  //    return this;
  //  }
  //  public void register(String name) {
  //    ItemRegistry.registerItem(this, name, this.hideFromCreativeTab);
  //  }
  protected String getTooltip() {
    return this.getUnlocalizedName() + ".tooltip";
  }
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
    if (getTooltip() != null) {
      tooltip.add(UtilChat.lang(getTooltip()));
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
