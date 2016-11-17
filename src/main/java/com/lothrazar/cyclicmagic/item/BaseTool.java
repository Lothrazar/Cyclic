package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BaseTool extends BaseItem {
  public BaseTool(int durability) {
    super();
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
  public void onUse(ItemStack stack, EntityPlayer player, World world, EnumHand hand) {
    player.swingArm(hand);
    UtilItemStack.damageItem(player, stack);
  }
}
