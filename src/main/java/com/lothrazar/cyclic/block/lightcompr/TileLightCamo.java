package com.lothrazar.cyclic.block.lightcompr;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileLightCamo extends TileEntityBase {

  ItemStackHandler notInventory = new ItemStackHandler(1);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> notInventory);

  public TileLightCamo(BlockPos pos, BlockState state) {
    super(TileRegistry.light_camo, pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    notInventory.deserializeNBT(tag.getCompound(NBTINV));
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, notInventory.serializeNBT());
    return super.save(tag);
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  public List<BlockPos> getShape() {
    List<BlockPos> lis = new ArrayList<BlockPos>();
    lis.add(worldPosition);
    return lis;
  }
}
