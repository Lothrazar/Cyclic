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
package com.lothrazar.cyclicmagic.playerupgrade;

import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerFlying;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFlight extends ItemFood implements IHasRecipe {

  public static final int FLY_SECONDS = 2 * 60;

  public ItemFlight() {
    super(4, false);
    this.setAlwaysEdible();
  }

  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    setFlying(player);
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    props.setFlyingTimer(props.getFlyingTimer() + FLY_SECONDS * Const.TICKS_PER_SEC);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 8),
        "lll",
        "lgl",
        "lll",
        'g', "dustGlowstone",
        'l', Items.CHORUS_FRUIT);
  }

  private void setFlying(EntityPlayer player) {
    player.fallDistance = 0.0F;
    player.capabilities.allowFlying = true;
    //    player.capabilities.isFlying = true;
  }

  private void setNonFlying(EntityPlayer player) {
    player.capabilities.allowFlying = false;
    player.capabilities.isFlying = false;
    if (player instanceof EntityPlayerMP) { //force clientside  to  update
      ModCyclic.network.sendTo(new PacketSyncPlayerFlying(false), (EntityPlayerMP) player);
    }
  }

  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    int flyingTicks = props.getFlyingTimer();//TICKS NOT SECONDS
    if (flyingTicks > 1) {//it decays at 1 not zero so that we only set flying False once, not constantly. avoids having boolean flag
      setFlying(player);
      //if you are flying but not using it (grounded) dont tick.
      //this pauses the timer
      //player.onGround == false ||
      if (!player.onGround || player.world.getBlockState(player.getPosition().down()).getBlock() == Blocks.AIR) {
        props.setFlyingTimer(props.getFlyingTimer() - 1);
      }
    }
    else if (flyingTicks == 1) { //times up! only 1/20 of a second left
      props.setFlyingTimer(0);//skip ahead to zero
      setNonFlying(player);
    }
    //else it is zero. so this is the same as null/undefined/ so player has never eaten or it wore off.
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltips, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltips.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
