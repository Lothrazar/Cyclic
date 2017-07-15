package com.lothrazar.cyclicmagic.component.entitydetector;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder.BuildType;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder.Fields;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDetector extends TileEntityBaseMachineInvo implements ITickable, ITilePreviewToggle {
  public TileEntityDetector() {
    super(0);
  }
  private int rangeX = 5;
  private int rangeY = 5;
  private int rangeZ = 5;
  private int limitUntilRedstone = 5;
  private boolean isPoweredNow = false;
  private CompareType compType = CompareType.GREATER;
  private EntityType entityType = EntityType.LIVING;
  private int renderParticles;
  private static final int MAX_RANGE = 16;
  public static enum Fields {
    GREATERTHAN, LIMIT, RANGEX, RANGEY, RANGEZ, ENTITYTYPE, RENDERPARTICLES;
  }
  public static enum EntityType {
    LIVING, ITEM, EXP, PLAYER;
  }
  public static enum CompareType {
    LESS, GREATER, EQUAL;
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    World world = this.getWorld();
    BlockPos p = this.getPos();
    double x = p.getX();
    double y = p.getY();
    double z = p.getZ();
    AxisAlignedBB entityRange = new AxisAlignedBB(
        x - this.rangeX, y - this.rangeY, z - this.rangeZ,
        x + this.rangeX, y + this.rangeY, z + this.rangeZ);
    List<Entity> entityList = world.getEntitiesWithinAABB(getEntityClass(), entityRange);
    int entitiesFound = (entityList == null) ? 0 : entityList.size();
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
      IBlockState state = world.getBlockState(this.getPos());
      world.notifyBlockUpdate(this.getPos(), state, state, 3);
      world.notifyNeighborsOfStateChange(this.getPos(), this.blockType, true);//bool is new in 1111
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
        return this.renderParticles;
      default:
      break;
    }
    return 0;
  }
  public void setField(Fields f, int value) {
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
      case ENTITYTYPE:
        if (value >= EntityType.values().length)
          value = 0;
        if (value < 0)
          value = EntityType.values().length - 1;
        this.entityType = EntityType.values()[value];
      break;
      case RENDERPARTICLES:
        this.renderParticles = value;
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
    int cType = tagCompound.getInteger("compare");
    if (cType >= 0 && cType < CompareType.values().length)
      this.compType = CompareType.values()[cType];
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
    tagCompound.setInteger("compare", compType.ordinal());
    tagCompound.setInteger("et", entityType.ordinal());
    return super.writeToNBT(tagCompound);
  }
  @Override
  public List<BlockPos> getShape() {
    return UtilShape.rectFrame(this.getPos(), this.rangeX, this.rangeY, this.rangeZ);
  }
  @Override
  public void togglePreview() {
    int val = (this.renderParticles + 1) % 2;
    this.setField(Fields.RENDERPARTICLES.ordinal(), val);
  }
  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }
}
