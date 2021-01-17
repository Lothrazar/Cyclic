package com.lothrazar.cyclic.block.detectoritem;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.detectorentity.CompareType;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileDetectorItem extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

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

  public TileDetectorItem() {
    super(TileRegistry.detector_item);
  }

  @Override
  public void tick() {
    timer--;
    if (world.isRemote || timer > 0) {
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
      BlockState state = world.getBlockState(this.getPos());
      world.notifyBlockUpdate(this.getPos(), state, state, 3);
      try {
        world.notifyNeighborsOfStateChange(this.getPos(), this.getBlockState().getBlock());
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
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerDetectorItem(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public boolean isPowered() {
    return isPoweredNow;
  }

  private int getCountInRange() {
    AxisAlignedBB entityRange = getRange();
    int entitiesFound = 0;
    List<ItemEntity> entityList = world.getEntitiesWithinAABB(ItemEntity.class, entityRange);
    for (ItemEntity item : entityList) {
      entitiesFound += item.getItem().getCount();
    }
    return entitiesFound;
  }

  private AxisAlignedBB getRange() {
    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    AxisAlignedBB entityRange = new AxisAlignedBB(
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
        this.render = value % 2;
      break;
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    this.rangeX = tag.getInt("ox");
    this.rangeY = tag.getInt("oy");
    this.rangeZ = tag.getInt("oz");
    this.limitUntilRedstone = tag.getInt("limit");
    int cType = tag.getInt("compare");
    if (cType >= 0 && cType < CompareType.values().length) {
      this.compType = CompareType.values()[cType];
    }
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("ox", rangeX);
    tag.putInt("oy", rangeY);
    tag.putInt("oz", rangeZ);
    tag.putInt("limit", limitUntilRedstone);
    tag.putInt("compare", compType.ordinal());
    return super.write(tag);
  }

  public List<BlockPos> getShape() {
    return UtilShape.getShape(getRange(), pos.getY());
  }
}
