package com.lothrazar.cyclic.block.miner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileMiner extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private int shapeIndex = 0;
  static final int MAX_HEIGHT = 64;
  private int height = MAX_HEIGHT / 2;
  private static final int MAX_SIZE = 12;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int size = 5;
  static final int MAX = 64000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
  private WeakReference<FakePlayer> fakePlayer;
  private List<BlockPos> shape;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private BlockPos targetPos = BlockPos.ZERO;
  private boolean directionIsUp = false;

  public enum Fields {
    REDSTONE, RENDER, SIZE, HEIGHT, DIRECTION;
  }

  public TileMiner() {
    super(TileRegistry.miner);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
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

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerMiner(i, world, pos, playerInventory, playerEntity);
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
    size = tag.getInt("size");
    height = tag.getInt("height");
    isCurrentlyMining = tag.getBoolean("isCurrentlyMining");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("size", size);
    tag.putInt("height", height);
    isCurrentlyMining = tag.getBoolean("isCurrentlyMining");
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if ((world instanceof ServerWorld) && fakePlayer == null)
      fakePlayer = setupBeforeTrigger((ServerWorld) world, "miner");
    try {
      TileEntityBase.tryEquipItem(inventory, fakePlayer, 0, Hand.MAIN_HAND);
      //TODO: does this target block match filter
      if (shape == null) {
        rebuildShape();
      }
      if (shape.size() == 0) {
        return;
      }
      setLitProperty(true);
      updateMiningProgress(shape);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Miner action error", e);
    }
  }

  private boolean updateMiningProgress(List<BlockPos> shape) {
    if (fakePlayer == null) {
      return false;
    }
    if (isCurrentlyMining == false) { //we can mine but are not currently. so try moving to a new position
      updateTargetPos(shape);
    }
    if (isTargetValid()) { //if target is valid, allow mining (no air, no blacklist, etc)
      isCurrentlyMining = true;
    }
    else { // no valid target, back out
      updateTargetPos(shape);
      resetProgress();
    }
    //currentlyMining may have changed, and we are still turned on:
    if (isCurrentlyMining) {
      BlockState targetState = world.getBlockState(targetPos);
      float relative = targetState.getPlayerRelativeBlockHardness(fakePlayer.get(), world, targetPos);
      //state.getPlayerRelativeBlockHardness(player, worldIn, pos);UtilItemStack.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), world, targetPos);
      curBlockDamage += relative;
      //
      ModCyclic.LOGGER.info(world.getBlockState(targetPos) + " progress ? " + targetPos + "  relative = " + relative + "    curBlockDamage=" + curBlockDamage);
      //if hardness is relative, jus fekin break it like air eh
      if (curBlockDamage >= 1.0f || relative == 0) {
        boolean harvested = fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
        if (!harvested) {
          //            world.destroyBlock(targetPos, true, fakePlayer.get()); 
          harvested = world.getBlockState(targetPos)
              .removedByPlayer(world, pos, fakePlayer.get(), true, world.getFluidState(pos));
          //          ModCyclic.LOGGER.info(" hacked ? " + targetPos + " removed");
        }
        if (harvested) {
          // success 
          resetProgress();
        }
        else {
          ModCyclic.LOGGER.info(" FAILED ? " + targetPos + " removed");
          //("CANCEL and or cant harvest" + targetPos);
          world.sendBlockBreakProgress(fakePlayer.get().getUniqueID().hashCode(),
              targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
    else {//is mining is false
      world.sendBlockBreakProgress(fakePlayer.get().getUniqueID().hashCode(),
          targetPos, (int) (curBlockDamage * 10.0F) - 1);
    }
    return false;
  }

  private boolean isTargetValid() {
    return targetPos != null && !world.isAirBlock(targetPos)
        && world.getBlockState(targetPos).hardness >= 0
        && world.getFluidState(targetPos) == null;
  }

  private void updateTargetPos(List<BlockPos> shape) {
    shapeIndex++;
    if (this.shapeIndex < 0 || this.shapeIndex >= shape.size()) {
      this.shapeIndex = 0;
    }
    targetPos = shape.get(shapeIndex);
  }

  private void resetProgress() {
    isCurrentlyMining = false;
    curBlockDamage = 0;
    if (fakePlayer != null && targetPos != null) {
      //BlockPos targetPos = pos.offset(state.getValue(BlockMiner.PROPERTYFACING));
      getWorld().sendBlockBreakProgress(fakePlayer.get().getUniqueID().hashCode(), targetPos, -1);
    }
  }

  private void rebuildShape() {
    resetProgress();
    shape = this.getShape();
    shapeIndex = 0;
    targetPos = null;
  }

  public List<BlockPos> getShape() {
    List<BlockPos> shape = new ArrayList<BlockPos>();
    shape = UtilShape.squareHorizontalFull(this.getCurrentFacingPos(size + 1), size);
    int diff = directionIsUp ? 1 : -1;
    //    System.out.println("diff" + (diff * height));
    if (height > 0) {
      shape = UtilShape.repeatShapeByHeight(shape, diff * height);
    }
    return shape;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case DIRECTION:
        return (directionIsUp) ? 1 : 0;
      case HEIGHT:
        return height;
      case SIZE:
        return size;
      default:
      break;
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
      case DIRECTION:
        this.directionIsUp = value == 1;
      break;
      case HEIGHT:
        height = Math.min(value, MAX_HEIGHT);
      break;
      case SIZE:
        size = Math.min(value, MAX_SIZE);
      break;
      default:
      break;
    }
  }
}
