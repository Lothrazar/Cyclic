package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class TileEnderShelf extends TileEntityBase {

  private final LazyOptional<EnderShelfItemHandler> inventory = LazyOptional.of(() -> new EnderShelfItemHandler(this));
  private final LazyOptional<EnderShelfItemHandler> fakeInventory = LazyOptional.of(() -> new EnderShelfItemHandler(this, true));
  private final LazyOptional<EnderControllerItemHandler> controllerInventory = LazyOptional.of(() -> new EnderControllerItemHandler(this));
  private BlockPos controllerLocation;
  private Set<BlockPos> connectedShelves;

  public TileEnderShelf() {
    super(TileRegistry.ender_shelf);
    this.connectedShelves = new HashSet<>();
    this.controllerLocation = null;
  }

  @Override
  public void setField(int field, int value) {

  }

  @Override
  public int getField(int field) {
    return 0;
  }

  public void setControllerLocation(BlockPos pos) {
    this.controllerLocation = pos;
  }

  public BlockPos getControllerLocation() {
    return this.controllerLocation;
  }

  public void setShelves(Set<BlockPos> shelves) {
    this.connectedShelves = shelves;
    this.connectedShelves = this.connectedShelves.stream().sorted(Comparator.comparing(o -> o.distanceSq(this.pos))).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Set<BlockPos> getShelves() {
    return this.connectedShelves;
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    boolean isController = EnderShelfHelper.isController(this.getBlockState());
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (isController && side == Direction.DOWN)
        return fakeInventory.cast();
      if (isController)
        return controllerInventory.cast();
      return inventory.cast();
    }

    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    if (tag.contains("shelves")) {
      ListNBT shelves = tag.getList("shelves", Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < shelves.size(); i++) {
        BlockPos pos = NBTUtil.readBlockPos(shelves.getCompound(i));
        this.connectedShelves.add(pos);
      }
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    ListNBT shelves = new ListNBT();
    int index = 0;
    for (BlockPos pos : this.connectedShelves) {
      shelves.add(NBTUtil.writeBlockPos(pos));
      index++;
    }
    tag.put("shelves", shelves);
    return super.write(tag);
  }
}
