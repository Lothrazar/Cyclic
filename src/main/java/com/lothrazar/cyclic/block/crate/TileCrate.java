package com.lothrazar.cyclic.block.crate;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileCrate extends TileBlockEntityCyclic implements MenuProvider {

  ItemStackHandler inventory = new ItemStackHandler(9 * 9);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileCrate(BlockPos pos, BlockState state) {
    super(TileRegistry.CRATE.get(), pos, state);
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerCrate(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) { // YES BP
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    System.out.println("fff LOAD " + tag);
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) { // YES BP
    super.saveAdditional(tag);
    tag.put(NBTINV, inventory.serializeNBT());
    System.out.println("fff SAVE !!!!  " + inventory.serializeNBT());
    System.out.println("fff SAVE !!!!  " + tag);
  }

//  @Override
//  public void onDataPacket(net.minecraft.network.Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
//    this.load(pkt.getTag());
//    super.onDataPacket(net, pkt);
//  }
//
//  @Override
//  public ClientboundBlockEntityDataPacket getUpdatePacket() {
//    return ClientboundBlockEntityDataPacket.create(this);
//  }
//
//  @Override
//  public CompoundTag getUpdateTag() { // YES BP
//    CompoundTag syncData = super.getUpdateTag();
//    this.saveAdditional(syncData);
//    System.out.println("fff getUpdateTag base tile entity " + syncData);
//    return syncData;
//  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
