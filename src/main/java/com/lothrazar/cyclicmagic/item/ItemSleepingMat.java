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

import java.lang.reflect.Method;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.item.IHasClickToggle;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import com.lothrazar.cyclicmagic.net.PacketSleepClient;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSleepingMat extends BaseTool implements IHasRecipe, IHasConfig, IHasClickToggle {

  // thank you for the examples forge. player data storage based on API source
  // https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
  private static final String NBT_STATUS = "cyclic_spawn";
  private static int seconds;
  public static boolean doPotions;

  public ItemSleepingMat() {
    super(100);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (player.isRiding()) {
      player.dismountRidingEntity();
    }
    if (!world.isRemote) {
      EntityPlayerMP mp = (EntityPlayerMP) player;
      //      final EntityPlayer.SleepResult result = player.trySleep(player.getPosition());
      //trySleep was changed in 1.11.2 to literally check for the specific exact  Blocks.BED in world. because fuck modders amirite?
      //and it just assumes unsafely its there and then dies.
      EntityPlayer.SleepResult result = this.canPlayerSleep(player, world);
      if (result == EntityPlayer.SleepResult.OK) {
        final IPlayerExtendedProperties sleep = CapabilityRegistry.getPlayerProperties(player);
        if (sleep != null) {
          onSleepSuccess(world, hand, stack, mp, sleep);
        }
        else {
          //should never happen... but just in case
          UtilChat.addChatMessage(player, "tile.bed.noSleep");
        }
      }
      else {
        UtilChat.addChatMessage(player, "tile.bed.noSleep");
      }
    }
    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
  }

  public void onSleepSuccess(World world, EnumHand hand, ItemStack stack, EntityPlayerMP player, final IPlayerExtendedProperties sleep) {
    sleep.setSleeping(true);
    if (doPotions) {
      player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
      player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, seconds * Const.TICKS_PER_SEC, Const.Potions.I));
    }
    this.onUse(stack, player, world, hand);
    //hack because vanilla/forge has that java.lang.IllegalArgumentException: Cannot get property PropertyDirection error with assuming its a bed when its blocks.air
    ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, player, true, "sleeping", "field_71083_bS");
    ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, player, 0, "sleepTimer", "field_71076_b");
    UtilChat.addChatMessage(player, this.getUnlocalizedName() + ".trying");
    //first set bed location
    player.bedLocation = player.getPosition();
    ModCyclic.network.sendTo(new PacketSleepClient(player.bedLocation), player);
    //then stop player in place
    player.motionX = player.motionZ = player.motionY = 0;
    world.updateAllPlayersSleepingFlag();
    //then trigger vanilla sleep event(s)
    //                world.setBlockState(player.getPosition(), Blocks.BED.getDefaultState());
    SPacketUseBed sleepPacket = new SPacketUseBed(player, player.getPosition());
    player.getServerWorld().getEntityTracker().sendToTracking(player, sleepPacket);
    player.connection.sendPacket(sleepPacket);
    if (this.isOn(stack)) {
      player.setSpawnPoint(player.getPosition(), true);//true means it wont check for bed block
    }
  }

  /**
   * hack in the vanilla sleep test, or at least something similar
   * 
   * @param player
   * @param world
   * @return
   */
  private SleepResult canPlayerSleep(EntityPlayer player, World world) {
    if (player.isEntityAlive() == false) {
      return EntityPlayer.SleepResult.OTHER_PROBLEM;
    }
    if (world.isDaytime()) {
      return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
    }
    PlayerSleepInBedEvent event = new PlayerSleepInBedEvent(player, player.getPosition());
    MinecraftForge.EVENT_BUS.post(event);
    if (event.getResultStatus() != null) {
      return event.getResultStatus();
    }
    return EntityPlayer.SleepResult.OK;
  }

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent event) {
    EntityPlayer p = event.getEntityPlayer();
    final IPlayerExtendedProperties sleep = p.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (sleep != null && sleep.isSleeping()) {
      if (p.isSneaking()) {
        //you want to cancel, ok
        sleep.setSleeping(false);
      }
      else {
        p.bedLocation = p.getPosition();
        event.setResult(Result.ALLOW);
      }
    }
  }

  @SubscribeEvent
  public void handleSleepInBed(PlayerSleepInBedEvent event) {
    EntityPlayer p = event.getEntityPlayer();
    final IPlayerExtendedProperties sleep = p.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (sleep != null && sleep.isSleeping()) {
      event.setResult(EntityPlayer.SleepResult.OK);
    }
  }

  @SubscribeEvent
  public void onWakeUp(PlayerWakeUpEvent evt) {
    EntityPlayer p = evt.getEntityPlayer();
    final IPlayerExtendedProperties sleep = p.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
    if (sleep != null && sleep.isSleeping()) {
      sleep.setSleeping(false);
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    doPotions = config.getBoolean("SleepingMatPotions", Const.ConfigCategory.items, false, "False will disable the potion effects given by the Sleeping Mat");
    seconds = config.getInt("SleepingMatPotion", Const.ConfigCategory.modpackMisc, 20, 0, 600, "Seconds of potion effect caused by using the sleeping mat");
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this),
        new ItemStack(Blocks.WOOL, 1, EnumDyeColor.RED.getMetadata()),
        "leather");
  }

  public static void setRenderOffsetForSleep(EntityPlayer mp, EnumFacing fac) {
    mp.renderOffsetX = -1.8F * fac.getFrontOffsetX();
    mp.renderOffsetZ = -1.8F * fac.getFrontOffsetZ();
    try {
      //stupid private functions in entity player
      Method m = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
      if (m != null) {
        m.invoke(mp, 0.2F, 0.2F);
      }
      else {
        ModCyclic.logger.error("Private function not found on " + Entity.class.getName() + " : setSize");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void toggle(EntityPlayer player, ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.setInteger(NBT_STATUS, vnew);
  }

  @Override
  public boolean isOn(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    if (tags.hasKey(NBT_STATUS) == false) {
      return false;//default for newlycrafted//legacy items
    }
    return tags.getInteger(NBT_STATUS) == 1;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack held, World player, List<String> list, net.minecraft.client.util.ITooltipFlag par4) {
    super.addInformation(held, player, list, par4);
    String onoff = this.isOn(held) ? "on" : "off";
    list.add(UtilChat.lang("item.sleeping_mat.tooltip.info") + UtilChat.lang("item.sleeping_mat.tooltip." + onoff));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return this.isOn(stack);
  }
}
