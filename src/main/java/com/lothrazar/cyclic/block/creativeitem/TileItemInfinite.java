package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemInfinite extends TileEntityBase implements TickableBlockEntity {

  public TileItemInfinite() {
    super(TileRegistry.item_infinite);
  }

  ItemStackHandler inputSlots = new ItemStackHandler(1);
  ItemStackHandler outputSlot = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlot);
  private final LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  @Override
  public void load(BlockState bs, CompoundTag tag) {
    super.load(bs, tag);
    inventory.deserializeNBT(tag.getCompound(NBTINV));
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.save(tag);
  }

  //  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public void tick() {
    ItemStack stackHere = inputSlots.getStackInSlot(0);
    if (!stackHere.isEmpty()) {
      outputSlot.insertItem(0, stackHere.copy(), false);
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
