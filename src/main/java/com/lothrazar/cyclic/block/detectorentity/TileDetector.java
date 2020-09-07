package com.lothrazar.cyclic.block.detectorentity;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.EntityFilterType;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TileDetector extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final int PER_TICK = 10;
  public static final int MAX_RANGE = 32;
  private int rangeX = 5;
  private int rangeY = 1;
  private int rangeZ = 5;
  //default is > 0 living entities 
  private int limitUntilRedstone = 0;
  private CompareType compType = CompareType.GREATER;
  EntityFilterType entityFilter = EntityFilterType.LIVING;
  private boolean isPoweredNow = false;

  public static enum Fields {
    GREATERTHAN, LIMIT, RANGEX, RANGEY, RANGEZ, ENTITYTYPE, RENDERPARTICLES;
  }

  public static enum CompareType {
    LESS, GREATER, EQUAL;
  }

  public TileDetector() {
    super(TileRegistry.detector_entity);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerDetector(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public boolean isPowered() {
    return isPoweredNow;
  }

  @Override
  public void tick() {
    timer--;
    if (world.isRemote || timer > 0) {
      return;//client so halt
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
    //    ModCyclic.LOGGER.info("! power " + isPoweredNow);
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
        ModCyclic.LOGGER.info("State change error in adjacent block ", e);
      }
    }
  }

  private int getCountInRange() {
    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    AxisAlignedBB entityRange = new AxisAlignedBB(
        x - this.rangeX, y - this.rangeY, z - this.rangeZ,
        x + this.rangeX, y + this.rangeY, z + this.rangeZ);
    List<? extends LivingEntity> list = this.entityFilter.getEntities(world, entityRange);
    //    entitiesFound = (entityList == null) ? 0 : entityList.size();
    //    ModCyclic.LOGGER.info(typeCurrent + " entitiesFound " + entitiesFound);
    return list.size();
  }

  @Override
  public int getField(int f) {
    switch (Fields.values()[f]) {
      case ENTITYTYPE:
        return this.entityFilter.ordinal();
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
      case RENDERPARTICLES:
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
          value = 999;
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
      case ENTITYTYPE:
        if (value >= EntityFilterType.values().length)
          value = 0;
        if (value < 0)
          value = EntityFilterType.values().length - 1;
        this.entityFilter = EntityFilterType.values()[value];
      break;
      case RENDERPARTICLES:
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
    if (cType >= 0 && cType < CompareType.values().length)
      this.compType = CompareType.values()[cType];
    int eType = tag.getInt("entityType");
    if (eType >= 0 && eType < EntityFilterType.values().length)
      this.entityFilter = EntityFilterType.values()[eType];
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("ox", rangeX);
    tag.putInt("oy", rangeY);
    tag.putInt("oz", rangeZ);
    tag.putInt("limit", limitUntilRedstone);
    tag.putInt("compare", compType.ordinal());
    tag.putInt("entityType", entityFilter.ordinal());
    return super.write(tag);
  }
}
