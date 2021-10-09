package com.lothrazar.cyclic.block.workbench;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@SuppressWarnings("unchecked")
public class TileWorkbench extends TileEntityBase implements MenuProvider {

  private LazyOptional<IItemHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(9));
  private LazyOptional<IItemHandler> output = LazyOptional.of(() -> new ItemStackHandler(1));

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerWorkbench(i, level, worldPosition, playerInventory, playerEntity);
  }

  public enum ItemHandlers {
    GRID, OUTPUT
  }

  ;

  public TileWorkbench(BlockPos pos, BlockState state) {
    super(TileRegistry.workbench, pos, state);
  }

  protected <T> LazyOptional<T> getCapability(Capability<T> cap, ItemHandlers handler) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (handler == ItemHandlers.GRID) {
        return inventory.cast();
      }
      else if (handler == ItemHandlers.OUTPUT) {
        return output.cast();
      }
    }
    return super.getCapability(cap, Direction.NORTH);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundTag>) h).deserializeNBT(tag.getCompound("inv")));
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    inventory.ifPresent(h -> {
      CompoundTag compound = ((INBTSerializable<CompoundTag>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.save(tag);
  }

  @Override
  public void setField(int field, int value) {
    //
  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
