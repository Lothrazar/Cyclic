package com.lothrazar.cyclic.block.cable.item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class TileCableItem extends TileCableBase implements MenuProvider {

  private static final int FLOW_QTY = 64; // fixed, for non-extract motion
  private int extractQty = FLOW_QTY; // default
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }
  };
  private Map<Direction, IItemHandler>flow = new ConcurrentHashMap<>();

  public TileCableItem(BlockPos pos, BlockState state) {
    super(TileRegistry.ITEM_PIPE.get(), pos, state);
    for (Direction f : Direction.values()) {
      flow.put(f, TileCableItem.createHandler());
    }
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileCableItem e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileCableItem e) {
    e.tick();
  }

  private static ItemStackHandler createHandler() {
    return new ItemStackHandler(1);
  }

  public void tick() {
    for (Direction extractSide : Direction.values()) {
      EnumConnectType connection = this.getBlockState().getValue(CableBase.FACING_TO_PROPERTY_MAP.get(extractSide));
      if (connection.isExtraction()) {
        final IItemHandler sideHandler = flow.get(extractSide);
        tryExtract(sideHandler, extractSide, extractQty, filter);
      }
    }
    normalFlow();
  }

  private void normalFlow() {
    // Label for loop for shortcutting, used to continue after items have been moved
    incomingSideLoop: for (final Direction incomingSide : Direction.values()) {
      //in all cases sideHandler is required
      final IItemHandler sideHandler = flow.get(incomingSide);//.orElse(null);
      for (final Direction outgoingSide : UtilDirection.getAllInDifferentOrder()) {
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

//  @Override
// //  // public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//    if (side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
//      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
//        return flow.get(side).cast();
//      }
//    }
//    return super.getCapability(cap, side);
//  }


  @SuppressWarnings("unchecked")
  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    extractQty = tag.getInt("extractCount");
    IItemHandler item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      if(item !=null){ //item.ifPresent(h -> {
        CompoundTag itemTag = tag.getCompound("item" + f.toString());
        ((ItemStackHandler)item).deserializeNBT(registries, itemTag);
      }
    }
    filter.deserializeNBT(registries,tag.getCompound("filter"));
    super.loadAdditional(tag, registries);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put("filter", filter.serializeNBT(registries));
    tag.putInt("extractCount", extractQty);
    IItemHandler item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      if(item !=null){ //item.ifPresent(h -> {
        CompoundTag compound = ((ItemStackHandler)item).serializeNBT(registries);
        tag.put("item" + f.toString(), compound);
      }
    }
    super.saveAdditional(tag, registries);
  }

  @Override
  public void setField(int field, int value) {
    this.extractQty = value;
  }

  @Override
  public int getField(int field) {
    return this.extractQty;
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
