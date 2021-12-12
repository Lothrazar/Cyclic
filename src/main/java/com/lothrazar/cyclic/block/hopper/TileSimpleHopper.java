package com.lothrazar.cyclic.block.hopper;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.hopperfluid.BlockFluidHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSimpleHopper extends TileEntityBase implements ITickableTileEntity, IHopper {

  ItemStackHandler inventory = new ItemStackHandler(1);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileSimpleHopper() {
    super(TileRegistry.HOPPER.get());
  }

  public TileSimpleHopper(TileEntityType<? extends TileSimpleHopper> tileEntityType) {
    super(tileEntityType);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    //block if redstone powered
    if (this.isPowered()) {
      return;
    }
    this.tryPullFromWorld(pos.offset(Direction.UP));
    this.getItemsFromAdjacent(inventory, Direction.UP, getFlow());
    Direction exportToSide = this.getBlockState().get(BlockFluidHopper.FACING);
    this.moveItemsToAdjacent(inventory, exportToSide, getFlow());
  }

  public int getFlow() {
    return 1;
  }

  private int getRadius() {
    return 1;
  }

  private void tryPullFromWorld(BlockPos center) {
    int radius = getRadius();
    AxisAlignedBB aabb = new AxisAlignedBB(
        center.getX() - radius, center.getY(), center.getZ() - radius,
        center.getX() + radius + 1, center.getY(), center.getZ() + radius + 1);
    List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class, aabb, (entity) -> {
      return entity.isAlive() && !entity.getItem().isEmpty(); //  && entity.getXpValue() > 0;//entity != null && entity.getHorizontalFacing() == facing;
    });
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(world.rand.nextInt(list.size()));
      ItemStack remainder = stackEntity.getItem();
      remainder = inventory.insertItem(0, remainder, false);
      stackEntity.setItem(remainder);
      if (remainder.isEmpty()) {
        stackEntity.remove();
      }
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public double getXPos() {
    return this.getPos().getX();
  }

  @Override
  public double getYPos() {
    return this.getPos().getY();
  }

  @Override
  public double getZPos() {
    return this.getPos().getZ();
  }
}
