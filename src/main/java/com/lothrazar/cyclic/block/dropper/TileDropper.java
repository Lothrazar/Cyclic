package com.lothrazar.cyclic.block.dropper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ConfigManager;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
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
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileDropper extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  static enum Fields {
    TIMER, REDSTONE, DROPCOUNT, DELAY, OFFSET, RENDER;
  }

  private int dropCount = 1;
  private int delay = 10;
  private int hOffset = 0;

  public TileDropper() {
    super(TileRegistry.dropper);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerDropper(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    this.delay = tag.getInt("delay");
    this.dropCount = tag.getInt("dropCount");
    this.hOffset = tag.getInt("hOffset");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    tag.putInt("delay", delay);
    tag.putInt("dropCount", dropCount);
    tag.putInt("hOffset", hOffset);
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    timer--;
    IEnergyStorage en = this.energy.orElse(null);
    IItemHandler inv = this.inventory.orElse(null);
    if (en == null || inv == null
        || en.getEnergyStored() < ConfigManager.DROPPERPOWER.get()) {
      setLitProperty(false);
      return;//out of energy so keep it rendered as off
    }
    setLitProperty(true);
    if (timer > 0) {
      return;
    }
    timer = delay;
    ItemStack dropMe = inv.getStackInSlot(0).copy();
    BlockPos target = getTargetPos();
    int amtDrop = Math.min(this.dropCount, dropMe.getCount());
    if (amtDrop > 0) {
      en.extractEnergy(ConfigManager.DROPPERPOWER.get(), false);
      dropMe.setCount(amtDrop);
      UtilItemStack.dropItemStackMotionless(world, target, dropMe);
      inv.getStackInSlot(0).shrink(amtDrop);
    } //      this.decrStackSize(slotCurrent, amtDrop);
  }

  private BlockPos getTargetPos() {
    BlockPos target = this.getCurrentFacingPos().offset(this.getCurrentFacing(), hOffset);
    return target;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case DELAY:
        return this.delay;
      case DROPCOUNT:
        return this.dropCount;
      case OFFSET:
        return this.hOffset;
      case RENDER:
        return render;
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
        this.needsRedstone = value % 2;
      break;
      case DELAY:
        delay = Math.max(0, value);
      break;
      case DROPCOUNT:
        dropCount = Math.max(1, value);
      break;
      case OFFSET:
        hOffset = Math.max(0, value);
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<>();
    shape.add(getTargetPos());
    return shape;
  }
}
