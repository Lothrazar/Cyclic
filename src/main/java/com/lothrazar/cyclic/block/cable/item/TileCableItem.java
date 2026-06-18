package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.DirectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileCableItem extends TileCableBase implements MenuProvider {

  public TileCableItem(BlockPos pos, BlockState state) {
    super(TileRegistry.ITEM_PIPE.get(), pos, state);
    setIsItem();
  }


  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableItem e) {
    e.tickItem();
  }

  public void tickItem() {
    for (Direction extractSide : Direction.values()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(extractSide));
      if (connection.isExtraction()) {
        final IItemHandler sideHandler = mapItemFlow.get(extractSide);
        tryExtract(sideHandler, extractSide, FLOW_QTY, itemFilter);
      }
    }
    normalFlow();
  }

  private void normalFlow() {
    // Label for loop for shortcutting, used to continue after items have been moved
    incomingSideLoop: for (final Direction incomingSide : Direction.values()) {
      //in all cases sideHandler is required
      final IItemHandler sideHandler = mapItemFlow.get(incomingSide);//.orElse(null);
      for (final Direction outgoingSide : DirectionUtil.getAllInDifferentOrder()) {
        if (outgoingSide == incomingSide) {
          continue;
        }
        if (!canPushToSide(outgoingSide)) {
          continue;
        }
        if (this.moveItems(outgoingSide, FLOW_QTY, sideHandler)) {
          continue incomingSideLoop; //if items have been moved then change side
        }
      }
      if (canPushToSide(incomingSide)) {
        // allow items to travel backwards if they have nowhere else to go, instead of getting stuck (see #1677)
        this.moveItems(incomingSide, FLOW_QTY, sideHandler);
      }
    }
  }

  private boolean canPushToSide(Direction outgoingSide) {
    EnumConnectType outgoingConnection = getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
    return !outgoingConnection.isExtraction() && !outgoingConnection.isBlocked();
  }


  @Override
  public Component getDisplayName() {
    return BlockRegistry.ITEM_PIPE.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCableItem(i, level, worldPosition, playerInventory, playerEntity);
  }

}
