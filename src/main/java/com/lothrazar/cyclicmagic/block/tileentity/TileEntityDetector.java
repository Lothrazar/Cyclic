package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder.Fields;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
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
    switch (f) {
    case ENTITYTYPE:
      break;
    case GREATERTHAN:
      break;
    case LIMIT:
      break;
    case RANGEX:
      break;
    case RANGEY:
      break;
    case RANGEZ:
      break;
    default:
      break;
    }
  }
}
