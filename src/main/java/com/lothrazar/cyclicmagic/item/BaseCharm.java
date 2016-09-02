package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseCharm extends BaseItem {
  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  public void damageCharm(EntityPlayer living, ItemStack stack, int itemSlot) {
    UtilItem.damageItem(living, stack);
    if (stack == null || stack.getItemDamage() == stack.getMaxDamage()) {
      living.inventory.setInventorySlotContents(itemSlot, null);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, living.getSoundCategory());
    }
  }
}
