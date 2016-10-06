package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilSound; 
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles", striprefs = true)
public abstract class BaseCharm extends BaseItem {
  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  public void damageCharm(EntityPlayer living, ItemStack stack) {
    UtilItem.damageItem(living, stack);
    if (stack == null || stack.getItemDamage() == stack.getMaxDamage() || stack.stackSize == 0) {
      stack.stackSize = 0;
      stack = null;
      //      living.inventory.setInventorySlotContents(itemSlot, null);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, living.getSoundCategory());
    }
  }
  /**
   * Fires while in inventory OR while in bauble slot
   * 
   * @param arg0
   * @param arg1
   */
  public abstract void onTick(ItemStack arg0, EntityPlayer arg1);
  @Optional.Method(modid = "Baubles")
  public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
    return true;
  }
  @Optional.Method(modid = "Baubles")
  public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
    return true;
  }
  @Optional.Method(modid = "Baubles")
  public baubles.api.BaubleType getBaubleType(ItemStack arg0) {
    return baubles.api.BaubleType.RING;
  }
  @Optional.Method(modid = "Baubles")
  public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {
  }
  @Optional.Method(modid = "Baubles")
  public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {
  }
  @Optional.Method(modid = "Baubles")
  public void onWornTick(ItemStack arg0, EntityLivingBase arg1) {
    if (arg1 instanceof EntityPlayer && arg0 != null && arg0.stackSize > 0) {
      this.onTick(arg0, (EntityPlayer) arg1);
    }
  }
}
