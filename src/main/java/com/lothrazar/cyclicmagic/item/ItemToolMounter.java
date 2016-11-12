package com.lothrazar.cyclicmagic.item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemToolMounter extends BaseTool {
  public ItemToolMounter(int durability) {
    super(durability);
  }
  /**
   * Returns true if the item can be used on the given entity, e.g. shears on
   * sheep.
   */
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (entity.worldObj.isRemote) { return false; }
//   System.out.println( entity.getClass().getName());
//   System.out.println( entity.getClass().getSimpleName());
    player.startRiding(entity, true);
    super.onUse(stack, player, player.worldObj, hand);
    return true;
  }
}
