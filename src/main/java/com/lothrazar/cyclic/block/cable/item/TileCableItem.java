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
import com.lothrazar.cyclic.block.cable.EnumConnectType;
import com.lothrazar.cyclic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
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
  //  private LazyOptional<IItemHandler> item = LazyOptional.of(this::createHandler);

  public TileCableItem() {
    super(BlockRegistry.Tiles.item_pipeTile);
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
          itemTarget = itemHandlerFrom.getStackInSlot(i);
          if (itemTarget.isEmpty()) {
            continue;
          }
          // and then pull 
          if (itemTarget.isEmpty() == false) {
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
    for (Direction incomingSide : Direction.values()) {
      sideHandler = flow.get(incomingSide).orElse(null);
      //thise items came from that
      Collections.shuffle(rawList);
      for (Integer i : rawList) {
        outgoingSide = Direction.values()[i];
        if (outgoingSide == incomingSide) {
          continue;
        }
        this.moveItems(outgoingSide, 64, sideHandler);
      }
    }
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //but first am i blocked
      EnumProperty<EnumConnectType> property = CableBase.FACING_TO_PROPERTY_MAP.get(side);
      if (this.getBlockState().get(property) != EnumConnectType.BLOCKED) {
        return flow.get(side).cast();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    LazyOptional<IItemHandler> item;
    for (Direction f : Direction.values()) {
      item = flow.get(f);
      item.ifPresent(h -> {
        CompoundNBT itemTag = tag.getCompound("item" + f.toString());
        ((INBTSerializable<CompoundNBT>) h).deserializeNBT(itemTag);
      });
    }
    super.read(tag);
  }

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
}
