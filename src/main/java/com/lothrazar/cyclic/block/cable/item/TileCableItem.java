package com.lothrazar.cyclic.block.cable.item;

import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import net.minecraft.util.math.BlockPos;
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
  private int extractQty = 64; // default
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  private Map<Direction, LazyOptional<IItemHandler>> flow = Maps.newHashMap();

  public TileCableItem() {
    super(TileRegistry.item_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(TileCableItem::createHandler));
    }
  }

  private static ItemStackHandler createHandler() {
    return new ItemStackHandler(1);
  }

  List<Integer> rawList = IntStream.rangeClosed(
      0,
      5).boxed().collect(Collectors.toList());

  @Override
  public void tick() {
    for (Direction side : Direction.values()) {
      EnumConnectType connection = this.getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(side));
      if (connection.isExtraction()) {
        tryExtract(side);
      }
    }
    normalFlow();
  }

  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    IItemHandler sideHandler = flow.get(extractSide).orElse(null);
    if (extractSide == null || !sideHandler.getStackInSlot(0).isEmpty()) {
      return;
    }
    BlockPos posTarget = this.pos.offset(extractSide);
    TileEntity tile = world.getTileEntity(posTarget);
    if (tile != null) {
      IItemHandler itemHandlerFrom = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, extractSide.getOpposite()).orElse(null);
      //
      ItemStack itemTarget;
      if (itemHandlerFrom != null) {
        //ok go
        for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
          itemTarget = itemHandlerFrom.extractItem(i, extractQty, true);
          if (itemTarget.isEmpty()) {
            continue;
          }
          // and then pull 
          if (!FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), itemTarget)) {
            continue;
          }
          itemTarget = itemHandlerFrom.extractItem(i, extractQty, false);
          ItemStack result = sideHandler.insertItem(0, itemTarget.copy(), false);
          itemTarget.setCount(result.getCount());
          return;
        }
      }
    }
  }

  private void normalFlow() {
    IItemHandler sideHandler;
    Direction outgoingSide;
    for (Direction incomingSide : Direction.values()) {
      sideHandler = flow.get(incomingSide).orElse(null);
      //thise items came from that
      Collections.shuffle(rawList);
      boolean validAdjacent = false;
      for (Integer i : rawList) {
        outgoingSide = Direction.values()[i];
        if (outgoingSide == incomingSide) {
          continue;
        }
        EnumConnectType connection = this.getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(outgoingSide));
        if (connection.isExtraction()) {
          continue;
        }
        validAdjacent = validAdjacent || this.moveItems(outgoingSide, 64, sideHandler);
      }
      if (!validAdjacent) {
        this.moveItems(incomingSide, FLOW_QTY, sideHandler);
      }
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

  @SuppressWarnings("unchecked")
  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    extractQty = tag.getInt("extractCount");
    LazyOptional<IItemHandler> item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      item.ifPresent(h -> {
        CompoundNBT itemTag = tag.getCompound("item" + f.toString());
        ((INBTSerializable<CompoundNBT>) h).deserializeNBT(itemTag);
      });
    }
    filter.deserializeNBT(tag.getCompound("filter"));
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
