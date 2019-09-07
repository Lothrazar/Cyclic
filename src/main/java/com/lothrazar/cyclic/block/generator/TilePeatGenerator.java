package com.lothrazar.cyclic.block.generator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.CustomEnergyStorage;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.fan.BlockFan;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TilePeatGenerator extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  private int burnTime;

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
    burnTime = tag.getInt("burnTime");
    CompoundNBT energyTag = tag.getCompound("energy");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
    CompoundNBT invTag = tag.getCompound("inv");
    handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("burnTime", burnTime);
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

  private boolean isBurning() {
    return this.burnTime > 0;
  }

  @Override
  public void tick() {
    if (this.burnTime == 1) {//about to be zero and refil
      energy.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(1000));
    }
    if (this.isBurning()) {
      --this.burnTime;
    }
    handler.ifPresent(h -> {
      ItemStack stack = h.getStackInSlot(0);
      if (stack.getItem() == CyclicRegistry.peat_fuel &&
          this.isBurning() == false) {
        //burn time
        h.extractItem(0, 1, false);
        this.burnTime = 200;
      }
    });
  }

  public int getBurnTime() {
    return this.burnTime;
  }

  public void setBurnTime(int value) {
    burnTime = value;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerGenerator(i, world, pos, playerInventory, playerEntity);
  }
}
