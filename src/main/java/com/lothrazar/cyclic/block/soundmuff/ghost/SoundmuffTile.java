package com.lothrazar.cyclic.block.soundmuff.ghost;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SoundmuffTile extends TileEntityBase {

  ItemStackHandler notInventory = new ItemStackHandler(1);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> notInventory);

  public SoundmuffTile() {
    super(TileRegistry.soundproofing_ghost);
  }

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    notInventory.deserializeNBT(tag.getCompound(NBTINV));
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, notInventory.serializeNBT());
    return super.save(tag);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
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
