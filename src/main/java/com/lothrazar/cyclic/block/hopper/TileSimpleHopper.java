package com.lothrazar.cyclic.block.hopper;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.hoppergold.TileGoldHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.items.ItemStackHandler;


public class TileSimpleHopper extends TileBlockEntityCyclic implements Hopper {

  ItemStackHandler inventory = new ItemStackHandler(1);

  public TileSimpleHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPER.get(), pos, state);
  }

  public TileSimpleHopper(BlockEntityType<TileGoldHopper> t, BlockPos pos, BlockState state) {
    super(t, pos, state);
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
    Direction exportToSide = this.getBlockState().getValue(BlockSimpleHopper.FACING);
    //is it a composter
    this.moveItemToCompost(exportToSide, inventory);
    this.moveItems(exportToSide, getFlow(), inventory);
    updateComparatorOutputLevel();
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
      updateComparatorOutputLevel();
    }
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    inventory.deserializeNBT(registries,tag.getCompound(NBTINV));
    super.loadAdditional(tag,registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    tag.put(NBTINV, inventory.serializeNBT(registries));
    super.saveAdditional(tag,registries);
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

  @Override
  public boolean isGridAligned() { return true; }
}
