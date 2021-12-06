package com.lothrazar.cyclic.block.collectitem;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemCollector extends TileBlockEntityCyclic implements MenuProvider {

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
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }
  };
  private int radius = 8;

  public TileItemCollector(BlockPos pos, BlockState state) {
    super(TileRegistry.COLLECTOR.get(), pos, state);
    this.needsRedstone = 0; // default on
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileItemCollector e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileItemCollector e) {
    e.tick();
  }

  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (level.isClientSide) {
      return;
    }
    AABB aabb = getRange();
    List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, aabb, (entity) -> {
      return entity.isAlive(); //  && entity.getXpValue() > 0;//entity != null && entity.getHorizontalFacing() == facing;
    });
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(level.random.nextInt(list.size()));
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
        stackEntity.remove(Entity.RemovalReason.DISCARDED);
      }
    }
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerItemCollector(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    filter.deserializeNBT(tag.getCompound("filter"));
    radius = tag.getInt("radius");
    height = tag.getInt("height");
    directionIsUp = tag.getBoolean("directionIsUp");
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put("filter", filter.serializeNBT());
    tag.putInt("radius", radius);
    tag.putInt("height", height);
    tag.putBoolean("directionIsUp", directionIsUp);
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  private BlockPos getTargetCenter() {
    // move center over that much, not including exact horizontal
    return this.getBlockPos().relative(this.getCurrentFacing(), radius + 1);
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(radius + 1), radius);
    int diff = directionIsUp ? 1 : -1;
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  private AABB getRange() {
    BlockPos center = getTargetCenter();
    int diff = directionIsUp ? 1 : -1;
    int yMin = center.getY();
    int yMax = center.getY() + diff * height;
    //for some reason
    if (!directionIsUp) {
      // when aiming down, we dont have the offset to get [current block] without this
      yMin++;
    }
    AABB aabb = new AABB(
        center.getX() - radius, yMin, center.getZ() - radius,
        center.getX() + radius + 1, yMax, center.getZ() + radius + 1);
    return aabb;
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
