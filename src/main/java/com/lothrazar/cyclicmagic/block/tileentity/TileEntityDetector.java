package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityDetector extends TileEntityBaseMachineInvo implements ITickable {
  private int rangeX = 5;
  private int rangeY = 5;
  private int rangeZ = 5;
  private int limitUntilRedstone = 5;
  private int ifFoundGreaterThanLimit = 0;
  private boolean isPoweredNow = false;
  private EntityType entityType = EntityType.LIVING;
  private static final int MAX_RANGE = 16;
  public static enum Fields {
    GREATERTHAN, LIMIT, RANGEX, RANGEY, RANGEZ, ENTITYTYPE;
  }
  public static enum EntityType {
    LIVING, ITEM, EXP, PLAYER;
  }
  @Override
  public void update() {
    World world = this.getWorld();
    List<Entity> entityList = world.getEntitiesWithinAABB(getEntityClass(), new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1)).expand(rangeX, rangeY, rangeZ));
    int entitiesFound = (entityList == null) ? 0 : entityList.size();
    //System.out.println("entitiesFound="+entitiesFound);
    boolean trigger = (ifFoundGreaterThanLimit == 1) ? (entitiesFound > limitUntilRedstone)
        : (entitiesFound < limitUntilRedstone);
    //System.out.println("isPoweredNow="+isPoweredNow+"__trigger="+trigger);
    if (isPoweredNow != trigger) {
      isPoweredNow = trigger;
      IBlockState state = world.getBlockState(this.getPos());
      world.notifyBlockUpdate(this.getPos(), state, state, 3);
      world.notifyNeighborsOfStateChange(this.getPos(), this.blockType);
    }
  }
  private Class<? extends Entity> getEntityClass() {
    switch (this.entityType) {
    case EXP:
      return EntityXPOrb.class;
    case ITEM:
      return EntityItem.class;
    case LIVING:
      return EntityLivingBase.class;
    case PLAYER:
      return EntityPlayer.class;
    default:
      break;
    }
    return null;
  }
  @Override
  public boolean isPowered() {
    return isPoweredNow;
  }
  @Override
  public int getField(int id) {
    return getField(Fields.values()[id]);
  }
  @Override
  public void setField(int id, int value) {
    setField(Fields.values()[id], value);
  }
  public int getField(Fields f) {
    switch (f) {
    case ENTITYTYPE:
      return this.entityType.ordinal();
    case GREATERTHAN:
      return this.ifFoundGreaterThanLimit;
    case LIMIT:
      return this.limitUntilRedstone;
    case RANGEX:
      return this.rangeX;
    case RANGEY:
      return this.rangeY;
    case RANGEZ:
      return this.rangeZ;
    default:
      break;
    }
    return 0;
  }
  public void setField(Fields f, int value) {
    if (f == Fields.GREATERTHAN) {
      if (value > 1) {
        value = 0;
      }
      if (value < 0) {
        value = 1;
      }
    }
    if (f == Fields.RANGEX || f == Fields.RANGEY || f == Fields.RANGEZ) {
      if (value > MAX_RANGE) {
        value = MAX_RANGE;
      }
      if (value < 1) {
        value = 1;
      }
    }
    if (f == Fields.LIMIT) {
      if (value > 999) {
        value = MAX_RANGE;
      }
      if (value < 1) {
        value = 1;
      }
    }
    switch (f) {
    case GREATERTHAN:
      this.ifFoundGreaterThanLimit = value;
      break;
    case LIMIT:
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
      if (value >= EntityType.values().length)
        value = 0;
      if (value < 0)
        value = EntityType.values().length - 1;
      this.entityType = EntityType.values()[value];
      break;
    default:
      break;
    }
  }
  public EntityType getEntityType() {
    int type = this.getField(Fields.ENTITYTYPE);
    return EntityType.values()[type];
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.rangeX = tagCompound.getInteger("ox");
    this.rangeY = tagCompound.getInteger("oy");
    this.rangeZ = tagCompound.getInteger("oz");
    this.limitUntilRedstone = tagCompound.getInteger("limit");
    this.ifFoundGreaterThanLimit = tagCompound.getInteger("compare");
    int eType = tagCompound.getInteger("et");
    if (eType >= 0 && eType < EntityType.values().length)
      this.entityType = EntityType.values()[eType];
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger("ox", rangeX);
    tagCompound.setInteger("oy", rangeY);
    tagCompound.setInteger("oz", rangeZ);
    tagCompound.setInteger("limit", limitUntilRedstone);
    tagCompound.setInteger("compare", ifFoundGreaterThanLimit);
    tagCompound.setInteger("et", entityType.ordinal());
    return super.writeToNBT(tagCompound);
  }
}
