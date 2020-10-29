package com.lothrazar.cyclic.block.shapedata;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.block.shapedata.TileShapedata.Fields;
import com.lothrazar.cyclic.block.shapedata.TileShapedata.StructCommands;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenShapedata extends ScreenBase<ContainerShapedata> {

  private ButtonMachineRedstone btnRender;

  public ScreenShapedata(ContainerShapedata screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 6;
    btnRender = addButton(new ButtonMachineRedstone(x, y, TileShapedata.Fields.RENDER.ordinal(),
        container.tile.getPos(), TextureEnum.RENDER_HIDE, TextureEnum.RENDER_SHOW, "gui.cyclic.render"));
    //
    //
    int width = 42;
    x = guiLeft + 126 - width;
    for (StructCommands shape : StructCommands.values()) {
      ButtonMachine btnShape = addButton(new ButtonMachine(x, y, width, 20,
          shape.name(), (p) -> {
            //      container.tile.setFlowing((container.getFlowing() + 1) % 2);
            PacketRegistry.INSTANCE.sendToServer(
                new PacketTileData(Fields.COMMAND.ordinal(),
                    shape.ordinal(), container.tile.getPos()));
          }));
      btnShape.setTooltip("block.cyclic.computer_shape.command");
      y += 20;
      //
      if (shape.ordinal() == 2) {
        x += width;
        y = guiTop + 6;
      }
    }
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, title.getString());
    btnRender.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 8, 28 + 18, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 8 + 18, 28, TextureRegistry.SLOT_GPS, 18);
    this.drawSlot(ms, 70, 38, TextureRegistry.SLOT_SHAPE, 18);
    //    this.drawSlot(ms, 60, 68, TextureRegistry.SLOT_SHAPE, 18);
  }
}
