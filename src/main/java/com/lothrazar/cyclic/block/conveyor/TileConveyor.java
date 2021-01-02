package com.lothrazar.cyclic.block.conveyor;

import java.util.List;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
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
    if (this.world == null) {
      return;
    }
    World world = this.world;
    Direction facing = this.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
    //TODO: offset not used?
    Vector3d offset = Vector3d.copyCenteredHorizontally(this.pos.offset(facing));
    BlockConveyor.Type type = this.getBlockState().get(BlockConveyor.TYPE);
    Direction vertical = type == BlockConveyor.Type.DOWN ? Direction.DOWN : type == BlockConveyor.Type.UP ? Direction.UP : null;
    if (vertical == Direction.UP) {
      offset = Vector3d.copyCentered(this.pos.offset(facing).offset(vertical));
    }
    else if (vertical == Direction.DOWN) {
      offset = Vector3d.copyCentered(this.pos.offset(facing.getOpposite()).offset(vertical));
    }
    List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos).expand(0.0F, 0.5F, 0.0F));
    List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(this.pos).expand(0.0F, 0.5F, 0.0F));
    //    Vector3d center = Vector3d.copyCenteredHorizontally(new Vector3i(this.pos.getX(), this.pos.getY(), this.pos.getZ()));
    entities.addAll(items);
    //UtilEntity.pullEntityList(offset.getX(), offset.getY(), offset.getZ(), true, entities);
    double heightLimit = this.getPos().getY() + 0.125D;
    double speed = 0.08D; //temp variable, replace with speed from blockstate later
    double xSpeed = 0.0D, zSpeed = 0.0D, ySpeed = 0.0D;
    for (Entity e : entities) {
      if (e.getPosY() > heightLimit)
        continue;
      xSpeed = facing.getXOffset() * speed;
      zSpeed = facing.getZOffset() * speed;
      ySpeed = 0.0D;
      double normalizedX = e.getPosX() - this.getPos().getX();
      double normalizedZ = e.getPosZ() - this.getPos().getZ();
      if (type == BlockConveyor.Type.CORNER_LEFT || type == BlockConveyor.Type.CORNER_RIGHT) {
        Direction rotated = type == BlockConveyor.Type.CORNER_LEFT ? facing.rotateYCCW() : facing.rotateY();
        if (Math.random() < 0.1)
          System.out.printf("Corner %s to %s. Entity at normalized [%f, %f]%n", facing.getString(), rotated.getString(), normalizedX, normalizedZ);
        if ((facing == Direction.NORTH && normalizedZ < 0.5D) || (facing == Direction.SOUTH && normalizedZ > 0.5D)) {
          xSpeed = rotated.getXOffset() * speed;
          zSpeed = 0.0D;
        }
        if ((facing == Direction.WEST && normalizedX < 0.5D) || (facing == Direction.EAST && normalizedX > 0.5D)) {
          xSpeed = 0.0D;
          zSpeed = rotated.getZOffset() * speed;
        }
      }
      if (type == BlockConveyor.Type.UP || type == BlockConveyor.Type.DOWN) {
        double normalizedDistance = 0.0D;
        if (facing == Direction.NORTH) normalizedDistance = 1 - normalizedZ;
        if (facing == Direction.SOUTH) normalizedDistance = normalizedZ;
        if (facing == Direction.EAST) normalizedDistance = normalizedX;
        if (facing == Direction.WEST) normalizedDistance = 1 - normalizedX;
        if (type == BlockConveyor.Type.DOWN)
          normalizedDistance = 1 - normalizedDistance;
        e.setPosition(e.getPosX(), this.getPos().getY() + normalizedDistance, e.getPosZ());
      }
      if (xSpeed != 0.0D || zSpeed != 0.0D)
        e.setMotion(xSpeed, ySpeed, zSpeed);
      //e.setPosition(e.getPosX(), this.getPos().getY() + 0.125D, e.getPosZ());
    }
  }
}
