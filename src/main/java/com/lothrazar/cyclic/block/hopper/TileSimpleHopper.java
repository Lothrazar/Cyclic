package com.lothrazar.cyclic.block.hopper;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.hopperfluid.BlockFluidHopper;
import com.lothrazar.cyclic.block.hoppergold.TileGoldHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSimpleHopper extends TileBlockEntityCyclic implements Hopper {

  ItemStackHandler inventory = new ItemStackHandler(1);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileSimpleHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPER.get(), pos, state);
  }

  public TileSimpleHopper(BlockEntityType<TileGoldHopper> t, BlockPos pos, BlockState state) {
    super(t, pos, state);
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

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileSimpleHopper e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileSimpleHopper e) {
    e.tick();
  }

  public void tick() {
    //block if redstone powered
    if (this.isPowered()) {
      return;
    }
    //no cooldown unlike mojang hopper
    this.tryPullFromWorld(worldPosition.relative(Direction.UP));
    this.tryExtract(inventory, Direction.UP, getFlow(), null);
    Direction exportToSide = this.getBlockState().getValue(BlockFluidHopper.FACING);
    //is it a composter
    this.moveItemToCompost(exportToSide, inventory);
    this.moveItems(exportToSide, getFlow(), inventory);
  }

  public int getFlow() {
    return 1;
  }

  private void tryPullFromWorld(BlockPos center) {
    List<ItemEntity> list = HopperBlockEntity.getItemsAtAndAbove(level, this);
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(level.random.nextInt(list.size()));
      ItemStack remainder = stackEntity.getItem();
      remainder = inventory.insertItem(0, remainder, false);
      stackEntity.setItem(remainder);
      if (remainder.isEmpty()) {
        stackEntity.remove(Entity.RemovalReason.KILLED);
      }
    }
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public double getLevelX() {
    return this.getBlockPos().getX();
  }

  @Override
  public double getLevelY() {
    return this.getBlockPos().getY();
  }

  @Override
  public double getLevelZ() {
    return this.getBlockPos().getZ();
  }
}
