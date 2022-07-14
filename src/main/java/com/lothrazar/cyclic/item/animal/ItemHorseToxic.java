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
package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemHorseToxic extends ItemBaseCyclic implements IEntityInteractable {

  public ItemHorseToxic(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(EntityInteract event) {
    if (event.getItemStack().getItem() == this
        && event.getTarget() instanceof Horse
        && event.getWorld() instanceof ServerLevel
        && !event.getPlayer().getCooldowns().isOnCooldown(this)) {
      // lets go 
      Horse horseOldEntity = (Horse) event.getTarget();
      ZombieHorse zombieNewEntity = EntityType.ZOMBIE_HORSE.spawn((ServerLevel) event.getWorld(), null, null, event.getPlayer(), event.getPos(), MobSpawnType.NATURAL, false, false);
      event.getWorld().addFreshEntity(zombieNewEntity);
      if (horseOldEntity.isTamed() && horseOldEntity.getOwnerUUID() == event.getPlayer().getUUID()) {
        // you still tamed it
        zombieNewEntity.tameWithName(event.getPlayer());
      }
      if (horseOldEntity.isSaddled()) {
        zombieNewEntity.equipSaddle(SoundSource.PLAYERS);
      }
      IItemHandler horseChest = horseOldEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
      if (horseChest != null && horseChest.getSlots() >= 2) {
        //dont drop saddle since i re-saddle. drop horse arm
        ItemStackUtil.drop(event.getWorld(), event.getPos(), horseChest.getStackInSlot(1));
      }
      if (horseOldEntity.hasCustomName()) {
        zombieNewEntity.setCustomName(horseOldEntity.getCustomName());
      }
      //remove the horse    
      horseOldEntity.remove(Entity.RemovalReason.DISCARDED);
      event.getPlayer().getCooldowns().addCooldown(this, 10);
      event.getItemStack().shrink(1);
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.SUCCESS);
    }
  }
}
