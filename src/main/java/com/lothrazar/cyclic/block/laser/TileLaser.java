package com.lothrazar.cyclic.block.laser;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilNBT;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileLaser extends TileEntityBase implements ITickableTileEntity {

  BlockPos laserTarget;
  int laserTimer;
  private int red = 255;
  private int green = 0;
  private int blue = 0;
  private int alpha = 90;//1-100 will become 0-1

  public static enum Fields {
    REDSTONE, TIMER, R, G, B, ALPHA;//, PULSE, EXTENDING, XOFF, YOFF, ZOFF;
  }

  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  public TileLaser() {
    super(TileRegistry.laser);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof LocationGpsCard;
      }
    };
  }

  BlockPos getPosTarget() {
    IItemHandler inv = this.inventory.orElse(null);
    if (inv != null) {
      //before going to nextpos
      //do we have a center offset
      BlockPosDim loc = LocationGpsCard.getPosition(inv.getStackInSlot(0));
      if (loc != null && loc.getPos() != null) {
        return loc.getPos();
      }
    }
    return this.getPos();
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
      case TIMER:
        return this.timer;
      case REDSTONE:
        return this.needsRedstone;
      case B:
        return blue;
      case G:
        return green;
      case R:
        return red;
      case ALPHA:
        return alpha;
      //      case PULSE:
      //        return isPulsing ? 1 : 0;
      //      case EXTENDING:
      //        return isExtending ? 1 : 0;
      //      case XOFF:
      //        return this.xOffset.ordinal();
      //      case YOFF:
      //        return this.yOffset.ordinal();
      //      case ZOFF:
      //        return this.zOffset.ordinal();
      //      default:
      //      break;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case B:
        blue = value;
      break;
      case G:
        green = value;
      break;
      case R:
        red = value;
      break;
      case ALPHA:
        alpha = value;
      break;
      //      case PULSE:
      //        isPulsing = (value == 1);
      //      break;
      //      case EXTENDING:
      //        isExtending = (value == 1);
      //      break;
      //      case XOFF:
      //        if (value >= OffsetEnum.values().length)
      //          value = 0;
      //        this.xOffset = OffsetEnum.values()[value];
      //      break;
      //      case YOFF:
      //        if (value >= OffsetEnum.values().length)
      //          value = 0;
      //        this.yOffset = OffsetEnum.values()[value];
      //      break;
      //      case ZOFF:
      //        if (value >= OffsetEnum.values().length)
      //          value = 0;
      //        this.zOffset = OffsetEnum.values()[value];
      //      break;
      //      default:
      //      break;
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    this.laserTarget = UtilNBT.getBlockPos(tag);
    laserTimer = tag.getInt("lt");
    red = tag.getInt("red");
    green = tag.getInt("green");
    blue = tag.getInt("blue");
    alpha = tag.getInt("alpha");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    if (laserTarget == null) {
      laserTarget = BlockPos.ZERO;
    }
    UtilNBT.putBlockPos(tag, laserTarget);
    tag.putInt("lt", laserTimer);
    tag.putInt("red", red);
    tag.putInt("green", green);
    tag.putInt("blue", blue);
    tag.putInt("alpha", alpha);
    return super.write(tag);
  }

  public float getRed() {
    return red / 255F;
  }

  public float getBlue() {
    return blue / 255F;
  }

  public float getGreen() {
    return green / 255F;
  }

  public float getAlpha() {
    return alpha / 100F;
  }

  public float getThick() {
    return 0.1F;
  }
}
