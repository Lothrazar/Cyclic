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
package com.lothrazar.cyclicmagic.item.mobs;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemVillagerMagic extends BaseItem implements IHasRecipe {

  private static final int CONVTIME = 1200;

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ea",
        "gi",
        'e', "gemEmerald",
        'a', Items.APPLE,
        'g', "nuggetGold",
        'i', "nuggetIron");
  }

  @Override
  @SideOnly(Side.CLIENT)
  public EnumRarity getRarity(ItemStack par1ItemStack) {
    return EnumRarity.RARE;
  }

  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) event.getEntity();
    //    ItemStack held = player.getHeldItemMainhand();
    ItemStack itemstack = event.getItemStack();
    if (itemstack != null && itemstack.getItem() instanceof ItemVillagerMagic && itemstack.getCount() > 0) {
      if (event.getTarget() instanceof EntityVillager) {
        EntityVillager villager = ((EntityVillager) event.getTarget());
        int count = 0;
        for (MerchantRecipe merchantrecipe : villager.getRecipes(player)) {
          if (merchantrecipe.isRecipeDisabled()) {
            //vanilla code as of 1.9.4 odes this (2d6+2) 
            merchantrecipe.increaseMaxTradeUses(villager.getEntityWorld().rand.nextInt(6) + villager.getEntityWorld().rand.nextInt(6) + 2);
            count++;
          }
        }
        if (count > 0) {
          UtilChat.addChatMessage(player, UtilChat.lang("item.apple_emerald.merchant") + count);
          consumeSelf(itemstack);
        }
        event.setCanceled(true);// stop the GUI inventory opening && horse mounting
      }
    }
  }

  private void consumeSelf(ItemStack itemstack) {
    itemstack.shrink(1);
    if (itemstack.getCount() == 0) {
      itemstack = ItemStack.EMPTY;
    }
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (entity instanceof EntityZombieVillager
        && entity.isChild() == false) {
      EntityZombieVillager zombie = ((EntityZombieVillager) entity);
      if (zombie.isConverting() == false) { // dont waste a second one if already converting
        //this is what we WANT to do, but the method is protected. we have to fake it by faking the interact event
        //      zombie.startConverting(1200);
        consumeSelf(itemstack);
        startConverting(zombie, CONVTIME);
        zombie.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 1));
        return true;
      }
    }
    return super.itemInteractionForEntity(itemstack, player, entity, hand);
  }

  private void startConverting(EntityZombieVillager v, int t) {
    //      v.conversionTime = t;
    ObfuscationReflectionHelper.setPrivateValue(EntityZombieVillager.class, v, t, "conversionTime", "field_82234_d");
    v.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, t, Math.min(v.world.getDifficulty().getDifficultyId() - 1, 0)));
    v.world.setEntityState(v, (byte) 16);
    try {
      //       v.getDataManager().set(CONVERTING, Boolean.valueOf(true));
      DataParameter<Boolean> CONVERTING = ObfuscationReflectionHelper.getPrivateValue(EntityZombieVillager.class, v, "CONVERTING", "field_184739_bx");
      v.getDataManager().set(CONVERTING, Boolean.valueOf(true));
    }
    catch (Exception e) {}
  }
}
