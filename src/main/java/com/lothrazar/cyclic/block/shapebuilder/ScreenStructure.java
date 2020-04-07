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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenStructure extends ScreenBase<ContainerStructure> {

  private TextboxInteger txtHeight;
  private TextboxInteger txtSize;
  private EnergyBar energy;
  private ButtonMachine btnRedstone;
  private List<ButtonMachine> shapeBtuns;

  public ScreenStructure(ContainerStructure screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this);
    energy.max = TileStructure.MAX;
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
      container.tile.setNeedsRedstone((container.getNeedsRedstone() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileStructure.Fields.REDSTONE.ordinal(), container.tile.getNeedsRedstone(), container.tile.getPos()));
    }));
    this.txtHeight = new TextboxInteger(this.font, guiLeft + 120, guiTop + 20, 20,
        container.tile.getPos(), TileStructure.Fields.HEIGHT.ordinal());
    //    this.txtHeight.setFocused2(true);
    this.children.add(txtHeight);
    this.txtSize = new TextboxInteger(this.font, guiLeft + 120, guiTop + 45, 20,
        container.tile.getPos(), TileStructure.Fields.SIZE.ordinal());
    this.children.add(txtSize);
    txtHeight.setText("" + container.tile.getField(TileStructure.Fields.HEIGHT.ordinal()));
    txtSize.setText("" + container.tile.getField(TileStructure.Fields.SIZE.ordinal()));
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
  public void removed() {
    this.txtHeight = null;
    this.txtSize = null;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    energy.renderHoveredToolTip(mouseX, mouseY, container.getEnergy());
    this.txtHeight.render(mouseX, mouseX, partialTicks);
    this.txtSize.render(mouseX, mouseX, partialTicks);
  }

  @Override
  public void tick() {
    this.txtHeight.tick();
    this.txtSize.tick();
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnRedstone.setTooltip(UtilChat.lang("gui.cyclic.redstone" + container.getNeedsRedstone()));
    btnRedstone.setTextureId(container.getNeedsRedstone() == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
    this.drawButtonTooltips(mouseX, mouseY);
    updateDisabledButtons();
  }

  private void updateDisabledButtons() {
    int shape = container.tile.getField(TileStructure.Fields.BUILDTYPE.ordinal());
    for (ButtonMachine b : this.shapeBtuns) {
      b.active = (b.getTileField() != shape);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(TextureRegistry.GUI);
    this.drawSlot(60, 20);
    energy.renderEnergy(container.getEnergy());
  }
}
