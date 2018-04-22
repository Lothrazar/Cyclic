/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.equipment;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.IHasClickToggle;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGlowingHelmet extends ItemArmor implements IHasRecipe, IHasClickToggle {

  public static final String NBT_GLOW = Const.MODID + "_glow";
  private final static String NBT_STATUS = "onoff";

  public ItemGlowingHelmet(EntityEquipmentSlot armorType) {
    super(MaterialRegistry.glowingArmorMaterial, 0, armorType);
  }

  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    boolean isTurnedOn = this.isOn(itemStack);
    setGlowing(player, isTurnedOn);
    if (isTurnedOn)
      setNightVision(player);
  }

  private void setNightVision(EntityPlayer player) {
    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20 * Const.TICKS_PER_SEC, 0));
  }

  public static void checkIfHelmOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EntityEquipmentSlot.HEAD);
    if (player.getEntityData().getBoolean(ItemGlowingHelmet.NBT_GLOW) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemGlowingHelmet))) {
      //turn it off once, from the message
      setGlowing(player, false);
    }
  }

  public static void setGlowing(EntityPlayer player, boolean hidden) {
    player.setGlowing(hidden);//hidden means dont render
    //flag it so we know the purple glow was from this item, not something else
    player.getEntityData().setBoolean(NBT_GLOW, hidden);
    player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack held, World player, List<String> list, net.minecraft.client.util.ITooltipFlag par4) {
    list.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
    String onoff = this.isOn(held) ? "on" : "off";
    list.add(UtilChat.lang("item.cantoggle.tooltip.info") + " " + UtilChat.lang("item.cantoggle.tooltip." + onoff));
    super.addInformation(held, player, list, par4);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ioi",
        "o o",
        "   ",
        'i', "dyeOrange",
        'o', "glowstone");
  }

  public void toggle(EntityPlayer player, ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.setInteger(NBT_STATUS, vnew);
  }

  public boolean isOn(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    if (tags.hasKey(NBT_STATUS) == false) {
      return true;
    } //default for newlycrafted//legacy items
    return tags.getInteger(NBT_STATUS) == 1;
  }

  /**
   * TODO: maybe should be static inside powerarmor class?
   * 
   * @param event
   */
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer) {//some of the items need an off switch
      EntityPlayer player = (EntityPlayer) event.getEntityLiving();
      ItemGlowingHelmet.checkIfHelmOff(player);
      //      ItemPowerArmor.checkIfLegsOff(player);
    }
  }
}
