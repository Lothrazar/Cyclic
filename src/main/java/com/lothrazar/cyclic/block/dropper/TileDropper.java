package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.ArrayList;
import java.util.List;
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
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileDropper extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static enum Fields {
    TIMER, REDSTONE, DROPCOUNT, DELAY, OFFSET, RENDER;
  }

  static final int MAX = 64000;
  public static IntValue POWERCONF;
  private CustomEnergyStorage energy = new CustomEnergyStorage(MAX, MAX);
  private ItemStackHandler inventory = new ItemStackHandler(1);
  private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private int dropCount = 1;
  private int delay = 10;
  private int hOffset = 0;

  public TileDropper() {
    super(TileRegistry.dropper);
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    timer--;
    final int cost = POWERCONF.get();
    if (energy.getEnergyStored() < cost) {
      if (cost > 0) {
        setLitProperty(false);
        return; //out of energy so keep it rendered as off
      }
    }
    setLitProperty(true);
    if (timer > 0) {
      return;
    }
    timer = delay;
    ItemStack dropMe = inventory.getStackInSlot(0).copy();
    BlockPos target = getTargetPos();
    int amtDrop = Math.min(this.dropCount, dropMe.getCount());
    if (amtDrop > 0) {
      energy.extractEnergy(cost, false);
      dropMe.setCount(amtDrop);
      UtilItemStack.dropItemStackMotionless(world, target, dropMe);
      inventory.getStackInSlot(0).shrink(amtDrop);
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerDropper(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energyCap.cast();
    }
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    energy.deserializeNBT(tag.getCompound(NBTENERGY));
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    this.delay = tag.getInt("delay");
    this.dropCount = tag.getInt("dropCount");
    this.hOffset = tag.getInt("hOffset");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTENERGY, energy.serializeNBT());
    tag.put(NBTINV, inventory.serializeNBT());
    tag.putInt("delay", delay);
    tag.putInt("dropCount", dropCount);
    tag.putInt("hOffset", hOffset);
    return super.write(tag);
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
