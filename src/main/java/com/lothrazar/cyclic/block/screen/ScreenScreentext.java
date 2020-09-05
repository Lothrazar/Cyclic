package com.lothrazar.cyclic.block.screen;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.clock.TileRedstoneClock;
import com.lothrazar.cyclic.gui.TextBoxAutosave;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenScreentext extends ScreenBase<ContainerScreentext> {

  private TextBoxAutosave txtDuration;

  public ScreenScreentext(ContainerScreentext screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    int w = 90;
    int f = TileRedstoneClock.Fields.DURATION.ordinal();
    x = guiLeft + 38;
    y = guiTop + 26;
    txtDuration = new TextBoxAutosave(this.font, x, y, w, container.tile.getPos(), 0);
    txtDuration.setText(container.tile.getFieldString(0));
    this.children.add(txtDuration);
  }

  @Override
  public void tick() {
    this.txtDuration.tick();
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
    //energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.txtDuration.render(ms, mouseX, mouseY, partialTicks);
    //  energy.draw(ms, container.getEnergy());
  }
}
