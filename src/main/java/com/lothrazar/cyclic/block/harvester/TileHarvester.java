package com.lothrazar.cyclic.block.harvester;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileHarvester extends TileEntityBase implements ITickableTileEntity {

  public TileHarvester() {
    super(CyclicRegistry.harvesterTile);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
  }

  static final int MAX = 6400000;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

  @Override
  public void tick() {
    BlockPos target = UtilWorld.getRandomPos(world.rand, getPos(), 9);
    this.tryHarvestSingle(target);
    IEnergyStorage cap = this.energy.orElse(null);
    cap.extractEnergy(1, true);
  }

  private void tryHarvestSingle(BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    IntegerProperty propInt = this.getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerWorld)) {
      return;
    }
    int currentAge = blockState.get(propInt);
    int minAge = Collections.min(propInt.getAllowedValues());
    int maxAge = Collections.max(propInt.getAllowedValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return;
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
          ModCyclic.LOGGER.info("Harvester remove seed item " + drop);
          //          iterator.remove();
          //           break;
        }
        else {
          UtilWorld.dropItemStackInWorld(world, posCurrent, drop);
        }
      }
    }
    //    world.destroyBlock(posCurrent, false);
    world.setBlockState(posCurrent, blockState.with(propInt, minAge));
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
    // TODO Auto-generated method stub
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
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }
}
