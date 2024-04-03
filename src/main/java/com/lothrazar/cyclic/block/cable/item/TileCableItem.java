package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableItem extends TileCableBase implements ITickableTileEntity, INamedContainerProvider {

  private static final int FLOW_QTY = 64; // fixed, for non-extract motion
  private int extractQty = FLOW_QTY; // default
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  private final ConcurrentHashMap<Direction, LazyOptional<IItemHandler>> flow = new ConcurrentHashMap<>();

  public TileCableItem() {
    super(TileRegistry.item_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(TileCableItem::createHandler));
    }
  }

  private static ItemStackHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    final EnumConnectType oldConnectType = getConnectionType(side);
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      final LazyOptional<IItemHandler> sidedCap = flow.get(side);
      if (sidedCap != null) { //redundant safety?
        sidedCap.invalidate();
      }
    }
    else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      flow.put(side, LazyOptional.of(TileCableItem::createHandler));
    }
    super.updateConnection(side, connectType);
  }

  @Override
  public void tick() {
    if (world.isRemote) {
      return;
    }
    for (final Direction extractSide : Direction.values()) {
      final EnumConnectType connection = getConnectionType(extractSide);
      if (connection.isExtraction()) {
        final IItemHandler sideHandler = flow.get(extractSide).orElse(null);
        tryExtract(sideHandler, extractSide, extractQty, filter);
      }
    }
    normalFlow();
  }

  private void normalFlow() {
    // Label for loop for shortcutting, used to continue after items have been moved
    incomingSideLoop: for (final Direction incomingSide : Direction.values()) {
      //in all cases sideHandler is required
      final IItemHandler sideHandler = flow.get(incomingSide).orElse(null);
      for (final Direction outgoingSide : UtilDirection.getAllInDifferentOrder()) {
        if (outgoingSide == incomingSide) {
          continue;
        }
        final EnumConnectType outgoingConnection = getConnectionType(outgoingSide);
        if (outgoingConnection.isExtraction() || outgoingConnection.isBlocked()) {
          continue;
        }
        if (this.moveItems(outgoingSide, FLOW_QTY, sideHandler)) {
          continue incomingSideLoop; //if items have been moved then change side
        }
      }
      //if no items have been moved then move items in from adjacent
      this.moveItems(incomingSide, FLOW_QTY, sideHandler);
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (!CableBase.isCableBlocked(this.getBlockState(), side)) {
        return flow.get(side).cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    for (final LazyOptional<IItemHandler> sidedCap : flow.values()) {
      sidedCap.invalidate();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    extractQty = tag.getInt("extractCount");
    filter.deserializeNBT(tag.getCompound("filter"));
    LazyOptional<IItemHandler> item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      item.ifPresent(h -> {
        CompoundNBT itemTag = tag.getCompound("item" + f.toString());
        ((INBTSerializable<CompoundNBT>) h).deserializeNBT(itemTag);
      });
    }
    super.read(bs, tag);
  }

  @SuppressWarnings("unchecked")
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("extractCount", extractQty);
    LazyOptional<IItemHandler> item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      item.ifPresent(h -> {
        CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
        tag.put("item" + f.toString(), compound);
      });
    }
    return super.write(tag);
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
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerCableItem(i, world, pos, playerInventory, playerEntity);
  }
}
