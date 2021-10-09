package com.lothrazar.cyclic.block.shapebuilder;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenStructure extends ScreenBase<ContainerStructure> {

  private EnergyBar energy;
  private List<ButtonMachine> shapeBtuns;
  public ButtonMachineField btnRedstone;
  private ButtonMachineField btnRender;

  public ScreenStructure(ContainerStructure screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileStructure.MAX);
    this.imageHeight = 256;
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    x = leftPos + 8;
    y = topPos + 8;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileStructure.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    btnRender = addRenderableWidget(new ButtonMachineField(x + 20, y, TileStructure.Fields.RENDER.ordinal(),
        menu.tile.getBlockPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    int w = 160;
    int h = 20;
    int f = TileStructure.Fields.HEIGHT.ordinal();
    x = leftPos + 8;
    y = topPos + 82;
    GuiSliderInteger durationslider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, TileStructure.MAXHEIGHT, menu.tile.getField(f)));
    durationslider.setTooltip("buildertype.height.tooltip");
    y += 21;
    f = TileStructure.Fields.SIZE.ordinal();
    GuiSliderInteger sizeslider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(),
        1, 64, menu.tile.getField(f)));
    sizeslider.setTooltip("buildertype.size.tooltip");
    //
    //
    //    txtHeight = new TextboxInteger(this.font, guiLeft + 120, guiTop + 20, 20,
    //        container.tile.getPos(), TileStructure.Fields.HEIGHT.ordinal());
    //    txtHeight.setText("" + container.tile.getField(TileStructure.Fields.HEIGHT.ordinal()));
    //    txtHeight.setTooltip(UtilChat.lang("buildertype.height.tooltip"));
    //    this.children.add(txtHeight);
    //    txtSize = new TextboxInteger(this.font, guiLeft + 90, guiTop + 20, 20,
    //        container.tile.getPos(), TileStructure.Fields.SIZE.ordinal());
    //    txtSize.setTooltip(UtilChat.lang("buildertype.size.tooltip"));
    //    txtSize.setText("" + container.tile.getField(TileStructure.Fields.SIZE.ordinal()));
    //    this.children.add(txtSize);
    TileStructure.Fields fld = TileStructure.Fields.BUILDTYPE;
    int numInRow = 0;
    x = this.leftPos + 8;
    y = this.topPos + 40;
    int bsize = 20;
    shapeBtuns = new ArrayList<>();
    for (BuildStructureType shape : BuildStructureType.values()) {
      numInRow++;
      if (numInRow == 7) {
        //only 6 per row fit on screen
        //so just reset x back to left side and bump up the y
        x = this.leftPos + 8;
        y += bsize;
      }
      String shapeName = UtilChat.lang("buildertype." + shape.name().toLowerCase());
      ButtonMachine btnShape = addRenderableWidget(new ButtonMachine(x, y, bsize, bsize,
          shapeName.substring(0, 2), (p) -> {
            //      container.tile.setFlowing((container.getFlowing() + 1) % 2);
            PacketRegistry.INSTANCE.sendToServer(
                new PacketTileData(fld.ordinal(),
                    shape.ordinal(), menu.tile.getBlockPos()));
          }));
      btnShape.setTooltip(shapeName);
      x += bsize;
      btnShape.setTileField(shape.ordinal());
      this.shapeBtuns.add(btnShape);
    }
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.getEnergy());
  }

  @Override
  protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnRender.onValueUpdate(menu.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    updateDisabledButtons();
  }

  private void updateDisabledButtons() {
    int shape = menu.tile.getField(TileStructure.Fields.BUILDTYPE.ordinal());
    for (ButtonMachine b : this.shapeBtuns) {
      b.active = (b.getTileField() != shape);
    }
  }

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE_PLAIN);
    energy.draw(ms, menu.getEnergy());
    this.drawSlot(ms, 60, 20);
    this.drawSlot(ms, 7, 131, TextureRegistry.SLOT_SHAPE, 18);
    this.drawSlot(ms, 151, 131, TextureRegistry.SLOT_GPS, 18);
  }
}
