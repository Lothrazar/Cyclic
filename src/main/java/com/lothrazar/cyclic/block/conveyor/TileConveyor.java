package com.lothrazar.cyclic.block.conveyor;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

public class TileConveyor extends TileBlockEntityCyclic {

  public TileConveyor(BlockPos pos, BlockState state) {
    super(TileRegistry.CONVEYOR.get(), pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileConveyor e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileConveyor e) {
    e.tick();
  }

  public void tick() {
    if (level == null || worldPosition == null) {
      return;
    }
    List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition).expandTowards(0.0F, 0.5F, 0.0F));
    for (Entity e : entities) {
      makeEntitiesTravel(e, this.getBlockState(), this.worldPosition, level);
    }
  }

  public static void makeEntitiesTravel(Entity entity, BlockState bs, BlockPos pos, Level world) {
    if (entity instanceof Player) {
      Player p = (Player) entity;
      if (p.isCrouching()) {
        return;
      }
    }
    double normalizedX = entity.getX() - pos.getX();
    double normalizedZ = entity.getZ() - pos.getZ();
    final double offside = 0.01D;
    //if the normalized values are >1 or <0, they entity is right at the border so dont apply it now
    Direction facing = bs.getValue(BlockStateProperties.HORIZONTAL_FACING);
    if (facing.getAxis() == Axis.Z && (normalizedX > 1 - offside || normalizedX < 0 + offside)) {
      return;
    }
    if (facing.getAxis() == Axis.X && (normalizedZ > 1 - offside || normalizedZ < 0 + offside)) {
      return;
    }
    ConveyorType type = bs.getValue(BlockConveyor.TYPE);
    double heightLimit = (type.isVertical()) ? pos.getY() + 1.3D : pos.getY() + 0.125D;
    double speed = bs.getValue(BlockConveyor.SPEED).getSpeed(); //0.08D; //temp variable, replace with speed from blockstate later
    double xSpeed = 0.0D, zSpeed = 0.0D, ySpeed = 0.0D;
    if (entity.getY() > heightLimit) {
      return;
    }
    xSpeed = facing.getStepX() * speed;
    ySpeed = 0.0D;
    zSpeed = facing.getStepZ() * speed;
    if (type.isCorner()) {
      //      Direction rotated = type == ConveyorType.CORNER_RIGHT ? facing.rotateYCCW() : facing.rotateY();
      //
      //      if (Math.random() < 0.1) 
      //      if ((facing == Direction.NORTH && normalizedZ < 0.5D) || (facing == Direction.SOUTH && normalizedZ > 0.5D)) {
      //        ModCyclic.LOGGER.info("first half");
      //        xSpeed = rotated.getXOffset() * speed;
      //                zSpeed = 0.0D; 
      //      }
      //      if ((facing == Direction.WEST && normalizedX < 0.5D) || (facing == Direction.EAST && normalizedX > 0.5D)) {
      //        xSpeed = 0.0D;
      //        zSpeed = rotated.getZOffset() * speed;
      //      }
      //FIX their centering when going around corners
      if (facing.getAxis() == Axis.Z && (normalizedX < 0.4 || normalizedX > 0.6)) {
        entity.setPos(Math.floor(entity.getX()) + 0.5, entity.getY(), entity.getZ());
      }
      if (facing.getAxis() == Axis.X && (normalizedZ < 0.4 || normalizedZ > 0.6)) {
        //centralize Z
        entity.setPos(entity.getX(), entity.getY(), Math.floor(entity.getZ()) + 0.5);
      }
    }
    if (type.isVertical()) {
      double hackEdge = 0.1;
      if (normalizedX < hackEdge || normalizedZ < hackEdge
          || normalizedX > 1 - hackEdge || normalizedZ > 1 - hackEdge) {
        // ?? : investigate jump hacks here
        entity.setPos(entity.getX(), entity.getY() + .2, entity.getZ());
      }
      ySpeed = speed * 1.3;
      if (type == ConveyorType.DOWN) {
        ySpeed *= -1;
      }
    }
    if (xSpeed != 0.0D || ySpeed != 0.0D || zSpeed != 0.0D) {
      entity.setDeltaMovement(xSpeed, ySpeed, zSpeed);
    }
  }
}
