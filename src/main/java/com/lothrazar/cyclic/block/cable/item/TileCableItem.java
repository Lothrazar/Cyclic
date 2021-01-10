package com.lothrazar.cyclic.block.cable.item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.cable.CableBase;
import com.lothrazar.cyclic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableItem extends TileEntityBase implements ITickableTileEntity {

  private Map<Direction, LazyOptional<IItemHandler>> flow = Maps.newHashMap();

  public TileCableItem() {
    super(TileRegistry.item_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, LazyOptional.of(this::createHandler));
    }
  }

  private IItemHandler createHandler() {
    ItemStackHandler h = new ItemStackHandler(1);
    return h;
  }

  List<Integer> rawList = IntStream.rangeClosed(
      0,
      5).boxed().collect(Collectors.toList());

  @Override
  public void tick() {
    tryExtract();
    normalFlow();
  }

  private void tryExtract() {
    Direction importFromSide = this.getBlockState().get(BlockCableFluid.EXTR).direction();
    if (importFromSide == null) {
      return;
    }
    IItemHandler sideHandler = flow.get(importFromSide).orElse(null);
    if (importFromSide == null || !sideHandler.getStackInSlot(0).isEmpty()) {
      return;
    }
    BlockPos posTarget = this.pos.offset(importFromSide);
    TileEntity tile = world.getTileEntity(posTarget);
    if (tile != null) {
      IItemHandler itemHandlerFrom = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, importFromSide.getOpposite()).orElse(null);
      //
      ItemStack itemTarget;
      if (itemHandlerFrom != null) {
        //ok go
        for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
          itemTarget = itemHandlerFrom.extractItem(i, 64, true);
          if (itemTarget.isEmpty()) {
            continue;
          }
          // and then pull 
          if (itemTarget.isEmpty() == false) {
            itemTarget = itemHandlerFrom.extractItem(i, 64, false);
            ItemStack result = sideHandler.insertItem(0, itemTarget.copy(), false);
            itemTarget.setCount(result.getCount());
            //            this.setInventorySlotContents(importFromSide.ordinal(), pulled.copy());
            return;
          }
        }
      }
    }
  }

  private void normalFlow() {
    IItemHandler sideHandler;
    Direction outgoingSide;
    Direction importFromSide = this.getBlockState().get(BlockCableFluid.EXTR).direction();
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
        if (importFromSide != null && importFromSide == outgoingSide) {
          continue;
        }
        validAdjacent = validAdjacent || this.moveItems(outgoingSide, 64, sideHandler);
      }
      if (!validAdjacent) {
        this.moveItems(incomingSide, 64, sideHandler);
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
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
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
