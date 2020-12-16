package com.lothrazar.cyclic.block.laser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.OffsetEnum;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileLaser extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  public static enum Fields {
    REDSTONE, THICK, RED, GREEN, BLUE, ALPHA, XOFF, YOFF, ZOFF;
  }

  protected OffsetEnum xOffset = OffsetEnum.CENTER;
  protected OffsetEnum yOffset = OffsetEnum.CENTER;
  protected OffsetEnum zOffset = OffsetEnum.CENTER;
  private int red = 255;
  private int green = 0;
  private int blue = 0;
  private int alpha = 70;//1-100 will become 0-1
  private int thick = 8;//1-20 
  ItemStackHandler inventory = new ItemStackHandler(4) {

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileLaser() {
    super(TileRegistry.laser);
    this.needsRedstone = 0;
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerLaser(i, world, pos, playerInventory, playerEntity);
  }

  BlockPos getPosTarget() {
    //before going to nextpos
    //do we have a center offset
    BlockPosDim loc = LocationGpsCard.getPosition(inventory.getStackInSlot(0));
    if (loc != null && loc.getPos() != null) {
      return loc.getPos();
    }
    return this.getPos();
  }

  @Override
  public void tick() {}

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case THICK:
        return this.thick;
      case REDSTONE:
        return this.needsRedstone;
      case BLUE:
        return blue;
      case GREEN:
        return green;
      case RED:
        return red;
      case ALPHA:
        return alpha;
      case XOFF:
        return this.xOffset.ordinal();
      case YOFF:
        return this.yOffset.ordinal();
      case ZOFF:
        return this.zOffset.ordinal();
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case THICK:
        this.thick = value;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case BLUE:
        blue = value;
      break;
      case GREEN:
        green = value;
      break;
      case RED:
        red = value;
      break;
      case ALPHA:
        alpha = value;
      break;
      case XOFF:
        if (value >= OffsetEnum.values().length)
          value = 0;
        this.xOffset = OffsetEnum.values()[value];
      break;
      case YOFF:
        if (value >= OffsetEnum.values().length)
          value = 0;
        this.yOffset = OffsetEnum.values()[value];
      break;
      case ZOFF:
        if (value >= OffsetEnum.values().length)
          value = 0;
        this.zOffset = OffsetEnum.values()[value];
      break;
    }
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    red = tag.getInt("red");
    green = tag.getInt("green");
    blue = tag.getInt("blue");
    alpha = tag.getInt("alpha");
    thick = tag.getInt("thick");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("red", red);
    tag.putInt("green", green);
    tag.putInt("blue", blue);
    tag.putInt("alpha", alpha);
    tag.putInt("thick", thick);
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
    float a = alpha;
    return a / 100F;
  }

  public float getThick() {
    float t = thick;
    return t / 100F;
  }
}
