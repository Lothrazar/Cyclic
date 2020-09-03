package com.lothrazar.cyclic.block.dropper;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenDropper extends ScreenBase<ContainerDropper> {

  private EnergyBar energy;
  private ButtonMachineRedstone btnRedstone;
  private TextboxInteger txtCount;
  private TextboxInteger txtDelay;
  private TextboxInteger txtOffset;

  public ScreenDropper(ContainerDropper screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileDropper.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileDropper.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    x = guiLeft + 86;
    y = guiTop + 18;
    txtCount = new TextboxInteger(this.font, x, y, 20,
        container.tile.getPos(), TileDropper.Fields.DROPCOUNT.ordinal());
    txtCount.setText("" + container.tile.getField(TileDropper.Fields.DROPCOUNT.ordinal()));
    txtCount.setTooltip(UtilChat.lang("cyclic.dropper.count"));
    this.children.add(txtCount);
    y += 22;
    txtOffset = new TextboxInteger(this.font, x, y, 20,
        container.tile.getPos(), TileDropper.Fields.OFFSET.ordinal());
    txtOffset.setText("" + container.tile.getField(TileDropper.Fields.OFFSET.ordinal()));
    txtOffset.setTooltip(UtilChat.lang("cyclic.dropper.offset"));
    this.children.add(txtOffset);
    y += 22;
    txtDelay = new TextboxInteger(this.font, x, y, 30,
        container.tile.getPos(), TileDropper.Fields.DELAY.ordinal());
    txtDelay.setMaxStringLength(3);
    txtDelay.setText("" + container.tile.getField(TileDropper.Fields.DELAY.ordinal()));
    txtDelay.setTooltip(UtilChat.lang("cyclic.dropper.delay"));
    this.children.add(txtDelay);
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  public void tick() {
    this.txtCount.tick();
    this.txtDelay.tick();
    this.txtOffset.tick();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 54, 34);
    energy.draw(ms, container.getEnergy());
    txtCount.render(ms, mouseX, mouseY, partialTicks);
    txtDelay.render(ms, mouseX, mouseY, partialTicks);
    txtOffset.render(ms, mouseX, mouseY, partialTicks);
  }
}
