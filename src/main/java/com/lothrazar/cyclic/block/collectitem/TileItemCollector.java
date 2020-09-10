package com.lothrazar.cyclic.block.collectitem;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemCollector extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  static enum Fields {
    REDSTONE, RENDER;
  }

  private int radius = 8;
  private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

  public TileItemCollector() {
    super(TileRegistry.collectortile);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(2 * 9);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerItemCollector(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return handler.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    radius = tag.getInt("radius");
    CompoundNBT invTag = tag.getCompound("inv");
    handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("radius", radius);
    handler.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  private BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), radius + 1);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (world.isRemote) {
      return;
    }
    AxisAlignedBB aabb = getRange();
    List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class, aabb, (entity) -> {
      return entity.isAlive();//  && entity.getXpValue() > 0;//entity != null && entity.getHorizontalFacing() == facing;
    });
    if (list.size() > 0) {
      ItemEntity stackEntity = list.get(world.rand.nextInt(list.size()));
      IItemHandler h = handler.orElse(null);
      ItemStack remainder = stackEntity.getItem();
      for (int i = 0; i < h.getSlots(); i++) {
        if (remainder.isEmpty()) {
          break;
        }
        remainder = h.insertItem(i, remainder, false);
      }
      stackEntity.setItem(remainder);
      if (remainder.isEmpty()) {
        stackEntity.remove();//kill it
      }
    }
  }

  public List<BlockPos> getShape() {
    return UtilShape.getShape(getRange(), pos.getY());
  }

  private AxisAlignedBB getRange() {
    BlockPos center = getTargetCenter();
    AxisAlignedBB aabb = new AxisAlignedBB(
        center.getX() - radius, center.getY(), center.getZ() - radius,
        center.getX() + radius, center.getY() + 2, center.getZ() + radius);
    return aabb;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return this.render;
    }
    return 0;
  }
}
