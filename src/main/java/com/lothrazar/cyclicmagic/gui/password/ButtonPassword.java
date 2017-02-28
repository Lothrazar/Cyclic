package com.lothrazar.cyclicmagic.gui.password;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTilePassword.PacketType;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiButton;

public class ButtonPassword extends GuiButton implements ITooltipButton {
  public PacketType type;
  List<String> tt = new ArrayList<String>();
  public ButtonPassword( PacketType activetype, int x, int y) {
    super(activetype.ordinal(), x, y, 40, 20, "");
    this.type = activetype;
    tt.add(UtilChat.lang("tile.password_block."+type.name().toLowerCase()+".tooltip"));
  }

  @Override
  public List<String> getTooltips() {
    return tt;
  }
}
