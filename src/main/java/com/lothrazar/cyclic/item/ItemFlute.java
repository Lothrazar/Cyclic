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
package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class ItemFlute extends ItemBaseCyclic implements IEntityInteractable {

  public ItemFlute(Properties prop) {
    super(prop);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);
    //    playerIn.startUsingItem(handIn);
    if (itemstack.hasTag() && itemstack.getTag().contains(EntityMagicNetEmpty.NBT_ENTITYID)) {
      //
      int id = itemstack.getTag().getInt("uniquemagic");
      Entity found = worldIn.getEntity(id);
      if (found != null) {
        UtilChat.addChatMessage(playerIn, UtilChat.lang("item.cyclic.whistle.saved") + found);
      }
      //
    }
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  public void interactWith(EntityInteract event) {
    Entity target = event.getTarget();
    Player player = event.getPlayer();
    if (event.getItemStack().getItem() == this
        && !player.getCooldowns().isOnCooldown(this)
        && UtilEntity.isTamedByPlayer(target, player)) {
      //      boolean test = event.getTarget() instanceof net.minecraft.world.entity.TamableAnimal;
      String id = EntityType.getKey(target.getType()).toString();
      event.getItemStack().getOrCreateTag().putString(EntityMagicNetEmpty.NBT_ENTITYID, id);
      event.getItemStack().getOrCreateTag().putInt("uniquemagic", target.getId());
      player.getCooldowns().addCooldown(this, 30);
      player.swing(event.getHand());
      UtilChat.addChatMessage(player, "item.cyclic.whistle.saved");
      event.setCancellationResult(InteractionResult.SUCCESS);
    }
  }
}
