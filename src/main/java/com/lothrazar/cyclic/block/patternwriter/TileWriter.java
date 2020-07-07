package com.lothrazar.cyclic.block.patternwriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.item.StructureDiskItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWriter extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  public static enum Fields {
    REDSTONE, STATUS;
  }

  /**
   * NONE : no item disk
   * 
   * INVALID : disk item but schematic not found
   * 
   * VALID : found and can build (show ingredients)
   */
  enum StructureStatus {
    NONE, INVALID, VALID;
  }

  public StructureStatus structStatus;

  public TileWriter() {
    super(BlockRegistry.Tiles.structure_writer);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(2 * 9 + 1) {

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == 0)
          return stack.getItem() instanceof StructureDiskItem;
        return true;
      }
    };
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerWriter(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    structStatus = StructureStatus.values()[tag.getInt("struct_status")];
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("struct_status", structStatus.ordinal());
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    inventory.ifPresent(inv -> {
      ItemStack disk = inv.getStackInSlot(0);
      if (!disk.isEmpty() && disk.getItem() instanceof StructureDiskItem) {
        this.structStatus = StructureStatus.INVALID;
        this.tryLoad(disk);
      }
      else {
        this.structStatus = StructureStatus.NONE;
      }
    });
    //
  }

  private void tryLoad(ItemStack disk) {
    ResourceLocation location = StructureDiskItem.readDisk(disk);
    if (location != null) {
      this.structStatus = StructureStatus.VALID;
      ModCyclic.LOGGER.info("found " + location);
      this.build(location);
    }
  }

  private void build(ResourceLocation location) {
    // TODO Auto-generated method stub
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
      case STATUS:
        structStatus = StructureStatus.values()[value];
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.getNeedsRedstone();
      case STATUS:
        return this.structStatus.ordinal();
    }
    return 0;
  }
}
