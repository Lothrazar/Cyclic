package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilNBT;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileLaser extends TileEntityBase implements ITickableTileEntity {

  BlockPos laserTarget;
  int laserTimer;

  static enum Fields {
    REDSTONE, RENDER, LT;
  }

  public TileLaser() {
    super(TileRegistry.laser);
  }

  @Override
  public void tick() {
    if (this.laserTimer > 0) {
      laserTimer--;
    }
    laserTarget = this.pos.up(3).west(7);//testing hack
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case LT:
        return this.laserTimer;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case LT:
        this.laserTimer = value;
      break;
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    this.laserTarget = UtilNBT.getBlockPos(tag);
    laserTimer = tag.getInt("lt");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    if (laserTarget == null) {
      laserTarget = BlockPos.ZERO;
    }
    UtilNBT.putBlockPos(tag, laserTarget);
    tag.putInt("lt", laserTimer);
    return super.write(tag);
  }
}
