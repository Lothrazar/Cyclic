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
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.client.gui.screen.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.EnderChestTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemHorseEnder extends ItemEntityInteractable {

  private static final String NBT_KEYACTIVE = ModCyclic.MODID + "_carrot_ender";

  public ItemHorseEnder(Properties prop) {
    super(prop);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void addCustomButtonToInventory(GuiScreenEvent.InitGuiEvent.Post event) {
    //
    if (event.getGui() instanceof HorseInventoryScreen) {
      ModCyclic.LOGGER.info("ender teleport gui screen " + event.getGui());
      //
      //event.getGui().width +
      event.addWidget(new Button(event.getGui().width / 2 - 8,
          event.getGui().height / 2 - 82,
          20, 20, new StringTextComponent("EC"), b -> {
            ModCyclic.LOGGER.info("ender teleport button click screen " + event.getGui());
            PlayerEntity player = ModCyclic.proxy.getClientPlayer();
            if (player == null) {
              return;
            }
            // 
            EnderChestTileEntity y;
            player.closeScreen();
            EnderChestInventory enderchestinventory = player.getInventoryEnderChest();
            enderchestinventory.setChestTileEntity(null);
            player.openContainer(new SimpleNamedContainerProvider((p_220114_1_, p_220114_2_, p_220114_3_) -> {
              return ChestContainer.createGeneric9X3(p_220114_1_, p_220114_2_, enderchestinventory);
            }, new TranslationTextComponent("container.enderchest")));//EnderChestBlock.CONTAINER_NAME));//stupid mojang makes things private for no reason becasue  they hate modders
            //..
            //            player.open
            player.addStat(Stats.OPEN_ENDERCHEST);
            UtilSound.playSound(player, SoundEvents.BLOCK_ENDER_CHEST_OPEN);
          }));
    }
  }

  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    if (event.getEntityLiving() instanceof AbstractHorseEntity) {
      //
      //      AbstractHorseEntity ahorse = (AbstractHorseEntity) event.getTarget();
      if (event.getEntityLiving().getPersistentData().contains(NBT_KEYACTIVE)
          && event.getEntityLiving().getPersistentData().getBoolean(NBT_KEYACTIVE)) {
        ModCyclic.LOGGER.info("ender teleport horse");
        //
      }
    }
  }

  @Override
  public void interactWith(EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof AbstractChestedHorseEntity
        && !event.getPlayer().getCooldownTracker().hasCooldown(this)) {
      // lets go 
      AbstractChestedHorseEntity ahorse = (AbstractChestedHorseEntity) event.getTarget();
      if (ahorse.isHorseSaddled()//tamed and not containing the upgrade? eat it! 
          && !ahorse.hasChest()) {
        //do the thing
        ahorse.setChested(true);
        ahorse.getPersistentData().putBoolean(NBT_KEYACTIVE, true);
        ModCyclic.LOGGER.info("set chested teleport horse");
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
        event.getPlayer().getCooldownTracker().setCooldown(this, 10);
        event.getItemStack().shrink(1);
      }
    }
  }
}
