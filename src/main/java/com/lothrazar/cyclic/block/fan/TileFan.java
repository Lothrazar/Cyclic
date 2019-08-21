package com.lothrazar.cyclic.block.fan;

import java.util.List;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileFan extends TileEntityBase implements ITickableTileEntity {

  private int range = 7;
  private int speed = 5;

  public TileFan() {
    super(CyclicRegistry.fantile);
  }

  @Override
  public void tick() {
    if (this.isPowered() == false) {
      setAnimation(false);
      return;
    }
    setAnimation(true);
    this.pushEntities();
  }

  private void setAnimation(boolean lit) {
    this.world.setBlockState(pos, this.world.getBlockState(pos).with(BlockFan.IS_LIT, lit));
  }

  private static final int MIN_RANGE = 1;

  public int getRange() {
    return this.range;
  }

  private float getSpeedCalc() {
    return (this.speed) / 35F;
  }

  private int getCurrentRange() {
    Direction facing = getCurrentFacing();
    BlockPos tester;
    for (int i = MIN_RANGE; i <= this.getRange(); i++) {//if we start at fan, we hit MYSELF (the fan)
      tester = this.getPos().offset(facing, i);
      if (canBlowThrough(tester) == false) {
        return i; //cant pass thru
      }
    }
    return getRange();
  }

  private boolean canBlowThrough(BlockPos tester) {
    //passes through air, and anything NOT a full block
    return this.getWorld().isAirBlock(tester);// HMM? HOW TO ? || !this.getWorld().getBlockState(tester).func_215691_g()
  }

  public List<BlockPos> getShape() {
    return UtilShape.line(getPos(), getCurrentFacing(), getCurrentRange());
  }

  private Direction getCurrentFacing() {
    return this.getBlockState().get(BlockStateProperties.FACING);
  }

  private int pushEntities() {
    List<BlockPos> shape = getShape();
    if (shape.size() == 0) {
      // sometimes is empty on changing dimension or tile load/unload
      return 0;
    }
    BlockPos start = shape.get(0);
    BlockPos end = shape.get(shape.size() - 1);//without this hotfix, fan works only on the flatedge of the band, not the 1x1 area
    switch (getCurrentFacing().getAxis()) {
      case X:
        end = end.add(0, 0, 1);//X means EASTorwest. adding +1z means GO 1 south
        end = end.add(0, 1, 0);//and of course go up one space. so we have a 3D range selected not a flat slice (ex: height 66 to 67)
      break;
      case Z:
        end = end.add(1, 0, 0);
        end = end.add(0, 1, 0);//and of course go up one space. so we have a 3D range selected not a flat slice (ex: height 66 to 67)
      break;
      case Y:
        start = start.add(1, 0, 0);
        end = end.add(0, 0, 1);
      default:
      break;
    }
    //ok now we have basically teh 3d box we wanted
    //problem: NORTH and WEST are skipping first blocks right at fan, but shouldnt.
    //EAST and SOUTH are skiping LAST blocks, but shouldnt
    //just fix it. root cause seems fine esp with UtilShape used
    Direction face = getCurrentFacing();
    switch (face) {
      case NORTH:
        start = start.south();
      break;
      case SOUTH:
        end = end.south();
      break;
      case EAST:
        end = end.east();
      break;
      case WEST:
        start = start.east();
      break;
      case DOWN:
      break;
      case UP:
      default:
      break;
    }
    AxisAlignedBB region = new AxisAlignedBB(start, end);
    List<Entity> entitiesFound = this.getWorld().getEntitiesWithinAABB(Entity.class, region);//UtilEntity.getLivingHostile(, region);
    int moved = 0;
    boolean doPush = true;// (pushIfZero == 0);
    int direction = 1;
    float SPEED = this.getSpeedCalc();
    for (Entity entity : entitiesFound) {
      if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isSneaking()) {
        continue;//sneak avoid feature
      }
      moved++;
      double newx = entity.getMotion().getX();
      double newy = entity.getMotion().getY();
      double newz = entity.getMotion().getZ();
      switch (face) {
        case NORTH:
          direction = !doPush ? 1 : -1;
          newz += direction * SPEED;
        break;
        case SOUTH:
          direction = doPush ? 1 : -1;
          newz += direction * SPEED;
        break;
        case EAST:
          direction = doPush ? 1 : -1;
          newx += direction * SPEED;
        break;
        case WEST:
          direction = !doPush ? 1 : -1;
          newx += direction * SPEED;
        break;
        case DOWN:
          direction = !doPush ? 1 : -1;
          newy += direction * SPEED;
        break;
        case UP:
          direction = doPush ? 1 : -1;
          newy += direction * SPEED;
        default:
        break;
      }
      entity.setMotion(newx, newy, newz);
    }
    return moved;
  }
}
