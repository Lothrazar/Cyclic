package com.lothrazar.cyclic.block.conveyor;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

import java.util.List;

public class TileConveyor extends TileEntityBase implements ITickableTileEntity {
  public TileConveyor() {
    super(TileRegistry.conveyor);
  }

  @Override
  public void setField(int field, int value) {

  }

  @Override
  public int getField(int field) {
    return 0;
  }

  @Override
  public void tick() {
    if (this.world == null)
      return;
    World world = this.world;
    Direction facing = this.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
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
    entities.addAll(items);
    UtilEntity.pullEntityList(offset.getX(), offset.getY(), offset.getZ(), true, entities);
  }
}
