package com.lothrazar.cyclic.block.clock;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenClock extends ScreenBase<ContainerClock> {

  private TextboxInteger txtDuration;
  private TextboxInteger txtDelay;
  private TextboxInteger txtPower;

  public ScreenClock(ContainerClock screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    int w = 30;
    int f = TileRedstoneClock.Fields.DURATION.ordinal();
    x = guiLeft + 38;
    y = guiTop + 26;
    txtDuration = new TextboxInteger(this.font, x, y, w, container.tile.getPos(), f);
    txtDuration.setMaxStringLength(3);
    txtDuration.setText("" + container.tile.getField(f));
    txtDuration.setTooltip(UtilChat.lang("cyclic.clock.duration"));
    this.children.add(txtDuration);
    x += w + 8;
    f = TileRedstoneClock.Fields.DELAY.ordinal();
    txtDelay = new TextboxInteger(this.font, x, y, w, container.tile.getPos(), f);
    txtDelay.setMaxStringLength(3);
    txtDelay.setText("" + container.tile.getField(f));
    txtDelay.setTooltip(UtilChat.lang("cyclic.clock.delay"));
    this.children.add(txtDelay);
    x += w + 8;
    f = TileRedstoneClock.Fields.POWER.ordinal();
    txtPower = new TextboxInteger(this.font, x, y, w, container.tile.getPos(), f);
    txtPower.setText("" + container.tile.getField(f));
    txtPower.setTooltip(UtilChat.lang("cyclic.clock.power"));
    this.children.add(txtPower);
  }
  //  @Override
  //  public void removed() {
  //    this.txtDuration = null;
  //    this.txtDelay = null;
  //    this.txtPower = null;
  //  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.func_230459_a_(ms, mouseX, mouseY);//renderHoveredToolTip
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.txtDuration.render(ms, mouseX, mouseX, partialTicks);
    this.txtDelay.render(ms, mouseX, mouseX, partialTicks);
    this.txtPower.render(ms, mouseX, mouseX, partialTicks);
  }
}
