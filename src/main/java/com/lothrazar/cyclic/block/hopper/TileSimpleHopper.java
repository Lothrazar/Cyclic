package com.lothrazar.cyclic.block.hopper;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.hopperfluid.BlockFluidHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSimpleHopper extends TileEntityBase implements ITickableTileEntity {

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
  public void tick() {
    //block if redstone powered
    if (this.isPowered()) {
      return;
    }
    tryPullFromWorld(pos.offset(Direction.UP));
    tryExtract(Direction.UP);
    Direction exportToSide = this.getBlockState().get(BlockFluidHopper.FACING);
    this.moveItems(exportToSide, getFlow(), inventory);
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

  private void tryExtract(Direction extractSide) {
    if (extractSide == null) {
      return;
    }
    BlockPos posTarget = this.pos.offset(extractSide);
    TileEntity tile = world.getTileEntity(posTarget);
    if (tile != null) {
      IItemHandler itemHandlerFrom = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, extractSide.getOpposite()).orElse(null);
      if (itemHandlerFrom != null) {
        //ok go
        ItemStack itemTarget;
        for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
          itemTarget = itemHandlerFrom.extractItem(i, getFlow(), true);
          if (itemTarget.isEmpty()) {
            continue; // nothing extracted
          }
          ItemStack resultSimulate = inventory.insertItem(0, itemTarget.copy(), true);
          if (resultSimulate.getCount() < itemTarget.getCount()) {
            //simulate worked
            // and then pull 
            itemTarget = itemHandlerFrom.extractItem(i, getFlow(), false);
            //            ItemStack result =
            inventory.insertItem(0, itemTarget, false);
            return;
          }
        }
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
}
