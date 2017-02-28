package com.lothrazar.cyclicmagic.gui.password;
import com.lothrazar.cyclicmagic.net.PacketTilePassword.PacketType;
import net.minecraft.client.gui.GuiButton;

public class ButtonPassword extends GuiButton {
  public PacketType type;

  public ButtonPassword( PacketType activetype, int x, int y) {
    super(activetype.ordinal(), x, y, 40, 20, "");
    this.type = activetype;
  }
}
