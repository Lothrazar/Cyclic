package com.lothrazar.cyclic.block.structurereadercreative;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
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
  //  @Override
  //  public void removed() {
  //    this.txtBox = null;
  //  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.func_230459_a_(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 12, 18, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 12, 38, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 36, 24, TextureRegistry.SLOT_LARGE, 26);
    this.txtBox.render(ms, mouseX, mouseX, partialTicks);
  }
}
