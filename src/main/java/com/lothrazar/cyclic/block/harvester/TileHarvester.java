package com.lothrazar.cyclic.block.harvester;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileHarvester extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final int ENERGY_COST = 250;
  private static final int RADIUS = 9;
  private static final int ATTEMPTS_PERTICK = 16;
  static final int MAX = 640000;

  public static enum Fields {
    REDSTONE;
  }

  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  BlockPos laserTarget;
  int laserTimer;

  public TileHarvester() {
    super(CyclicRegistry.Tiles.harvesterTile);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
  }

  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    if (this.laserTimer > 0) {
      laserTimer--;
    }
    //k is zero
    for (int i = 0; i < ATTEMPTS_PERTICK; i++) {
      BlockPos target = UtilWorld.getRandomPos(world.rand, getPos(), RADIUS);
      if (this.tryHarvestSingle(target)) {
        IEnergyStorage cap = this.energy.orElse(null);
        cap.extractEnergy(ENERGY_COST, true);
        break;
      }
    }
  }

  private boolean tryHarvestSingle(BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    IntegerProperty propInt = this.getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerWorld)) {
      return false;
    }
    int currentAge = blockState.get(propInt);
    int minAge = Collections.min(propInt.getAllowedValues());
    int maxAge = Collections.max(propInt.getAllowedValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return false;
    }
    //what does the non-grown state drop
    List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, posCurrent, (TileEntity) null);
    List<ItemStack> seeds = Block.getDrops(blockState.getBlock().getDefaultState(),
        (ServerWorld) world, posCurrent, (TileEntity) null);
    //if it droped more than one thing, and seeds exist
    if (seeds.size() > 0 && drops.size() > 1) {
      ItemStack seed = seeds.get(0);
      //  if it dropped more than one ( seed and a thing)
      for (Iterator<ItemStack> iterator = drops.iterator(); iterator.hasNext();) {
        final ItemStack drop = iterator.next();
        if (drop.getItem() == seed.getItem()) { // Remove exactly one seed (consume for replanting
          drop.shrink(1);
          ModCyclic.LOGGER.info("Harvester remove seed item " + drop);
          //          iterator.remove();
          //           break;
        }
        if (drop.getCount() > 0) {
          UtilWorld.dropItemStackInWorld(world, posCurrent, drop);
        }
      }
    }
    world.setBlockState(posCurrent, blockState.with(propInt, minAge));
    laserTarget = posCurrent;
    laserTimer = 15;
    world.notifyBlockUpdate(posCurrent, getBlockState(), getBlockState(), 3);
    UtilWorld.flagUpdate(world, pos, this.getBlockState(), this.getBlockState());
    this.markDirty();
    return true;
  }

  private IntegerProperty getAgeProp(BlockState blockState) {
    for (IProperty<?> p : blockState.getProperties()) {
      //
      if (p != null && p.getName() != null
          && p instanceof IntegerProperty &&
          p.getName().equalsIgnoreCase("age")) {
        return (IntegerProperty) p;
      }
    }
    return null;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        setNeedsRedstone(value);
      break;
    }
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX / 4);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    this.laserTarget = UtilNBT.getBlockPos(tag);
    laserTimer = tag.getInt("lt");
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    if (laserTarget == null) {
      laserTarget = BlockPos.ZERO;
    }
    UtilNBT.putBlockPos(tag, laserTarget);
    tag.putInt("lt", laserTimer);
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerHarvester(i, world, pos, playerInventory, playerEntity);
  }
}
