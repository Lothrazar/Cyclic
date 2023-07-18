package com.lothrazar.cyclic.block.fanslab;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileFanSlab extends TileBlockEntityCyclic {

  static enum Fields {
    REDSTONE, RANGE, SPEED;
  }

  public static final int MIN_RANGE = 1;
  public static final int MAX_RANGE = 64;
  public static final int MIN_SPEED = 1;
  public static final int MAX_SPEED = 20;
  private int range = 5;
  private int speed = 5;

  public TileFanSlab(BlockPos pos, BlockState state) {
    super(TileRegistry.FANSLAB.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileFanSlab e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileFanSlab e) {
    e.tick();
  }

  public void tick() {
    boolean powered = this.isPowered();
    boolean previous = this.getBlockState().getValue(BlockFanSlab.POWERED);
    if (previous != powered) {
      this.level.setBlockAndUpdate(worldPosition, this.getBlockState().setValue(BlockFanSlab.POWERED, powered));
    }
    if (powered) {
      this.pushEntities();
    }
  }

  public int getRange() {
    return this.range;
  }

  private float getSpeedCalc() {
    return (this.speed) / 39F;
  }

  private int getCurrentRange() {
    Direction facing = getCurrentFacing();
    BlockPos tester;
    for (int i = MIN_RANGE; i <= this.getRange(); i++) {
      //if we start at fan, we hit MYSELF (the fan)
      tester = this.getBlockPos().relative(facing, i);
      if (canBlowThrough(tester) == false) {
        return i; //cant pass thru
      }
    }
    return getRange();
  }

  private boolean canBlowThrough(BlockPos tester) {
    return !level.getBlockState(tester).canOcclude();
  }

  public List<BlockPos> getShape() {
    return ShapeUtil.line(getBlockPos(), getCurrentFacing(), getCurrentRange());
  }

  @Override
  public Direction getCurrentFacing() {
    switch (this.getBlockState().getValue(BlockFanSlab.FACE)) {
      case CEILING:
        return Direction.DOWN;
      case FLOOR:
        return Direction.UP;
      default:
        return this.getBlockState().getValue(BlockFanSlab.HORIZONTAL_FACING);
    }
  }

  private int pushEntities() {
    List<BlockPos> shape = getShape();
    if (shape.size() == 0) {
      // sometimes is empty on changing dimension or tile load/unload
      return 0;
    }
    BlockPos start = shape.get(0);
    BlockPos end = shape.get(shape.size() - 1); //without this hotfix, fan works only on the flatedge of the band, not the 1x1 area
    switch (getCurrentFacing().getAxis()) {
      case X:
        end = end.offset(0, 0, 1); //X means EASTorwest. adding +1z means GO 1 south
        end = end.offset(0, 1, 0); //and of course go up one space. so we have a 3D range selected not a flat slice (ex: height 66 to 67)
      break;
      case Z:
        end = end.offset(1, 0, 0);
        end = end.offset(0, 1, 0); //and of course go up one space. so we have a 3D range selected not a flat slice (ex: height 66 to 67)
      break;
      case Y:
        start = start.offset(1, 0, 0);
        end = end.offset(0, 0, 1);
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
        start = start.south();// push while INSIDE the block
      break;
      case SOUTH:
        start = start.north(); // push while INSIDE the block
        end = end.south();
      break;
      case EAST:
        start = start.west(); // push while INSIDE the block
        end = end.east();
      break;
      case WEST:
        start = start.east(); // push while INSIDE the block
        start = start.east();
      break;
      case DOWN:
      break;
      case UP:
      default:
      break;
    }
    AABB region = new AABB(start, end);
    List<Entity> entitiesFound = this.getLevel().getEntitiesOfClass(Entity.class, region);
    int moved = 0;
    final boolean doPush = true;
    int direction = 1;
    float speed = this.getSpeedCalc();
    for (Entity entity : entitiesFound) {
      if (entity instanceof Player && ((Player) entity).isCrouching()) {
        continue; //sneak avoid feature
      }
      moved++;
      double newx = entity.getDeltaMovement().x();
      double newy = entity.getDeltaMovement().y();
      double newz = entity.getDeltaMovement().z();
      switch (face) {
        case NORTH:
          direction = !doPush ? 1 : -1;
          newz += direction * speed;
        break;
        case SOUTH:
          direction = doPush ? 1 : -1;
          newz += direction * speed;
        break;
        case EAST:
          direction = doPush ? 1 : -1;
          newx += direction * speed;
        break;
        case WEST:
          direction = !doPush ? 1 : -1;
          newx += direction * speed;
        break;
        case DOWN:
          direction = !doPush ? 1 : -1;
          newy += direction * speed;
        break;
        case UP:
          direction = doPush ? 1 : -1;
          newy += direction * speed;
        break;
      }
      entity.setDeltaMovement(newx, newy, newz);
      if (level.isClientSide && entity.tickCount % PacketPlayerFalldamage.TICKS_FALLDIST_SYNC == 0
          && entity instanceof Player) {
        PacketRegistry.INSTANCE.sendToServer(new PacketPlayerFalldamage());
      }
    }
    return moved;
  }

  @Override
  public void load(CompoundTag tag) {
    speed = tag.getInt("speed");
    range = tag.getInt("range");
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.putInt("speed", speed);
    tag.putInt("range", range);
    super.saveAdditional(tag);
  }

  @Override
  public int getField(int f) {
    switch (Fields.values()[f]) {
      case RANGE:
        return range;
      case REDSTONE:
        return this.needsRedstone;
      case SPEED:
        return this.speed;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    Fields f = Fields.values()[field];
    switch (f) {
      case RANGE:
        range = value;
        if (range < MIN_RANGE) {
          range = MIN_RANGE;
        }
        if (range > MAX_RANGE) {
          range = MAX_RANGE;
        }
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case SPEED:
        speed = value;
        if (speed < MIN_SPEED) {
          speed = MIN_SPEED;
        }
        if (speed > MAX_SPEED) {
          speed = MAX_SPEED;
        }
      break;
    }
  }
}
