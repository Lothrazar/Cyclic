package com.lothrazar.cyclicmagic.item.base;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseItem extends Item {
  protected String getTooltip() {
    return this.getUnlocalizedName() + ".tooltip";
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    if (getTooltip() != null) {
      list.add(UtilChat.lang(getTooltip()));
    }
  }
}
