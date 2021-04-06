package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

public class TileEnderShelf extends TileEntityBase {

  EnderShelfItemHandler invShelf = new EnderShelfItemHandler(this);
  EnderControllerItemHandler controllerInv = new EnderControllerItemHandler(this);
  private final LazyOptional<EnderShelfItemHandler> inventoryCap = LazyOptional.of(() -> invShelf);
  private final LazyOptional<EnderControllerItemHandler> controllerInventoryCap = LazyOptional.of(() -> controllerInv);
  private BlockPos controllerLocation;
  private List<BlockPos> connectedShelves;

  public TileEnderShelf() {
    super(TileRegistry.ender_shelf);
    this.connectedShelves = new ArrayList<>();
    this.controllerLocation = null;
  }

  @Override
  public void setField(int field, int value) {}

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

  /**
   * Converts to sorted list
   * 
   * @param shelves
   */
  public void setShelves(Set<BlockPos> shelves) {
    this.connectedShelves = shelves.stream().sorted(Comparator.comparing(o -> o.distanceSq(this.pos))).collect(Collectors.toList());
  }

  public List<BlockPos> getShelves() {
    return this.connectedShelves;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    boolean isController = EnderShelfHelper.isController(this.getBlockState());
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      //enable next two lines for dev feature, extract random enchanted books from bottom of Controller
      //if (isController && side == Direction.DOWN)
      //  return fakeInventory.cast();
      if (isController) {
        return controllerInventoryCap.cast();
      }
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventoryCap.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
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
    inventoryCap.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    ListNBT shelves = new ListNBT();
    for (BlockPos pos : this.connectedShelves) {
      shelves.add(NBTUtil.writeBlockPos(pos));
    }
    tag.put("shelves", shelves);
    return super.write(tag);
  }
}
