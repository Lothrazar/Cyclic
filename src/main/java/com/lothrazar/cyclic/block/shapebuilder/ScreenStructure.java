package com.lothrazar.cyclic.block.shapebuilder;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenStructure extends ScreenBase<ContainerStructure> {

  private TextboxInteger txtHeight;
  private TextboxInteger txtSize;
  private EnergyBar energy;
  private List<ButtonMachine> shapeBtuns;
  public ButtonMachineRedstone btnRedstone;
  private ButtonMachineRedstone btnRender;

  public ScreenStructure(ContainerStructure screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileStructure.MAX);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileStructure.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    btnRender = addButton(new ButtonMachineRedstone(x + 20, y, TileStructure.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_SHOW, TextureEnum.RENDER_HIDE, "gui.cyclic.render"));
    txtHeight = new TextboxInteger(this.font, guiLeft + 120, guiTop + 20, 20,
        container.tile.getPos(), TileStructure.Fields.HEIGHT.ordinal());
    txtHeight.setText("" + container.tile.getField(TileStructure.Fields.HEIGHT.ordinal()));
    txtHeight.setTooltip(UtilChat.lang("buildertype.height.tooltip"));
    this.children.add(txtHeight);
    txtSize = new TextboxInteger(this.font, guiLeft + 90, guiTop + 20, 20,
        container.tile.getPos(), TileStructure.Fields.SIZE.ordinal());
    txtSize.setTooltip(UtilChat.lang("buildertype.size.tooltip"));
    txtSize.setText("" + container.tile.getField(TileStructure.Fields.SIZE.ordinal()));
    this.children.add(txtSize);
    TileStructure.Fields fld = TileStructure.Fields.BUILDTYPE;
    int numInRow = 0;
    x = this.guiLeft + 4;
    y = this.guiTop + 38;
    int bsize = 20;
    shapeBtuns = new ArrayList<>();
    for (BuildStructureType shape : BuildStructureType.values()) {
      numInRow++;
      if (numInRow == 7) {//only 6 per row fit on screen
        //so just reset x back to left side and bump up the y
        x = this.guiLeft + 4;
        y += bsize;
      }
      String shapeName = UtilChat.lang("buildertype." + shape.name().toLowerCase());
      ButtonMachine btnShape = addButton(new ButtonMachine(x, y, bsize, bsize,
          shapeName.substring(0, 2), (p) -> {
            //      container.tile.setFlowing((container.getFlowing() + 1) % 2);
            PacketRegistry.INSTANCE.sendToServer(
                new PacketTileData(fld.ordinal(),
                    shape.ordinal(), container.tile.getPos()));
          }));
      btnShape.setTooltip(shapeName);
      x += bsize;
      btnShape.setTileField(shape.ordinal());
      this.shapeBtuns.add(btnShape);
    }
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  public void tick() {
    this.txtHeight.tick();
    this.txtSize.tick();
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    btnRender.onValueUpdate(container.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    updateDisabledButtons();
  }

  private void updateDisabledButtons() {
    int shape = container.tile.getField(TileStructure.Fields.BUILDTYPE.ordinal());
    for (ButtonMachine b : this.shapeBtuns) {
      b.active = (b.getTileField() != shape);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 60, 20);
    energy.draw(ms, container.getEnergy());
    this.txtHeight.render(ms, mouseX, mouseX, partialTicks);
    this.txtSize.render(ms, mouseX, mouseX, partialTicks);
  }
}
