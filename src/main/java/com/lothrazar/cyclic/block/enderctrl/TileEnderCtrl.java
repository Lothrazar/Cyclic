package com.lothrazar.cyclic.block.enderctrl;

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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEnderCtrl extends TileEntityBase {

  private static final String NBT_SHELVES = "shelves";
  EnderControllerItemHandler controllerInv = new EnderControllerItemHandler(this);
  private final LazyOptional<EnderControllerItemHandler> controllerInventoryCap = LazyOptional.of(() -> controllerInv);
  private List<BlockPos> connectedShelves;

  public TileEnderCtrl() {
    super(TileRegistry.ender_controller);
    this.connectedShelves = new ArrayList<>();
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
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
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return controllerInventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    if (tag.contains(NBT_SHELVES)) {
      ListNBT shelves = tag.getList(NBT_SHELVES, Constants.NBT.TAG_COMPOUND);
      for (int i = 0; i < shelves.size(); i++) {
        BlockPos pos = NBTUtil.readBlockPos(shelves.getCompound(i));
        this.connectedShelves.add(pos);
      }
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    ListNBT shelves = new ListNBT();
    for (BlockPos pos : this.connectedShelves) {
      shelves.add(NBTUtil.writeBlockPos(pos));
    }
    tag.put(NBT_SHELVES, shelves);
    return super.write(tag);
  }
}
