package com.lothrazar.cyclic.block.soundrecord;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.ItemStackHandlerWrapper;
import com.lothrazar.cyclic.item.datacard.SoundCard;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileSoundRecorder extends TileEntityBase implements INamedContainerProvider {

  static final int MAX_SOUNDS = 10; // locked by gui size. TODO: scrollbar
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

  public TileSoundRecorder() {
    super(TileRegistry.SOUND_RECORDER.get());
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerSoundRecorder(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
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
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    for (int i = 0; i < MAX_SOUNDS; i++) {
      tag.putString(SOUNDAT + i, sounds.get(i));
    }
    for (int i = 0; i < ignored.size(); i++) {
      tag.putString(IGNORED + i, ignored.get(i));
    }
    return super.write(tag);
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
    else {
      ModCyclic.LOGGER.error("Invalid string " + field + value);
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
