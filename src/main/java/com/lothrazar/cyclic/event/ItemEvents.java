package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.CableWrench;
import com.lothrazar.cyclic.block.cable.DirectionNullable;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.WrenchActionType;
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
import net.minecraft.state.EnumProperty;
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
      WrenchActionType type = WrenchActionType.getType(event.getItemStack());
      if (type == WrenchActionType.EXTRACT) {
        if (state.getBlock() == BlockRegistry.fluid_pipe
            || state.getBlock() == BlockRegistry.item_pipe) {
          //TODO: cableBase class once all 3 support extracty
          DirectionNullable current = state.get(BlockCableFluid.EXTR);
          Direction targetFace = event.getFace();
          if (event.getPlayer().isCrouching()) {
            targetFace = targetFace.getOpposite();
          }
          DirectionNullable newextr = current.toggle(targetFace);
          world.setBlockState(pos, state.with(BlockCableFluid.EXTR, newextr));
        }
      }
      else if (type == WrenchActionType.BLOCK && state.getBlock() instanceof CableBase) {
        //
        EnumProperty<EnumConnectType> prop = CableBase.FACING_TO_PROPERTY_MAP.get(event.getFace());
        EnumConnectType status = state.get(prop);
        BlockState stateNone;
        switch (status) {
          case BLOCKED:
            //unblock it
            //then updatePostPlacement
            stateNone = state.with(prop, EnumConnectType.NONE);
            world.setBlockState(pos, stateNone);
          //            CableBase cable = (CableBase) state.getBlock();
          //            cable.updatePostPlacement(stateNone, facing, facingState, worldIn, currentPos, facingPos)
          break;
          case CABLE:
          case INVENTORY:
          case NONE:
            world.setBlockState(pos, state.with(prop, EnumConnectType.BLOCKED));
          break;
        }
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
    if (held.getItem() instanceof CableWrench) {
      //mode 
      if (!world.isRemote && WrenchActionType.getTimeout(held) == 0) {
        WrenchActionType.toggle(held);
      }
      WrenchActionType.setTimeout(held);
      UtilChat.sendStatusMessage(player, UtilChat.lang(WrenchActionType.getName(held)));
    }
  }
}
