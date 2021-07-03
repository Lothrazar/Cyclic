package com.lothrazar.cyclic.block.anvilvoid;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileAnvilVoid extends TileEntityBase implements INamedContainerProvider, ITickableTileEntity {

  static enum Fields {
    TIMER, REDSTONE;
  }

  public static IntValue POWERCONF;
  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK;
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileAnvilVoid() {
    super(TileRegistry.ANVILVOID.get());
    this.needsRedstone = 1;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerAnvilVoid(i, world, pos, playerInventory, playerEntity);
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
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
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
    setLitProperty(true);
    //is output empty
    if (outputSlots.getStackInSlot(0).isEmpty() == false) {
      return;
    }
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.getItem() == Items.ENCHANTED_BOOK) {
      inputSlots.extractItem(0, 1, false);
      outputSlots.insertItem(0, new ItemStack(Items.BOOK), false);
    }
    else if (stack.getTag() != null && stack.getTag().contains("Enchantments")
        && !stack.getItem().isIn(DataTags.IMMUNE)) {
          //is enchanted
          stack.getTag().remove("Enchantments");
          outputSlots.insertItem(0, stack.copy(), false);
          inputSlots.extractItem(0, stack.getCount(), false);
        }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      default:
      break;
    }
    return 0;
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TIMER:
        this.timer = value;
      break;
    }
  }
}
