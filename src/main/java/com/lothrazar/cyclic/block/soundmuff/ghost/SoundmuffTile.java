package com.lothrazar.cyclic.block.soundmuff.ghost;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class SoundmuffTile extends TileBlockEntityCyclic {

  private CompoundTag facadeState = null;

  ItemStackHandler notInventory = new ItemStackHandler(1);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> notInventory);

  public SoundmuffTile(BlockPos pos, BlockState state) {
    super(TileRegistry.SOUNDPROOFING_GHOST.get(), pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    notInventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, notInventory.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
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

  public BlockState getFacadeState() {
    if (level == null || facadeState == null || facadeState.isEmpty()) {
      return null; // level is null on world load 
    }
    BlockState stateFound = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), facadeState);
    return stateFound;
  }

  public void setFacadeState(CompoundTag facadeState) {
    this.facadeState = facadeState;
  }
}
