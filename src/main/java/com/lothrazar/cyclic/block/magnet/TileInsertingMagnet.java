package com.lothrazar.cyclic.block.magnet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.core.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileInsertingMagnet extends TileBlockEntityCyclic {

  private static final double ENTITY_PULL_DIST = 0.2; //closer than this and nothing happens
  private static final double ENTITY_PULL_SPEED_CUTOFF = 2; //closer than this and it slows
  private static final float ITEMSPEEDFAR = 0.8F;
  private static final float ITEMSPEEDCLOSE = 0.09F;

  public TileInsertingMagnet(BlockPos pos, BlockState state) {
    super(TileRegistry.MAGNET.get(), pos, state);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileInsertingMagnet e) {
    Set<Item> filter = new HashSet<>(); // TODO: filter from DATACARD if present
    final int radius = BlockMagnetPanel.RADIUS.get();
    int vradius = 0;
    int x = blockPos.getX();
    int y = blockPos.getY();
    int z = blockPos.getZ();
    AABB axisalignedbb = (new AABB(x, y, z, x + 1, y + 1, z + 1)).inflate(radius, vradius, radius);
    List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, axisalignedbb);
    pullEntityList(x + 0.5, y + 0.1, z + 0.5, true, list, filter);
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<ItemEntity> all, Set<Item> filter) {
    int moved = 0;
    double hdist, xDist, zDist;
    float speed;
    int direction = (towardsPos) ? 1 : -1;
    //negative to flip the vector and push it away
    for (ItemEntity entity : all) {
      if (entity == null) {
        continue;
      }
      if (filter != null
          && !filter.isEmpty()
          && !filter.contains(entity.getItem().getItem())) {
        // filter is not empty AND it one of it items matches /me/
        continue;
      }
      //being paranoid
      BlockPos p = entity.blockPosition();
      xDist = Math.abs(x - p.getX());
      zDist = Math.abs(z - p.getZ());
      hdist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (hdist > ENTITY_PULL_DIST) {
        speed = (hdist > ENTITY_PULL_SPEED_CUTOFF) ? ITEMSPEEDFAR : ITEMSPEEDCLOSE;
        setEntityMotionFromVector(entity, x, y, z, direction * speed);
        moved++;
      }
      //else its basically on it, no point
    }
    return moved;
  }

  public static void setEntityMotionFromVector(Entity entity, double x, double y, double z, float modifier) {
    Vector3 originalPosVector = new Vector3(x, y, z);
    Vector3 entityVector = new Vector3(entity);
    Vector3 finalVector = originalPosVector.copy().subtract(entityVector);
    if (finalVector.mag() > 1) {
      finalVector.normalize();
    }
    double motionX = finalVector.x * modifier;
    double motionY = finalVector.y * modifier;
    double motionZ = finalVector.z * modifier;
    entity.setDeltaMovement(motionX, motionY, motionZ);
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileInsertingMagnet e) {
    //
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
