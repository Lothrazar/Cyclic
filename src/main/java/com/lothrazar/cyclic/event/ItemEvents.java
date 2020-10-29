package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.base.ItemEntityInteractable;
import com.lothrazar.cyclic.block.cable.CableWrench;
import com.lothrazar.cyclic.block.cable.WrenchActionType;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.heart.HeartItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {

  @SubscribeEvent
  public void onPlayerCloneDeath(PlayerEvent.Clone event) {
    ModifiableAttributeInstance original = event.getOriginal().getAttribute(Attributes.MAX_HEALTH);
    if (original != null) {
      AttributeModifier healthModifier = original.getModifier(HeartItem.healthModifierUuid);
      if (healthModifier != null)
        event.getPlayer().getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(healthModifier);
    }
  }
  //
  //  @SubscribeEvent
  //  public void onLivingDeathEvent(LivingDeathEvent event) {
  //    //
  //  }

  @SubscribeEvent
  public void onBonemealEvent(BonemealEvent event) {
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    if (world.getBlockState(pos).getBlock() == Blocks.PODZOL
        && world.isAirBlock(pos.up())) {
      world.setBlockState(pos.up(), BlockRegistry.flower_cyan.getDefaultState());
      event.setResult(Result.ALLOW);
    }
    else if (world.getBlockState(pos).getBlock() == BlockRegistry.flower_cyan) {
      event.setResult(Result.ALLOW);
      if (world.rand.nextDouble() < 0.5)
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.flower_cyan));
    }
  }

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) event.getEntity();
      if (p.getPersistentData().getBoolean("cyclic_sleeping")) {
        event.setResult(Result.ALLOW);
      }
    }
  }

  @SubscribeEvent
  public void onRightClickBlock(RightClickBlock event) {
    if (event.getItemStack().isEmpty()) {
      return;
    }
    if (event.getItemStack().getItem() instanceof ItemScaffolding
        && event.getPlayer().isCrouching()) {
      scaffoldHit(event);
    }
  }

  private void scaffoldHit(RightClickBlock event) {
    ItemScaffolding item = (ItemScaffolding) event.getItemStack().getItem();
    Direction opp = event.getFace().getOpposite();
    BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, item.getBlock());
    if (event.getWorld().isAirBlock(dest)) {
      event.getWorld().setBlockState(dest, item.getBlock().getDefaultState());
      ItemStack stac = event.getPlayer().getHeldItem(event.getHand());
      UtilItemStack.shrink(event.getPlayer(), stac);
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getItemStack().getItem() instanceof ItemEntityInteractable) {
      ItemEntityInteractable item = (ItemEntityInteractable) event.getItemStack().getItem();
      item.interactWith(event);
    }
  }

  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    PlayerEntity player = event.getPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held.isEmpty()) {
      return;
    }
    World world = player.getEntityWorld();
    if (held.getItem() instanceof BuilderItem) {
      if (BuilderActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      BuilderActionType.setTimeout(held);
      event.setCanceled(true);
      if (player.isCrouching()) {
        //pick out target block
        BlockState target = world.getBlockState(event.getPos());
        BuilderActionType.setBlockState(held, target);
        UtilChat.sendStatusMessage(player, target.getBlock().getTranslationKey());
      }
      else {
        //change size
        if (!world.isRemote) {
          BuilderActionType.toggle(held);
        }
        UtilSound.playSound(player, SoundRegistry.tool_mode);
        UtilChat.sendStatusMessage(player, UtilChat.lang(BuilderActionType.getName(held)));
      }
    }
    if (held.getItem() instanceof CableWrench && WrenchActionType.getTimeout(held) == 0) {
      //mode 
      if (!world.isRemote) {
        WrenchActionType.toggle(held);
      }
      UtilSound.playSound(player, SoundRegistry.tool_mode);
      WrenchActionType.setTimeout(held);
      UtilChat.sendStatusMessage(player, UtilChat.lang(WrenchActionType.getName(held)));
    }
  }
}
