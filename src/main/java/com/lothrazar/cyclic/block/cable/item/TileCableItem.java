package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
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
  private final IItemHandler itemHandler = new ItemStackHandler(1) {
    @Override
    public boolean isItemValid(final int slot, final ItemStack stack) {
      return FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), stack);
    }
  };
  private final Map<Direction, EnumConnectType> connectTypeMap = new HashMap<>();
  private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> itemHandler);
  private final Map<Direction, LazyOptional<IItemHandler>> itemCapSides = new HashMap<>();

  public TileCableItem() {
    super(TileRegistry.item_pipeTile);
  }

  private static ItemStackHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public EnumConnectType getConnectionType(final Direction side) {
    return connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
  }

  @Override
  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    final EnumConnectType oldConnectType = getConnectionType(side);
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      final LazyOptional<IItemHandler> sidedCap = itemCapSides.get(side);
      if (sidedCap != null) {
        sidedCap.invalidate();
      }
    }
    else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      itemCapSides.put(side, LazyOptional.of(() -> itemHandler));
    }
    connectTypeMap.put(side, connectType);
  }

  @Override
  public void tick() {
    for (final Direction extractSide : Direction.values()) {
      final EnumProperty<EnumConnectType> extractFace = CableBase.FACING_TO_PROPERTY_MAP.get(extractSide);
      final EnumConnectType connection = this.getBlockState().get(extractFace);
      if (connection.isExtraction()) {
        tryExtract(itemHandler, extractSide, extractQty, filter);
      }
    }
    normalFlow();
  }

  private void normalFlow() {
    // Label for loop for shortcutting, used to continue after items have been moved
    incomingSideLoop: for (final Direction incomingSide : Direction.values()) {
      //in all cases sideHandler is required
      for (final Direction outgoingSide : UtilDirection.getAllInDifferentOrder()) {
        if (outgoingSide == incomingSide) {
          continue;
        }
        if (getConnectionType(outgoingSide) == EnumConnectType.BLOCKED) {
          continue;
        }
        final EnumProperty<EnumConnectType> outgoingFace = CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide);
        final EnumConnectType outgoingConnection = this.getBlockState().get(outgoingFace);
        if (outgoingConnection.isExtraction() || outgoingConnection.isBlocked()) {
          continue;
        }
        if (this.moveItems(outgoingSide, FLOW_QTY, itemHandler)) {
          continue incomingSideLoop; //if items have been moved then change side
        }
      }
      //if no items have been moved then move items in from adjacent
      this.moveItems(incomingSide, FLOW_QTY, itemHandler);
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (side == null) {
        return itemCap.cast();
      }
      LazyOptional<IItemHandler> sidedCap = itemCapSides.get(side);
      if (sidedCap == null) {
        if (getConnectionType(side) != EnumConnectType.BLOCKED) {
          sidedCap = LazyOptional.of(() -> itemHandler);
          itemCapSides.put(side, sidedCap);
          return sidedCap.cast();
        }
      }
      else {
        return sidedCap.cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    itemCap.invalidate();
    for (final LazyOptional<IItemHandler> sidedCap : itemCapSides.values()) {
      sidedCap.invalidate();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    extractQty = tag.getInt("extractCount");
    ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(tag.getCompound(NBTINV));
    filter.deserializeNBT(tag.getCompound("filter"));
    super.read(bs, tag);
  }

  @SuppressWarnings("unchecked")
  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("extractCount", extractQty);
    tag.put(NBTINV, ((INBTSerializable<CompoundNBT>) itemHandler).serializeNBT());
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
