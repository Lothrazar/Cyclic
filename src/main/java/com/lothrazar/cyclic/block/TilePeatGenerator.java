package com.lothrazar.cyclic.block;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.fan.BlockFan;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePeatGenerator extends TileEntityBase implements ITickableTileEntity {

  private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

  public TilePeatGenerator() {
    super(CyclicRegistry.peat_generatorTile);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(100000, 0);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return handler.cast();
    }
    if (cap == CapabilityEnergy.ENERGY) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  private void setAnimation(boolean lit) {
    this.world.setBlockState(pos, this.world.getBlockState(pos).with(BlockFan.IS_LIT, lit));
  }

  @Override
  public void read(CompoundNBT tag) {
    //    radius = tag.getInt("radius");
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    CompoundNBT invTag = tag.getCompound("inv");
    handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    //    tag.putInt("radius", radius);
    handler.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world.isRemote) {
      return;
    }
    handler.ifPresent(h -> {
      ItemStack stack = h.getStackInSlot(0);
      if (stack.getItem() == Items.DIAMOND) {
        //        h.extractItem(0, 1, false);
        energy.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(1000));
        //        counter = 20;
      }
    });
    //    if (this.isPowered() == false) {
    //      setAnimation(false);
    //      return;
    //    }
    //    setAnimation(true);
    //    if (world.rand.nextDouble() < 0.3) {
    //      BlockPos target = pos.offset(this.getCurrentFacing());
    //      BlockState state = world.getBlockState(target);
    //      if (state.getBlockHardness(world, target) >= 0) {
    //        this.world.destroyBlock(target, true);
    //      }
    //      //else unbreakable
    //    }
  }
}
