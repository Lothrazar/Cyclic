package com.lothrazar.cyclic.block.detectoritem;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.detectorentity.CompareType;
import com.lothrazar.cyclic.data.PreviewOutlineType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileDetectorItem extends TileBlockEntityCyclic implements MenuProvider {

  static enum Fields {
    GREATERTHAN, LIMIT, RANGEX, RANGEY, RANGEZ, RENDER;
  }

  private static final int PER_TICK = 10;
  public static final int MAX_RANGE = 32;
  private int rangeX = 5;
  private int rangeY = 1;
  private int rangeZ = 5;
  //default is > 0 living entities 
  private int limitUntilRedstone = 0;
  private CompareType compType = CompareType.GREATER;
  private boolean isPoweredNow = false;

  public TileDetectorItem(BlockPos pos, BlockState state) {
    super(TileRegistry.DETECTOR_ITEM.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileDetectorItem e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileDetectorItem e) {
    e.tick();
  }

  public void tick() {
    timer--;
    if (level.isClientSide || timer > 0) {
      return;
    }
    timer = PER_TICK;
    //and then
    int entitiesFound = getCountInRange();
    boolean trigger = false;
    switch (this.compType) {
      case LESS:
        trigger = (entitiesFound < limitUntilRedstone);
      break;
      case GREATER:
        trigger = (entitiesFound > limitUntilRedstone);
      break;
      case EQUAL:
        trigger = (entitiesFound == limitUntilRedstone);
      break;
      default:
      break;
    }
    if (isPoweredNow != trigger) {
      isPoweredNow = trigger;
      BlockState state = level.getBlockState(this.getBlockPos());
      level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
      try {
        level.updateNeighborsAt(this.getBlockPos(), this.getBlockState().getBlock());
      }
      catch (Throwable e) {
        //somehow this lead to a  
        //        java.lang.NullPointerException
        //        at net.minecraft.block.BlockDoor.neighborChanged(BlockDoor.java:228)
        // from the notifyNeighborsOfStateChange(...)
        // door was doing a get state on pos.up() , so its top half..
        //this catch means no game crash 
        ModCyclic.LOGGER.error("Detector: State change error in adjacent block ", e);
      }
    }
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.DETECTOR_ITEM.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerDetectorItem(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public boolean isPowered() {
    return isPoweredNow;
  }

  private int getCountInRange() {
    AABB entityRange = getRange();
    int entitiesFound = 0;
    List<ItemEntity> entityList = level.getEntitiesOfClass(ItemEntity.class, entityRange);
    for (ItemEntity item : entityList) {
      entitiesFound += item.getItem().getCount();
    }
    return entitiesFound;
  }

  private AABB getRange() {
    double x = worldPosition.getX();
    double y = worldPosition.getY();
    double z = worldPosition.getZ();
    AABB entityRange = new AABB(
        x - this.rangeX, y - this.rangeY, z - this.rangeZ,
        x + this.rangeX + 1, y + this.rangeY, z + this.rangeZ + 1);
    return entityRange;
  }

  @Override
  public int getField(int f) {
    switch (Fields.values()[f]) {
      case GREATERTHAN:
        return this.compType.ordinal();
      case LIMIT:
        return this.limitUntilRedstone;
      case RANGEX:
        return this.rangeX;
      case RANGEY:
        return this.rangeY;
      case RANGEZ:
        return this.rangeZ;
      case RENDER:
        return this.render;
      default:
      break;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    Fields f = Fields.values()[field];
    if (f == Fields.RANGEX || f == Fields.RANGEY || f == Fields.RANGEZ) {
      if (value > MAX_RANGE) {
        value = MAX_RANGE;
      }
      if (value < 1) {
        value = 1;
      }
    }
    switch (f) {
      case GREATERTHAN:
        if (value >= CompareType.values().length) {
          value = 0;
        }
        if (value < 0) {
          value = CompareType.values().length - 1;
        }
        this.compType = CompareType.values()[value];
      break;
      case LIMIT:
        if (value > 999) {
          value = MAX_RANGE;
        }
        if (value < 0) {
          value = 0;
        }
        this.limitUntilRedstone = value;
      break;
      case RANGEX:
        this.rangeX = value;
      break;
      case RANGEY:
        this.rangeY = value;
      break;
      case RANGEZ:
        this.rangeZ = value;
      break;
      case RENDER:
        this.render = value % PreviewOutlineType.values().length;
      break;
    }
  }

  @Override
  public void load(CompoundTag tag) {
    this.rangeX = tag.getInt("ox");
    this.rangeY = tag.getInt("oy");
    this.rangeZ = tag.getInt("oz");
    this.limitUntilRedstone = tag.getInt("limit");
    int cType = tag.getInt("compare");
    if (cType >= 0 && cType < CompareType.values().length) {
      this.compType = CompareType.values()[cType];
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("ox", rangeX);
    tag.putInt("oy", rangeY);
    tag.putInt("oz", rangeZ);
    tag.putInt("limit", limitUntilRedstone);
    tag.putInt("compare", compType.ordinal());
    super.saveAdditional(tag);
  }

  public List<BlockPos> getShape() {
    return ShapeUtil.getShape(getRange(), worldPosition.getY());
  }
}
