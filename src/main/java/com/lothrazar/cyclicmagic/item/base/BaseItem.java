package com.lothrazar.cyclicmagic.item.base;
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BaseItem extends Item {
  protected String getTooltip() {
    return this.getUnlocalizedName() + ".tooltip";
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip,net.minecraft.client.util.ITooltipFlag advanced) {
    if (getTooltip() != null) {
      tooltip.add(UtilChat.lang(getTooltip()));
    }
    super.addInformation(stack, player, tooltip, advanced);
  }
}
