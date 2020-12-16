package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileWorkbench extends TileEntityBase implements INamedContainerProvider {

  private LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(9));
  private LazyOptional<IItemHandler> output = LazyOptional.of(() -> new ItemStackHandler(1));

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerWorkbench(i, world, pos, playerInventory, playerEntity);
  }

  public enum ItemHandlers {
    GRID, OUTPUT
  };

  public TileWorkbench() {
    super(TileRegistry.workbench);
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
      return inventory.cast();
    return super.getCapability(cap, side);
  }

  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull ItemHandlers handler) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (handler == ItemHandlers.GRID)
        return inventory.cast();
      else if (handler == ItemHandlers.OUTPUT)
        return output.cast();
    }
    return super.getCapability(cap, Direction.NORTH);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void setField(int field, int value) {

  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
