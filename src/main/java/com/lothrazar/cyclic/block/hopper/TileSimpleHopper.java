package com.lothrazar.cyclic.block.hopper;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.hoppergold.TileGoldHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;


public class TileSimpleHopper extends TileBlockEntityCyclic implements Hopper {

  ItemStackHandler inventory = new ItemStackHandler(1);

  public TileSimpleHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPER.get(), pos, state);
    this.needsRedstone = 0;
  }

  public TileSimpleHopper(BlockEntityType<TileGoldHopper> t, BlockPos pos, BlockState state) {
    super(t, pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileSimpleHopper e) {
    e.tick();
  }

  public void tick() {
    //block if redstone powered
    if (this.isPowered()) {
      return;
    }
    //no cooldown unlike mojang hopper
    this.tryPullFromWorld(worldPosition.relative(Direction.UP));
    this.tryExtract(inventory, Direction.UP, getFlow(), getFilter());
    Direction exportToSide = this.getBlockState().getValue(BlockSimpleHopper.FACING);
    //is it a composter
    this.moveItemToCompost(exportToSide, inventory);
    this.moveItems(exportToSide, getFlow(), inventory);
    updateComparatorOutputLevel();
  }

  public int getFlow() {
    return 1;
  }

  /**
   * Hook for subclasses (eg. gold hopper) to provide a filter card slot.
   * Returning null means no filter; matches the wooden hopper's behavior.
   */
  protected ItemStackHandler getFilter() {
    return null;
  }

  private void tryPullFromWorld(BlockPos center) {
    List<ItemEntity> list = HopperBlockEntity.getItemsAtAndAbove(level, this);
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(level.getRandom().nextInt(list.size()));
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
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  @Override
  public void setField(int field, int value) {
  }

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
  public boolean isGridAligned() {
    return true;
  }

  public ItemStackHandler getInventory() {
    return inventory;
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
