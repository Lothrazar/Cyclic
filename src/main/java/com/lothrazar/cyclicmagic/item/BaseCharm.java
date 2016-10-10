package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.util.UtilItem;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles", striprefs = true)
public abstract class BaseCharm extends BaseItem implements baubles.api.IBauble {
  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return canTick(stack);
  }
  public boolean canTick(ItemStack stack) {
    return stack.getItemDamage() < stack.getMaxDamage();
  }
  public void damageCharm(EntityPlayer living, ItemStack stack) {
    UtilItem.damageItem(living, stack);
    if (stack == null || stack.getItemDamage() == stack.getMaxDamage() || stack.stackSize == 0) {
      if (stack != null && stack.stackSize == 0) {
        stack.stackSize = 1;//avoid that red zero if baulbes doesnt make it gone
      }
      //      living.inventory.setInventorySlotContents(itemSlot, null);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, living.getSoundCategory());
    }
  }
  public void addRecipeAndRepair(Item craftItem) {
    this.addRecipeAndRepair(new ItemStack(craftItem));
  }
  public void addRecipeAndRepair(ItemStack craftItem) {
    GameRegistry.addRecipe(new ItemStack(this),
        "r x",
        "id ",
        "iir",
        'x', craftItem,
        'd', Items.DIAMOND,
        'r', Items.NETHER_WART,
        'i', Items.IRON_INGOT);
    GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE), craftItem);
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
    try {
      if (baubles.api.BaubleType.values().length >= 4) { //length is 4 if trinket exists
        return baubles.api.BaubleType.TRINKET;
      }
      else {
        return baubles.api.BaubleType.RING;
      }
    }
    catch (Exception e) {
      return baubles.api.BaubleType.RING;
    }
  }
  @Optional.Method(modid = "Baubles")
  public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {
  }
  @Optional.Method(modid = "Baubles")
  public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {
  }
  @Optional.Method(modid = "Baubles")
  public void onWornTick(ItemStack stack, EntityLivingBase arg1) {
    if (!this.canTick(stack)) { return; }
    if (arg1 instanceof EntityPlayer && stack != null && stack.stackSize > 0) {
      this.onTick(stack, (EntityPlayer) arg1);
    }
  }
}
