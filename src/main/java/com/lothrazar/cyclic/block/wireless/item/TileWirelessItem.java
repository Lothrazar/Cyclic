package com.lothrazar.cyclic.block.wireless.item;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.BlockState;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileWirelessItem extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static final int SLOT_GPS = 0;
  static final int SLOT_ITEMS = 1;

  static enum Fields {
    RENDER, TRANSFER_RATE, REDSTONE;
  }

  public TileWirelessItem() {
    super(TileRegistry.WIRELESS_ITEM.get());
    this.needsRedstone = 0;
  }

  private int transferRate = 1;
  ItemStackHandler inventory = new ItemStackHandler(2) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return (slot == SLOT_ITEMS) || stack.getItem() instanceof LocationGpsCard;
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerWirelessItem(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    this.transferRate = tag.getInt("transferRate");
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.putInt("transferRate", transferRate);
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    if (world.isRemote) {
      return;
    }
    boolean moved = false;
    //run the transfer. one slot only
    BlockPosDim loc = getTargetInSlot(SLOT_GPS);
    if (loc != null && UtilWorld.dimensionIsEqual(loc, world)) {
      moved = moveItems(Direction.UP, loc.getPos(), this.transferRate, this.inventory, SLOT_ITEMS);
    }
    this.setLitProperty(moved);
  }

  BlockPosDim getTargetInSlot(int s) {
    return LocationGpsCard.getPosition(inventory.getStackInSlot(s));
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
      case TRANSFER_RATE:
        transferRate = value;
      break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
      case TRANSFER_RATE:
        return this.transferRate;
    }
    return 0;
  }

  public float getRed() {
    return 0.89F;
  }

  public float getBlue() {
    return 0;
  }

  public float getGreen() {
    return 0.12F;
  }

  public float getAlpha() {
    return 0.9F;
  }

  public float getThick() {
    return 0.065F;
  }
}
