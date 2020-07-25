package com.lothrazar.cyclic.block.shapebuilder;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
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
  public ButtonMachine btnRedstone;
  private List<ButtonMachine> shapeBtuns;
  private ButtonMachine btnRender;

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
    btnRedstone = addButton(new ButtonMachine(x, y, 20, 20, "", (p) -> {
      container.tile.setNeedsRedstone((container.tile.getNeedsRedstone() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileStructure.Fields.REDSTONE.ordinal(), container.tile.getNeedsRedstone(), container.tile.getPos()));
    }));
    btnRender = addButton(new ButtonMachine(x + 20, y, 20, 20, "", (p) -> {
      int f = TileStructure.Fields.RENDER.ordinal();
      container.tile.setField(f, (container.tile.getField(f) + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, container.tile.getField(f), container.tile.getPos()));
    }));
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
  //  @Override
  //  public void removed() {
  //    this.txtHeight = null;
  //    this.txtSize = null;
  //  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.func_230459_a_(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.getEnergy());
  }

  @Override
  public void tick() {
    this.txtHeight.tick();
    this.txtSize.tick();
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.setTooltip(UtilChat.lang("gui.cyclic.redstone" + container.tile.getNeedsRedstone()));
    btnRedstone.setTextureId(container.tile.getNeedsRedstone() == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
    int on = container.tile.getField(TileStructure.Fields.RENDER.ordinal());
    btnRender.setTooltip(UtilChat.lang("gui.cyclic.render" + on));
    btnRender.setTextureId(on == 1 ? TextureEnum.RENDER_SHOW : TextureEnum.RENDER_HIDE);
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
