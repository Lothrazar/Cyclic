package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityDetector extends TileEntityBaseMachine implements ITickable {
  private int rangeX = 5;
  private int rangeY = 5;
  private int rangeZ = 5;
  private int limitUntilRedstone = 5;
  private boolean ifFoundGreaterThanLimit = false;
  private boolean isPoweredNow = false;
  @Override
  public void update() {
    World world = this.getWorld();
    List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1)).expand(rangeX, rangeY, rangeZ));
    int entitiesFound = (entityList == null) ? 0 : entityList.size();
    boolean trigger = (ifFoundGreaterThanLimit) ? (entitiesFound > limitUntilRedstone)
        : (entitiesFound < limitUntilRedstone);
    if (isPoweredNow != trigger) {
      isPoweredNow = trigger;
      IBlockState state = world.getBlockState(this.getPos());
      world.notifyBlockUpdate(this.getPos(), state, state, 3);
      world.notifyNeighborsOfStateChange(this.getPos(), this.blockType);
    }
  }
  @Override
  public boolean isPowered() {
    return isPoweredNow;
  }
}
