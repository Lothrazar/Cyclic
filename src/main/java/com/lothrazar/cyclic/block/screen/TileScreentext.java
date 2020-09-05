package com.lothrazar.cyclic.block.screen;

import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TileScreentext extends TileEntityBase implements INamedContainerProvider {

  public static final int STRINGS = 4;
  private String[] text = new String[STRINGS];
  int red = 100;
  int green = 100;
  int blue = 100;
  int padding = 0;
  int fontSize = 1;
  int offset = 0;

  public static enum Fields {
    REDSTONE, RED, GREEN, BLUE, PADDING, FONT, OFFSET;
  }

  public TileScreentext() {
    super(TileRegistry.screen);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerScreentext(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tags) {
    text = new String[STRINGS];
    for (int i = 0; i < STRINGS; i++) {
      text[i] = tags.getString("text" + i);
    }
    tags.putInt("red", red);
    tags.putInt("green", green);
    tags.putInt("blue", blue);
    tags.putInt("padding", padding);
    tags.putInt("font", fontSize);
    tags.putInt("offset", offset);
    super.read(bs, tags);
  }

  @Override
  public CompoundNBT write(CompoundNBT tags) {
    for (int i = 0; i < STRINGS; i++) {
      if (text[i] != null) {
        tags.putString("text" + i, text[i]);
      }
    }
    red = tags.getInt("red");
    green = tags.getInt("green");
    blue = tags.getInt("blue");
    padding = tags.getInt("padding");
    fontSize = tags.getInt("font");
    offset = tags.getInt("offset");
    return super.write(tags);
  }

  @Override
  public void setFieldString(int field, String value) {
    text[field] = value;
    System.out.println("SAVE !!!  " + text[field]);
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
}
