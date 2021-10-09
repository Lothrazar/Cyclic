package com.lothrazar.cyclic.block.laser;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.data.OffsetEnum;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileLaser extends TileEntityBase implements MenuProvider {

  static enum Fields {
    REDSTONE, THICK, RED, GREEN, BLUE, ALPHA, XOFF, YOFF, ZOFF;
  }

  protected OffsetEnum xOffset = OffsetEnum.CENTER;
  protected OffsetEnum yOffset = OffsetEnum.CENTER;
  protected OffsetEnum zOffset = OffsetEnum.CENTER;
  private int red = 255;
  private int green = 0;
  private int blue = 0;
  private int alpha = 70;
  private int thick = 8;
  ItemStackHandler inventory = new ItemStackHandler(4) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() instanceof LocationGpsCard;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileLaser(BlockPos pos, BlockState state) {
    super(TileRegistry.laser, pos, state);
    this.needsRedstone = 0;
  }
  //  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileLaser e) {
  //    e.tick();
  //  }
  //
  //  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileLaser e) {
  //    e.tick();
  //  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerLaser(i, level, worldPosition, playerInventory, playerEntity);
  }

  BlockPos getPosTarget() {
    //before going to nextpos
    //do we have a center offset
    BlockPosDim loc = LocationGpsCard.getPosition(inventory.getStackInSlot(0));
    if (loc != null && loc.getPos() != null) {
      return loc.getPos();
    }
    return this.getBlockPos();
  }

  @Override
  public AABB getRenderBoundingBox() {
    return BlockEntity.INFINITE_EXTENT_AABB;
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
        if (value >= OffsetEnum.values().length) {
          value = 0;
        }
        this.xOffset = OffsetEnum.values()[value];
        break;
      case YOFF:
        if (value >= OffsetEnum.values().length) {
          value = 0;
        }
        this.yOffset = OffsetEnum.values()[value];
        break;
      case ZOFF:
        if (value >= OffsetEnum.values().length) {
          value = 0;
        }
        this.zOffset = OffsetEnum.values()[value];
        break;
    }
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    red = tag.getInt("red");
    green = tag.getInt("green");
    blue = tag.getInt("blue");
    alpha = tag.getInt("alpha");
    thick = tag.getInt("thick");
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("red", red);
    tag.putInt("green", green);
    tag.putInt("blue", blue);
    tag.putInt("alpha", alpha);
    tag.putInt("thick", thick);
    return super.save(tag);
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
