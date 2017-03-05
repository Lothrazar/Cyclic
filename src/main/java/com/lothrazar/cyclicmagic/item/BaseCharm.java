package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.ICanToggleOnOff;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles", striprefs = true)
public abstract class BaseCharm extends BaseItem implements baubles.api.IBauble, ICanToggleOnOff {
  private final static String NBT_STATUS = "onoff";
  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
  public void toggleOnOff(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.setInteger(NBT_STATUS, vnew);
  }
  public boolean isOn(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    if (tags.hasKey(NBT_STATUS) == false) { return true;//default for newlycrafted//legacy items
    }
    return tags.getInteger(NBT_STATUS) == 1;
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return canTick(stack);
  }
  public boolean canTick(ItemStack stack) {
    return isOn(stack) && (stack.getItemDamage() < stack.getMaxDamage());
  }
  public void damageCharm(EntityPlayer living, ItemStack stack) {
    UtilItemStack.damageItem(living, stack);
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
  public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {}
  @Optional.Method(modid = "Baubles")
  public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {}
  @Optional.Method(modid = "Baubles")
  public void onWornTick(ItemStack stack, EntityLivingBase arg1) {
    if (!this.canTick(stack)) { return; }
    if (arg1 instanceof EntityPlayer && stack != null && stack.getCount() > 0) {
      this.onTick(stack, (EntityPlayer) arg1);
    }
  }
  @Override
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    super.addInformation(held, player, list, par4);
    String onoff = this.isOn(held) ? "on" : "off";
    list.add(UtilChat.lang("item.cantoggle.tooltip.info") + UtilChat.lang("item.cantoggle.tooltip." + onoff));
  }
}
