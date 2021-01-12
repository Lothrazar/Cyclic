package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.base.ItemEntityInteractable;
import com.lothrazar.cyclic.block.cable.CableWrench;
import com.lothrazar.cyclic.block.cable.WrenchActionType;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.item.AntimatterEvaporatorWandItem;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.carrot.ItemHorseEnder;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.heart.HeartItem;
import com.lothrazar.cyclic.item.storagebag.StorageBagItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
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
      AttributeModifier healthModifier = original.getModifier(HeartItem.ID);
      if (healthModifier != null)
        event.getPlayer().getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(healthModifier);
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    LivingEntity liv = event.getEntityLiving();
    if (//!liv.world.isRemote &&
    liv.getPersistentData().contains(ItemHorseEnder.NBT_KEYACTIVE)
        && liv.getPersistentData().getInt(ItemHorseEnder.NBT_KEYACTIVE) > 0) {
      // 
      if (liv.isInWater()
          && liv.canBreatheUnderwater() == false
          && liv.getAir() < liv.getMaxAir()
          && !liv.isPotionActive(Effects.WATER_BREATHING)) {
        liv.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 20 * 60, 4));
        liv.addPotionEffect(new EffectInstance(PotionRegistry.PotionEffects.swimspeed, 20 * 60, 1));
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.isBurning()
          && !liv.isPotionActive(Effects.FIRE_RESISTANCE)) {
        liv.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 20 * 60, 4));
        liv.extinguish();
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.fallDistance > 12
          && !liv.isPotionActive(Effects.SLOW_FALLING)) {
        liv.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 4));
        //        if (liv.getPassengers().size() > 0) {
        //          liv.getPassengers().get(0).addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 20 * 60, 1));
        //        }
        ItemHorseEnder.onSuccess(liv);
      }
      if (liv.getHealth() < 6
          && !liv.isPotionActive(Effects.ABSORPTION)) {
        liv.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 20 * 60, 4));
        liv.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 20 * 60, 4));
        ItemHorseEnder.onSuccess(liv);
      }
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
      if (world.rand.nextDouble() < 0.5)//TODO: config
        UtilItemStack.drop(world, pos, new ItemStack(BlockRegistry.flower_cyan));
    }
  }

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) event.getEntity();
      if (p.getPersistentData().getBoolean("cyclic_sleeping")) { // TODO: const in sleeping mat
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
    ///////////// shape
    if (held.getItem() instanceof ShapeCard && player.isCrouching()) {
      BlockState target = world.getBlockState(event.getPos());
      ShapeCard.setBlockState(held, target);
      UtilChat.sendStatusMessage(player, target.getBlock().getTranslationKey());
    }
    ///////////////// builders
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
    ////////////////////////// wrench
    if (held.getItem() instanceof CableWrench && WrenchActionType.getTimeout(held) == 0) {
      //mode 
      if (!world.isRemote) {
        WrenchActionType.toggle(held);
      }
      UtilSound.playSound(player, SoundRegistry.tool_mode);
      WrenchActionType.setTimeout(held);
      UtilChat.sendStatusMessage(player, UtilChat.lang(WrenchActionType.getName(held)));
    }
    if (held.getItem() instanceof AntimatterEvaporatorWandItem) {
      AntimatterEvaporatorWandItem.toggleMode(player, held);
    }
  }

  @SubscribeEvent
  public void onPlayerPickup(EntityItemPickupEvent event) {
    if (event.getEntityLiving() instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      ItemStack stack = event.getItem().getItem();
      ItemStack resultStack = null;
      Set<Integer> bagSlots = StorageBagItem.getAllBagSlots(player);
      for (Integer i : bagSlots) {
        ItemStack bag = player.inventory.getStackInSlot(i);
        switch (StorageBagItem.getPickupMode(bag)) {
          case EVERYTHING:
            resultStack = StorageBagItem.tryInsert(bag, stack);
          break;
          case FILTER:
            resultStack = StorageBagItem.tryFilteredInsert(bag, stack);
          break;
          case NOTHING:
          break;
        }
      }
      if (resultStack != null)
        event.getItem().setItem(resultStack);
      if (resultStack != null && resultStack.getCount() != stack.getCount())
        event.setResult(Result.ALLOW);
      else
        event.setResult(Result.DEFAULT);
    }
  }
}
