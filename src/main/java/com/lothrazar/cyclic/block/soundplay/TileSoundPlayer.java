package com.lothrazar.cyclic.block.soundplay;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.item.datacard.SoundCard;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSoundPlayer extends TileBlockEntityCyclic implements MenuProvider {

  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.SOUND_DATA.get();
    }
  };
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  public TileSoundPlayer(BlockPos pos, BlockState state) {
    super(TileRegistry.SOUND_PLAYER.get(), pos, state);
  }

  public void tryPlaySound() {
    ItemStack card = this.inventory.getStackInSlot(0);
    if (card.hasTag() && card.getTag().contains(SoundCard.SOUND_ID) && level instanceof ServerLevel) {
      String sid = card.getTag().getString(SoundCard.SOUND_ID);
      SoundUtil.playSoundFromServerById((ServerLevel) level, worldPosition, sid);
    }
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerSoundPlayer(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    super.saveAdditional(tag);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
