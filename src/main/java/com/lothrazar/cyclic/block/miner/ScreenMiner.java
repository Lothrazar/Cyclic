package com.lothrazar.cyclic.block.miner;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.shapebuilder.TileStructure;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenMiner extends ScreenBase<ContainerMiner> {

  private TextboxInteger txtHeight;
  private TextboxInteger txtSize;
  private ButtonMachineRedstone btnRedstone;
  private EnergyBar energy;

  public ScreenMiner(ContainerMiner screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileMiner.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileMiner.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    //
    txtHeight = new TextboxInteger(this.font, guiLeft + 120, guiTop + 20, 20,
        container.tile.getPos(), TileStructure.Fields.HEIGHT.ordinal());
    txtHeight.setText("" + container.tile.getField(TileMiner.Fields.HEIGHT.ordinal()));
    txtHeight.setTooltip(UtilChat.lang("buildertype.height.tooltip"));
    this.children.add(txtHeight);
    txtSize = new TextboxInteger(this.font, guiLeft + 90, guiTop + 20, 20,
        container.tile.getPos(), TileStructure.Fields.SIZE.ordinal());
    txtSize.setTooltip(UtilChat.lang("buildertype.size.tooltip"));
    txtSize.setText("" + container.tile.getField(TileMiner.Fields.SIZE.ordinal()));
    this.children.add(txtSize);
  }

  @Override
  public void tick() {
    this.txtHeight.tick();
    this.txtSize.tick();
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
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
    this.drawSlot(ms, 54, 34);
    energy.draw(ms, container.tile.getEnergy());
    this.txtHeight.render(ms, mouseX, mouseX, partialTicks);
    this.txtSize.render(ms, mouseX, mouseX, partialTicks);
  }
}
