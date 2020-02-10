package com.lothrazar.cyclic.block.cable.item;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import com.google.common.collect.Maps;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCableItem extends TileEntityBase implements ITickableTileEntity {

  private Map<Direction, IItemHandler> flow = Maps.newHashMap();
  private static final int MAX = 8000;
  private LazyOptional<IItemHandler> item = LazyOptional.of(this::createHandler);

  public TileCableItem() {
    super(BlockRegistry.Tiles.item_pipeTile);
    for (Direction f : Direction.values()) {
      flow.put(f, createHandler());
    }
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public void tick() {
    List<Integer> rawList = IntStream.rangeClosed(
        0,
        5).boxed().collect(Collectors.toList());
    Collections.shuffle(rawList);
    for (Integer i : rawList) {
      Direction exportToSide = Direction.values()[i];
      //      if (this.isIncomingFromFace(exportToSide) == false) {
      //        moveItems(exportToSide, MAX);
      //      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (side == null) {
      //overflow
    }
    else {
      //return inventory for THIS side 
      //  
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return item.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      //      flow.put(f, tag.getInt(f.getName() + "_incitem"));
    }
    CompoundNBT itemTag = tag.getCompound("item");
    item.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(itemTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    for (Direction f : Direction.values()) {
      //      tag.putInt(f.getName() + "_incitem", mapIncoming.get(f));
    }
    item.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("item", compound);
    });
    return super.write(tag);
  }

  private static final int TIMER_SIDE_INPUT = 15;
  //  private boolean isIncomingFromFace(Direction face) {
  //    return flow.get(face) > 0;
  //  }

  public void updateIncomingFace(Direction inputFrom) {
    //    flow.put(inputFrom, TIMER_SIDE_INPUT);
  }

  @Override
  public void setField(int field, int value) {
    // TODO Auto-generated method stub
  }
}
