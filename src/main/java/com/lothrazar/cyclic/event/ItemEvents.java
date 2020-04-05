package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.block.cable.DirectionNullable;
import com.lothrazar.cyclic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.item.ItemEntityInteractable;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEvents {

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent event) {
    if (event.getEntity() instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) event.getEntity();
      //    final IPlayerExtendedProperties sleep = p.getCapability(ModCyclic.CAPABILITYSTORAGE, null);
      if (p.getPersistentData().getBoolean("cyclic_sleeping")) {
        //          p.bedLocation = p.getPosition();
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
    if (event.getItemStack().getItem() == ItemRegistry.cable_wrench && event.getFace() != null) {
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      BlockState state = world.getBlockState(pos);
      if (state.getBlock() == BlockRegistry.fluid_pipe
          || state.getBlock() == BlockRegistry.item_pipe) {
        //TODO: cableBase class once all 3 support extracty
        DirectionNullable current = world.getBlockState(pos).get(BlockCableFluid.EXTR);
        Direction targetFace = event.getFace();
        if (event.getPlayer().isCrouching()) {
          targetFace = targetFace.getOpposite();
        }
        DirectionNullable newextr = current.toggle(targetFace);
        world.setBlockState(pos, state.with(BlockCableFluid.EXTR, newextr));
      }
    }
  }

  private void scaffoldHit(RightClickBlock event) {
    ItemScaffolding item = (ItemScaffolding) event.getItemStack().getItem();
    Direction opp = event.getFace().getOpposite();
    BlockPos dest = UtilWorld.nextReplaceableInDirection(event.getWorld(), event.getPos(), opp, 16, item.getBlock());
    event.getWorld().setBlockState(dest, item.getBlock().getDefaultState());
    ItemStack stac = event.getPlayer().getHeldItem(event.getHand());
    stac.shrink(1);
    event.setCanceled(true);
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
    if (!held.isEmpty() && held.getItem() instanceof BuilderItem) {
      //      if (event.getFace() != null && player.isSneaking()) {
      //        //hita block
      //        IBlockState hit = player.world.getBlockState(event.getPos());
      //        ModCyclic.logger.log("HIT" + hit.getBlock());
      //        return;
      //      }
      if (BuilderActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      World world = player.getEntityWorld();
      BuilderActionType.setTimeout(held);
      event.setCanceled(true);
      //      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode, SoundCategory.PLAYERS);
      if (player.isCrouching()) {
        //pick out target block
        BlockState target = world.getBlockState(event.getPos());
        BuilderActionType.setBlockState(held, target);
        UtilChat.sendStatusMessage(player, target.getBlock().getNameTextComponent());
      }
      else {
        //change size
        if (!world.isRemote) {
          BuilderActionType.toggle(held);
        }
        UtilChat.sendStatusMessage(player, UtilChat.lang(BuilderActionType.getName(held)));
      }
    }
  }
}
