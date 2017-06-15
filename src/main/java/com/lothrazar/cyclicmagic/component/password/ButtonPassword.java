package com.lothrazar.cyclicmagic.component.password;
import com.lothrazar.cyclicmagic.component.password.PacketTilePassword.PacketType;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;

public class ButtonPassword extends GuiButtonTooltip {
  public PacketType type;
  public ButtonPassword(PacketType activetype, int x, int y) {
    super(activetype.ordinal(), x, y, 80, 20, "");
    this.type = activetype;
    setTooltip("tile.password_block." + type.name().toLowerCase() + ".tooltip");
  }
}
