package com.lothrazar.cyclic.block.cable.item;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilDirection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableItem extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final int FLOW_QTY = 64; // fixed, for non-extract motion
  private int extractQty = FLOW_QTY;
  private static final int TIMER_SIDE_INPUT = 15;
  public final ItemStackHandler filter = new ItemStackHandler(1) {
    @Override
    public boolean isItemValid(final int slot, final ItemStack stack) {
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
  private final Map<Direction, IItemHandler> itemCache = new HashMap<>();
  private final Map<Direction, TileEntityBase> adjacentTileEntityBases = new HashMap<>();
  private final Map<Direction, Integer> receivedFrom = new HashMap<>();

  public TileCableItem() {
    super(TileRegistry.item_pipeTile);
  }

  @Override
  public IItemHandler getItemHandler() {
    return itemHandler;
  }

  @Override
  protected IItemHandler getAdjacentItemHandler(final Direction side) {
    return itemCache.computeIfAbsent(side, k -> {
      adjacentTileEntityBases.remove(k);
      if (world == null) {
        return null;
      }
      final TileEntity tileEntity = world.getTileEntity(pos.offset(k));
      if (tileEntity == null) {
        return null;
      }
      else if (tileEntity instanceof TileEntityBase) {
        adjacentTileEntityBases.put(k, (TileEntityBase) tileEntity);
      }
      final LazyOptional<IItemHandler> lazyOptional = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, k.getOpposite());
      final IItemHandler handler = lazyOptional.resolve().orElse(null);
      if (handler != null) {
        lazyOptional.addListener((o) -> {
          adjacentTileEntityBases.remove(k);
          itemCache.remove(k);
          receivedFrom.remove(k);
        });
      }
      return handler;
    });
  }

  private EnumConnectType getConnectionType(final Direction side) {
    return getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(side));
    //return connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
  }

  @Override
  public void setReceivedFrom(final Direction side) {
    receivedFrom.put(side, TIMER_SIDE_INPUT);
  }

  @Override
  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    final EnumConnectType oldConnectType = getConnectionType(side);
    if (connectType == EnumConnectType.BLOCKED && oldConnectType != EnumConnectType.BLOCKED) {
      final LazyOptional<IItemHandler> sidedCap = itemCapSides.get(side);
      if (sidedCap != null) {
        sidedCap.invalidate();
        itemCache.remove(side);
      }
    }
    else if (oldConnectType == EnumConnectType.BLOCKED && connectType != EnumConnectType.BLOCKED) {
      itemCapSides.put(side, LazyOptional.of(() -> itemHandler));
    }
    connectTypeMap.put(side, connectType);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    for (final Iterator<Map.Entry<Direction, Integer>> it = receivedFrom.entrySet().iterator(); it.hasNext(); ) {
      final Map.Entry<Direction, Integer> entry = it.next();
      entry.setValue(entry.getValue() - 1);
      if (entry.getValue() <= 0) {
        it.remove();
      }
    }
    int remainingAmount = FLOW_QTY;
    for (final Direction side : UtilDirection.getAllInDifferentOrder()) {
      final EnumConnectType connectType = getConnectionType(side);
      if (connectType == EnumConnectType.CABLE) {
        remainingAmount -= getItemsFromAdjacent(itemHandler, side, Math.min(extractQty, remainingAmount));
      }
      else if (connectType == EnumConnectType.INVENTORY && !receivedFrom.containsKey(side)) {
        final int moved = moveItemsToAdjacent(itemHandler, side, remainingAmount);
        if (moved <= 0) {
          continue;
        }
        remainingAmount -= moved;
        final TileEntityBase adjacentTileEntityBase = adjacentTileEntityBases.get(side);
        if (adjacentTileEntityBase != null) {
          adjacentTileEntityBase.setReceivedFrom(side.getOpposite());
        }
      }
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
  public void read(final BlockState bs, final CompoundNBT tag) {
    filter.deserializeNBT(tag.getCompound(NBTFILTER));
    ((INBTSerializable<CompoundNBT>) itemHandler).deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @SuppressWarnings("unchecked")
  @Override
  public CompoundNBT write(final CompoundNBT tag) {
    tag.put(NBTFILTER, filter.serializeNBT());
    tag.put(NBTINV, ((INBTSerializable<CompoundNBT>) itemHandler).serializeNBT());
    return super.write(tag);
  }

  @Override
  public void setField(final int field, final int value) {
    this.extractQty = value;
  }

  @Override
  public int getField(int field) {
    return extractQty;
  }

  @Override
  public ITextComponent getDisplayName() {
    final ResourceLocation resourceLocation = getType().getRegistryName();
    return new StringTextComponent((resourceLocation == null) ? "" : resourceLocation.getPath());
  }

  @Override
  public Container createMenu(final int i, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
    if (world == null) {
      return null;
    }
    return new ContainerCableItem(i, world, pos, playerInventory, playerEntity);
  }
}
