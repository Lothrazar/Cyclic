package com.lothrazar.cyclic.block.fan;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenFan extends ScreenBase<ContainerFan> {

  private ButtonMachine btnRedstone;
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
    btnRedstone = addButton(new ButtonMachine(x, y, 20, 20, "", (p) -> {
      container.tile.setNeedsRedstone((container.tile.getNeedsRedstone() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(TileFan.Fields.REDSTONE.ordinal(), container.tile.getNeedsRedstone(), container.tile.getPos()));
    }));
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
    this.func_230459_a_(ms, mouseX, mouseY);//renderHoveredToolTip
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    btnRedstone.setTooltip(UtilChat.lang("gui.cyclic.redstone" + container.tile.getNeedsRedstone()));
    btnRedstone.setTextureId(container.tile.getNeedsRedstone() == 1 ? TextureEnum.REDSTONE_NEEDED : TextureEnum.REDSTONE_ON);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    txtSize.render(ms, mouseX, mouseY, partialTicks);
    txtRange.render(ms, mouseX, mouseY, partialTicks);
  }
}
