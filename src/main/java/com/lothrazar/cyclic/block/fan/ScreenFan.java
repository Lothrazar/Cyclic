package com.lothrazar.cyclic.block.fan;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFan extends ScreenBase<ContainerFan> {

  private ButtonMachineRedstone btnRedstone;
  private TextboxInteger txtSize;
  private TextboxInteger txtRange;

  public ScreenFan(ContainerFan screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileFan.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    x = guiLeft + 46;
    y = guiTop + 22;
    txtSize = new TextboxInteger(this.font, x, y, 20,
        container.tile.getPos(), TileFan.Fields.SPEED.ordinal());
    txtSize.setText("" + container.tile.getField(TileFan.Fields.SPEED.ordinal()));
    txtSize.setTooltip(UtilChat.lang("cyclic.fan.speed"));
    this.children.add(txtSize);
    y += 30;
    txtRange = new TextboxInteger(this.font, x, y, 20,
        container.tile.getPos(), TileFan.Fields.RANGE.ordinal());
    txtRange.setText("" + container.tile.getField(TileFan.Fields.RANGE.ordinal()));
    txtRange.setTooltip(UtilChat.lang("cyclic.fan.range"));
    this.children.add(txtRange);
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    txtSize.render(ms, mouseX, mouseY, partialTicks);
    txtRange.render(ms, mouseX, mouseY, partialTicks);
  }
}
