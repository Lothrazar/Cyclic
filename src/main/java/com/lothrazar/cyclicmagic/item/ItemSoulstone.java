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
package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseItem;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoulstone extends BaseItem implements IHasRecipe {

  public ItemSoulstone() {
    super();
    this.setMaxStackSize(1);
  }

  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {
    if (event.getEntityLiving().getHealth() - event.getAmount() <= 0 && event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) event.getEntityLiving();
      for (int i = 0; i < p.inventory.getSizeInventory(); ++i) {
        ItemStack s = p.inventory.getStackInSlot(i);
        if (s.getItem() instanceof ItemSoulstone) {
          UtilChat.addChatMessage(p, event.getEntityLiving().getName() + UtilChat.lang("item.soulstone.used"));
          p.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
          UtilSound.playSound(p, SoundEvents.BLOCK_GLASS_BREAK);
          p.setHealth(6);// 3 hearts
          int time = Const.TICKS_PER_SEC * 30;
          p.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, time));
          time = Const.TICKS_PER_SEC * 60;//a full minute
          p.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, time));
          p.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, time, 4));
          //and bad luck lasts much longer
          time = Const.TICKS_PER_SEC * 60 * 10;
          p.addPotionEffect(new PotionEffect(MobEffects.UNLUCK, time));
          p.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, time, 1));
          event.setCanceled(true);
          break;
        }
      }
    }
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " a ", "bsc", " d ",
        's', "netherStar",
        'a', Items.GOLDEN_APPLE,
        'b', Items.POISONOUS_POTATO,
        'c', Blocks.PURPUR_BLOCK,
        'd', "gemEmerald");
  }
}
