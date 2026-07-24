package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.HolderLookup;

public class TileScreentext extends TileBlockEntityCyclic implements MenuProvider {

  public static final int STRINGS = 4;
  private String[] text = new String[STRINGS];
  int red = 255;
  int green = 255;
  int blue = 255;
  int padding = 0;
  int fontSize = 1;
  int offset = 0;
  private boolean dropShadow;

  static enum Fields {
    REDSTONE, RED, GREEN, BLUE, PADDING, FONT, OFFSET;
  }

  public TileScreentext(BlockPos pos, BlockState state) {
    super(TileRegistry.SCREEN.get(), pos, state);
    this.needsRedstone = 0;
  }

  public int getColor() {
    return ((red & 0xFF) << 16) | //red
        ((green & 0xFF) << 8) | //green
        ((blue & 0xFF) << 0);
  }


  @Override
  public Component getDisplayName() {
    return BlockRegistry.SCREEN.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerScreentext(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(CompoundTag tags, HolderLookup.Provider registries) {
    text = new String[STRINGS];
    for (int i = 0; i < STRINGS; i++) {
      text[i] = tags.getStringOr("text" + i, "");
    }
    red = tags.getIntOr("red", 0);
    green = tags.getIntOr("green", 0);
    blue = tags.getIntOr("blue", 0);
    padding = tags.getIntOr("padding", 0);
    fontSize = tags.getIntOr("font", 0);
    offset = tags.getIntOr("offset", 0);
    dropShadow = tags.getBooleanOr("dropShadow", false);
    super.loadAdditional(tags, registries);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    for (int i = 0; i < STRINGS; i++) {
      if (text[i] != null) {
        tag.putString("text" + i, text[i]);
      }
    }
    tag.putInt("red", red);
    tag.putInt("green", green);
    tag.putInt("blue", blue);
    tag.putInt("padding", padding);
    tag.putInt("font", fontSize);
    tag.putInt("offset", offset);
    tag.putBoolean("dropShadow", dropShadow);
    super.saveAdditional(tag, registries);
  }

  @Override
  public void setFieldString(int field, String value) {
    text[field] = value;
  }

  @Override
  public String getFieldString(int field) {
    return text[field];
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case BLUE:
        return blue;
      case GREEN:
        return green;
      case RED:
        return red;
      case PADDING:
        return this.padding;
      case FONT:
        return this.fontSize;
      case OFFSET:
        return offset;
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case BLUE:
        blue = value;
      break;
      case GREEN:
        green = value;
      break;
      case RED:
        red = value;
      break;
      case PADDING:
        padding = value;
      break;
      case FONT:
        fontSize = value;
      break;
      case OFFSET:
        offset = value;
      break;
      case REDSTONE:
        this.setNeedsRedstone(value);
      break;
    }
  }

  public boolean getDropShadow() {
    return this.dropShadow;
  }
}
