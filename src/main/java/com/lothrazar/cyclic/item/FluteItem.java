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
import com.lothrazar.cyclic.api.IEntityInteractable;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class FluteItem extends ItemBaseCyclic implements IEntityInteractable {

  private static final String UNIQUEMAGIC = "uniquemagic";
  private static final String FLUTENAME = "flutename";
  public static final int CD = 30;

  public FluteItem(Properties prop) {
    super(prop.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    //    playerIn.startUsingItem(handIn);
    if (!player.getCooldowns().isOnCooldown(this) &&
        itemstack.hasTag() && itemstack.getTag().contains(EntityMagicNetEmpty.NBT_ENTITYID)) {
      int id = itemstack.getTag().getInt(UNIQUEMAGIC);
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
    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isFoil(ItemStack stack) {
    return stack.hasTag() && stack.getTag().contains(UNIQUEMAGIC);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    if (stack.hasTag() && stack.getTag().contains(FLUTENAME)) {
      tooltip.add(new TranslatableComponent(stack.getTag().getString(FLUTENAME)).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
  }

  @Override
  public void interactWith(EntityInteract event) {
    Entity target = event.getTarget();
    Player player = event.getPlayer();
    if (event.getItemStack().getItem() == this
        && !player.getCooldowns().isOnCooldown(this)
        && EntityUtil.haveSameDimension(target, player)) {
      String id = EntityType.getKey(target.getType()).toString();
      event.getItemStack().getOrCreateTag().putString(FLUTENAME, target.getDisplayName().getString());
      event.getItemStack().getOrCreateTag().putString(EntityMagicNetEmpty.NBT_ENTITYID, id);
      event.getItemStack().getOrCreateTag().putInt(UNIQUEMAGIC, target.getId());
      player.getCooldowns().addCooldown(this, CD);
      player.swing(event.getHand());
      ChatUtil.addChatMessage(player, "item.cyclic.flute_summoning.saved");
      SoundUtil.playSound(player, SoundRegistry.BASS_ECHO.get(), 0.5F);
      event.setCancellationResult(InteractionResult.SUCCESS);
    }
  }
}
