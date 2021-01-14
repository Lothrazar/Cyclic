package com.lothrazar.cyclic.block.conveyor;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileConveyor extends TileEntityBase implements ITickableTileEntity {

  public TileConveyor() {
    super(TileRegistry.conveyor);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void tick() {
    if (world == null || pos == null) {
      return;
    }
    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).expand(0.0F, 0.5F, 0.0F));
    for (Entity e : entities) {
      makeEntitiesTravel(e, this.getBlockState(), this.pos, world);
    }
  }

  /**
   * TODO: move to utility class
   */
  public static void makeEntitiesTravel(Entity entity, BlockState bs, BlockPos pos, World world) {
    if (entity instanceof PlayerEntity) {
      PlayerEntity p = (PlayerEntity) entity;
      if (p.isCrouching()) {
        return;
      }
    }
    double normalizedX = entity.getPosX() - pos.getX();
    double normalizedZ = entity.getPosZ() - pos.getZ();
    final double offside = 0.01D;
    //if the normalized values are >1 or <0, they entity is right at the border so dont apply it now
    Direction facing = bs.get(BlockStateProperties.HORIZONTAL_FACING);
    if (facing.getAxis() == Axis.Z && (normalizedX > 1 - offside || normalizedX < 0 + offside)) {
      //      ModCyclic.LOGGER.info("cancel edge movement  " + entity);
      return;
    }
    if (facing.getAxis() == Axis.X && (normalizedZ > 1 - offside || normalizedZ < 0 + offside)) {
      return;
    }
    ConveyorType type = bs.get(BlockConveyor.TYPE);
    double heightLimit = (type.isVertical()) ? pos.getY() + 1.3D : pos.getY() + 0.125D;
    double speed = bs.get(BlockConveyor.SPEED).getSpeed(); //0.08D; //temp variable, replace with speed from blockstate later
    double xSpeed = 0.0D, zSpeed = 0.0D, ySpeed = 0.0D;
    if (entity.getPosY() > heightLimit) {
      //            ModCyclic.LOGGER.info("cancel HEIGHT LIMIT ");
      return;
    }
    xSpeed = facing.getXOffset() * speed;
    ySpeed = 0.0D;
    zSpeed = facing.getZOffset() * speed;
    if (type.isCorner()) {
      //      Direction rotated = type == ConveyorType.CORNER_RIGHT ? facing.rotateYCCW() : facing.rotateY();
      //
      //      if (Math.random() < 0.1)
      //        System.out.printf("Corner %s to %s. Entity at normalized [%f, %f]%n", facing.getString(), rotated.getString(), normalizedX, normalizedZ);
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
        entity.setPosition(Math.floor(entity.getPosX()) + 0.5, entity.getPosY(), entity.getPosZ());
      }
      if (facing.getAxis() == Axis.X && (normalizedZ < 0.4 || normalizedZ > 0.6)) {
        //centralize Z
        entity.setPosition(entity.getPosX(), entity.getPosY(), Math.floor(entity.getPosZ()) + 0.5);
      }
    }
    if (type.isVertical()) {
      double hackEdge = 0.1;
      if (normalizedX < hackEdge || normalizedZ < hackEdge
          || normalizedX > 1 - hackEdge || normalizedZ > 1 - hackEdge) {
        ModCyclic.LOGGER.info("jump hacks " + entity);
        entity.setPosition(entity.getPosX(), entity.getPosY() + .2, entity.getPosZ());
      }
      ySpeed = speed * 1.3;
      if (type == ConveyorType.DOWN) {
        ySpeed *= -1;
      }
    }
    if (xSpeed != 0.0D || ySpeed != 0.0D || zSpeed != 0.0D) {
      entity.setMotion(xSpeed, ySpeed, zSpeed);
    }
  }
}
