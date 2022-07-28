package com.lothrazar.cyclic.block.soundrecord;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.capabilities.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.item.datacard.SoundCard;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

public class TileSoundRecorder extends TileBlockEntityCyclic implements MenuProvider {

  static final int MAX_SOUNDS = 10; // locked by gui size. 
  private static final String SOUNDAT = "soundat";
  private static final String IGNORED = "ignored";

  static enum Fields {
    CLEARALL, IGNORE, SAVE;
  }

  ItemStackHandler inputSlots = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.SOUND_DATA.get();
    }
  };
  ItemStackHandler outputSlots = new ItemStackHandler(1);
  private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(inputSlots, outputSlots);
  private LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);
  private NonNullList<String> sounds = NonNullList.withSize(MAX_SOUNDS, "");
  private List<String> ignored = new ArrayList<>();

  public TileSoundRecorder(BlockPos pos, BlockState state) {
    super(TileRegistry.SOUND_RECORDER.get(), pos, state);
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.SOUND_RECORDER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerSoundRecorder(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    for (int i = 0; i < MAX_SOUNDS; i++) {
      if (tag.contains(SOUNDAT + i)) {
        sounds.set(i, tag.getString(SOUNDAT + i));
      }
    }
    for (int i = 0; i < MAX_SOUNDS * 100; i++) {
      if (tag.contains(IGNORED + i)) {
        ignored.add(tag.getString(IGNORED + i));
      }
    }
    super.load(tag);
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    for (int i = 0; i < MAX_SOUNDS; i++) {
      tag.putString(SOUNDAT + i, sounds.get(i));
    }
    for (int i = 0; i < ignored.size(); i++) {
      tag.putString(IGNORED + i, ignored.get(i));
    }
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
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case CLEARALL:
        this.clearSounds();
      break;
      case IGNORE:
        this.ignoreSound(value);
      break;
      case SAVE:
        this.saveSoundToCard(value);
      break;
    }
  }

  private void saveSoundToCard(int value) {
    String igme = sounds.get(value);
    if (!inputSlots.getStackInSlot(0).isEmpty()) {
      SoundCard.saveSound(inputSlots.getStackInSlot(0), igme);
    }
  }

  public void ignoreSound(int value) {
    String igme = sounds.get(value);
    if (!ignored.contains(igme)) {
      ignored.add(igme);
    }
    sounds.set(value, "");
  }

  @Override
  public void setFieldString(int field, String value) {
    if (field < MAX_SOUNDS) {
      sounds.set(field, value);
    }
  }

  @Override
  public String getFieldString(int field) {
    if (field < MAX_SOUNDS) {
      return sounds.get(field);
    }
    return "";
  }

  @Override
  public int getField(int field) {
    return 0;
  }

  public void clearSounds() {
    sounds = NonNullList.withSize(MAX_SOUNDS, "");
  }

  public boolean onSoundHeard(String soundIn) {
    if (soundIn != null && !sounds.contains(soundIn) && !ignored.contains(soundIn)) {
      int found = -1; //hacky
      for (int i = 0; i < MAX_SOUNDS; i++) {
        if (sounds.get(i).isEmpty()) {
          found = i;
          break;
        }
      }
      if (found > -1) {
        sounds.set(found, soundIn);
        return true;
      }
    }
    return false;
  }
}
