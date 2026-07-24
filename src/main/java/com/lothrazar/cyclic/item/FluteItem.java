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

import java.util.List;
import com.lothrazar.library.core.IEntityInteractable;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;


public class FluteItem extends ItemBaseCyclic implements IEntityInteractable {

  private static final String UNIQUEMAGIC = "uniquemagic";
  private static final String FLUTENAME = "flutename";
  public static final int CD = 30;

  public FluteItem(Properties prop) {
    super(prop.stacksTo(1));
  }

  @Override
  public InteractionResult use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    //    playerIn.startUsingItem(handIn);
    if (!player.getCooldowns().isOnCooldown(this) &&
        itemstack.has(DataComponents.CUSTOM_DATA) && itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(EntityMagicNetEmpty.NBT_ENTITYID)) {
      int id = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(UNIQUEMAGIC, 0);
      Entity found = worldIn.getEntity(id);
      if (found instanceof LivingEntity living) {
        boolean success = EntityUtil.enderTeleportEvent(living, worldIn, player.blockPosition());
        if (success) {
          ChatUtil.addChatMessage(player, ChatUtil.lang("item.cyclic.flute_summoning.teleported"));
          player.getCooldowns().addCooldown(this, CD);
          SoundUtil.playSound(player, SoundRegistry.HOVERING.get(), 0.5F);
        }
      }
    }
    return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack) {
    return stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(UNIQUEMAGIC);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    if (stack.has(DataComponents.CUSTOM_DATA) && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(FLUTENAME)) {
      tooltip.add(Component.translatable(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(FLUTENAME)).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
  }

  @Override
  public void interactWith(PlayerInteractEvent.EntityInteract event) {
    Entity target = event.getTarget();
    Player player = event.getEntity();
    if (event.getItemStack().getItem() == this
        && !player.getCooldowns().isOnCooldown(this)
        && EntityUtil.haveSameDimension(target, player)) {
      String id = EntityType.getKey(target.getType()).toString();
      CustomData.update(DataComponents.CUSTOM_DATA, event.getItemStack(), t -> t.putString(FLUTENAME, target.getDisplayName().getString()));
      CustomData.update(DataComponents.CUSTOM_DATA, event.getItemStack(), t -> t.putString(EntityMagicNetEmpty.NBT_ENTITYID, id));
      CustomData.update(DataComponents.CUSTOM_DATA, event.getItemStack(), t -> t.putInt(UNIQUEMAGIC, target.getId()));
      player.getCooldowns().addCooldown(this, CD);
      player.swing(event.getHand());
      ChatUtil.addChatMessage(player, "item.cyclic.flute_summoning.saved");
      SoundUtil.playSound(player, SoundRegistry.BASS_ECHO.get(), 0.5F);
      event.setCancellationResult(InteractionResult.SUCCESS);
    }
  }
}
