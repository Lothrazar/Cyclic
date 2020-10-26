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
package com.lothrazar.cyclic.item.carrot;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemEntityInteractable;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilParticle;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class ItemHorseEnder extends ItemEntityInteractable {

  private static final String NBT_KEYACTIVE = ModCyclic.MODID + "_carrot_ender";

  public ItemHorseEnder(Properties prop) {
    super(prop);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void addCustomButtonToInventory(GuiScreenEvent.InitGuiEvent.Post event) {
    //
    if (event.getGui() instanceof HorseInventoryScreen
        && ModCyclic.proxy.getClientPlayer() != null
        && ModCyclic.proxy.getClientPlayer().getRidingEntity() != null) {
      //      ModCyclic.LOGGER.info("ender teleport gui screen " + event.getGui());
      // 
      //      HorseInventoryScreen hs = (HorseInventoryScreen) event.getGui();
      Entity liv = ModCyclic.proxy.getClientPlayer().getRidingEntity();
      if (liv.getPersistentData().contains(NBT_KEYACTIVE)
          && liv.getPersistentData().getInt(NBT_KEYACTIVE) > 0) {
        //
        int ct = liv.getPersistentData().getInt(NBT_KEYACTIVE);
        //        String test = UtilChat.lang("cyclic.carrot_ender.count") + ct;
        ExtendedButton bt2 = new ExtendedButton(event.getGui().width / 2 + 68,
            event.getGui().height / 2 - 80,
            //cyclic.carrot_ender.charges
            18, 14, new StringTextComponent("" + ct), b -> {
              //              if(event.i)
              UtilChat.addChatMessage(ModCyclic.proxy.getClientPlayer(), "item.cyclic.carrot_ender.tooltip");
              //                  test);
            }) {};
        //        bt2.active = false;
        event.addWidget(bt2);
      }
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity liv = event.getEntityLiving();
    if (//!liv.world.isRemote &&
    liv.getPersistentData().contains(NBT_KEYACTIVE)
        && liv.getPersistentData().getInt(NBT_KEYACTIVE) > 0) {
      // 
      if (liv.isInWater()
          && liv.canBreatheUnderwater() == false
          && liv.getAir() < liv.getMaxAir()
          && !liv.isPotionActive(Effects.WATER_BREATHING)) {
        liv.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 20 * 60, 4));
        liv.addPotionEffect(new EffectInstance(PotionRegistry.PotionEffects.swimspeed, 20 * 60, 1));
        onSuccess(liv);
      }
      if (liv.isBurning()
          && !liv.isPotionActive(Effects.FIRE_RESISTANCE)) {
        liv.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 20 * 60, 4));
        liv.extinguish();
        onSuccess(liv);
      }
      if (liv.fallDistance > 12
          && !liv.isPotionActive(Effects.SLOW_FALLING)) {
        liv.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 4));
        //        if (liv.getPassengers().size() > 0) {
        //          liv.getPassengers().get(0).addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 1));
        //        }
        onSuccess(liv);
      }
      if (liv.getHealth() < 6
          && !liv.isPotionActive(Effects.ABSORPTION)) {
        liv.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20 * 60, 4));
        liv.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20 * 60, 4));
        onSuccess(liv);
      }
    }
  }

  private void onSuccess(LivingEntity liv) {
    UtilSound.playSound(liv, SoundEvents.ENTITY_GENERIC_DRINK);
    UtilParticle.spawnParticle(liv.world, ParticleTypes.CRIT, liv.getPosition(), 3);
    increment(liv, -1);
    //    ModCyclic.LOGGER.info("carrot_ender triggered down " + liv.getPersistentData().getInt(NBT_KEYACTIVE));
    //    int current = ahorse.getPersistentData().getInt(NBT_KEYACTIVE);
    //    UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang("cyclic.carrot_ender.count") + current);
  }

  @Override
  public void interactWith(EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof AbstractHorseEntity
        && !event.getPlayer().getCooldownTracker().hasCooldown(this)) {
      // lets go 
      AbstractHorseEntity ahorse = (AbstractHorseEntity) event.getTarget();
      if (event.getTarget() instanceof AbstractChestedHorseEntity
          && ahorse.isTame()) {
        AbstractChestedHorseEntity ss = (AbstractChestedHorseEntity) event.getTarget();
        ss.setChested(true);
      }
      //do the thing 
      increment(ahorse, 1);
      ModCyclic.LOGGER.info("set   teleport horse");
      event.setCanceled(true);
      event.setCancellationResult(ActionResultType.SUCCESS);
      event.getPlayer().getCooldownTracker().setCooldown(this, 1);
      event.getItemStack().shrink(1);
      int current = ahorse.getPersistentData().getInt(NBT_KEYACTIVE);
      UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang("cyclic.carrot_ender.count") + current);
    }
  }

  private void increment(LivingEntity ahorse, int val) {
    int old = ahorse.getPersistentData().getInt(NBT_KEYACTIVE);
    ahorse.getPersistentData().putInt(NBT_KEYACTIVE, old + val);
  }
}
