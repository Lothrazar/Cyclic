package com.lothrazar.cyclic.block.structurereadercreative;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenReader extends ScreenBase<ContainerReader> {

  private TextBoxAutosave txtBox;

  public ScreenReader(ContainerReader screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y; //
    x = guiLeft + 8;
    y = guiTop + 58;
    txtBox = new TextBoxAutosave(this.font, x, y, 160,
        container.tile.getPos(), 0);
    txtBox.setMaxStringLength(500);
    txtBox.setText("" + container.tile.getFieldString(0));
    txtBox.setTooltip(UtilChat.lang("block.cyclic.structurename"));
    this.children.add(txtBox);
  }

  @Override
  public void removed() {
    this.txtBox = null;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawButtonTooltips(mouseX, mouseY);
    this.drawName(this.title.getFormattedText());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.INVENTORY);
    this.drawSlot(12, 18, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(12, 38, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(36, 24, TextureRegistry.SLOT_LARGE, 26);
    this.txtBox.render(mouseX, mouseX, partialTicks);
  }
}
