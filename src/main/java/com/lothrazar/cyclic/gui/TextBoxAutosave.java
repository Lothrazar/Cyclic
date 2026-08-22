package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.net.PacketTileString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class TextBoxAutosave extends EditBox {

  private static final int KEY_DELETE = 261;
  private static final int KEY_BACKSPACE = 259;
  private BlockPos pos;
  private TileBlockEntityCyclic tile;

  public TextBoxAutosave(Font fontIn, int xIn, int yIn, int widthIn, BlockPos pos, int field) {
    super(fontIn, xIn, yIn, widthIn, 16, null);
    this.setBordered(true);
    this.setVisible(true);
    this.setTextColor(16777215);
    this.pos = pos;
    this.tileFieldId = field;
    this.tile = (TileBlockEntityCyclic) Minecraft.getInstance().level.getBlockEntity(pos);
  }

  @Override
  public void setFocused(boolean onFocusedChanged) {
    super.setFocused(onFocusedChanged);
    saveValue();
  }

  @Override
  public boolean charTyped(CharacterEvent event) {
    boolean worked = super.charTyped(event);
    if (worked) {
      saveValue();
    }
    return worked;
  }

  @Override
  public boolean keyPressed(KeyEvent event) {
    boolean spr = super.keyPressed(event);
    int keyCode = event.key();
    if (keyCode == KEY_BACKSPACE || keyCode == KEY_DELETE) {
      saveValue();
    }
    return spr;
  }

  private void saveValue() {
    String current = getValue();
    tile.setFieldString(tileFieldId, current);
    ClientPacketDistributor.sendToServer(new PacketTileString(this.tileFieldId, current, pos));
  }

  private int tileFieldId;
  private List<Component> tooltip;

  public List<Component> getTooltips() {
    return tooltip;
  }

  public void setTooltip(String tt) {
    if (tooltip == null) {
      tooltip = new ArrayList<>();
    }
    this.tooltip.add(Component.translatable(tt));
  }
}
