package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemCollector extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  static final int MAX_SIZE = 12;
  static final int MAX_HEIGHT = 64;
  private int height = 1;
  private boolean directionIsUp = false;
  //radius 7 translates to 15x15 area (center block + 7 each side)
  ItemStackHandler inventory = new ItemStackHandler(2 * 9);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.filter_data;
    }
  };
  private int radius = 8;

  public TileItemCollector() {
    super(TileRegistry.COLLECTOR_ITEM);
    this.needsRedstone = 0; // default on
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    AxisAlignedBB aabb = getRange();
    //  && entity.getXpValue() > 0;//entity != null && entity.getHorizontalFacing() == facing;
    List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class, aabb, Entity::isAlive);
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(world.rand.nextInt(list.size()));
      ItemStack remainder = stackEntity.getItem();
      // and then pull 
      if (!FilterCardItem.filterAllowsExtract(filter.getStackInSlot(0), remainder)) {
        return; //not allowed
      }
      for (int i = 0; i < inventory.getSlots(); i++) {
        if (remainder.isEmpty()) {
          break;
        }
        remainder = inventory.insertItem(i, remainder, false);
      }
      stackEntity.setItem(remainder);
      if (remainder.isEmpty()) {
        stackEntity.remove();
      }
    }
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerItemCollector(i, world, pos, playerInventory, playerEntity);
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
  public void read(BlockState bs, CompoundNBT tag) {
    filter.deserializeNBT(tag.getCompound("filter"));
    radius = tag.getInt("radius");
    height = tag.getInt("height");
    directionIsUp = tag.getBoolean("directionIsUp");
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("radius", radius);
    tag.putInt("height", height);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  private BlockPos getTargetCenter() {
    // move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), radius + 1);
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(radius + 1), radius);
    int diff = directionIsUp ? 1 : -1;
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  private AxisAlignedBB getRange() {
    BlockPos center = getTargetCenter();
    int diff = directionIsUp ? 1 : -1;
    int yMin = center.getY();
    int yMax = center.getY() + diff * height;
    //for some reason
    if (!directionIsUp) {
      // when aiming down, we dont have the offset to get [current block] without this
      yMin++;
    }
    return new AxisAlignedBB(
        center.getX() - radius, yMin, center.getZ() - radius,
        center.getX() + radius + 1, yMax, center.getZ() + radius + 1);
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case SIZE:
        radius = Math.min(value, MAX_SIZE);
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
      case DIRECTION:
        this.directionIsUp = value == 1;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return this.render;
      case SIZE:
        return radius;
      case HEIGHT:
        return height;
      case DIRECTION:
        return directionIsUp ? 1 : 0;
    }
    return 0;
  }
}
