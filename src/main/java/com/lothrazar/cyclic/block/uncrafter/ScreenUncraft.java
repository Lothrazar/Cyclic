package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenUncraft extends ScreenBase<ContainerUncraft> {

  private TexturedProgress progress;
  private EnergyBar energy;
  private ButtonMachineField btnRedstone;

  public ScreenUncraft(ContainerUncraft screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileUncraft.MAX);
    this.progress = new TexturedProgress(this.font, 58, 20, TextureRegistry.SAW);
    this.progress.max = TileUncraft.TIMER.get();
    energy.guiLeft = progress.guiLeft = leftPos;
    energy.guiTop = progress.guiTop = topPos;
    energy.visible = TileUncraft.POWERCONF.get() > 0;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileUncraft.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    if (menu.tile.getStatus() != UncraftStatusEnum.EMPTY) {
      String name = ChatUtil.lang(
          ModCyclic.MODID + ".gui.uncrafter." + menu.tile.getStatus().name().toLowerCase());
      int center = (this.getXSize() - this.font.width(name)) / 2;
      drawString(ms, name, center + 37, 24);
      //=======
      //    UncraftStatusEnum status = container.tile.getStatus();
      //    if (status != UncraftStatusEnum.EMPTY && status != UncraftStatusEnum.MATCH) {
      //      minecraft.getTextureManager().bindTexture(TextureRegistry.WIDGETS);
      //      blit(ms, 125, 15, 228, 452, 24, 24, 512, 512);
      //    }
      //  }
      //
      //  @Override
      //  protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
      //    super.renderHoveredTooltip(matrixStack, x, y);
      //    if (this.isPointInRegion(125, 15, 24, 24, x, y)) {
      //      UncraftStatusEnum status = container.tile.getStatus();
      //      if (status != UncraftStatusEnum.EMPTY && status != UncraftStatusEnum.MATCH) {
      //        TranslationTextComponent comp = new TranslationTextComponent(ModCyclic.MODID + ".gui.uncrafter." + container.tile.getStatus().name().toLowerCase());
      //        GuiUtils.drawHoveringText(matrixStack, Arrays.asList(comp), x, y, this.width, this.height, 0xFFFFFF, font);
      //      }
      //>>>>>>> 54f4445a2d7902cf4ef454efe328c9667ca5b652
    }
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, menu.tile.getEnergy());
    this.drawSlot(ms, 38, 18);
    for (int i = 0; i < 8; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, 44);
      this.drawSlot(ms, 7 + i * Const.SQ, 44 + Const.SQ);
    }
    progress.draw(ms, TileUncraft.TIMER.get() - menu.tile.getField(TileUncraft.Fields.TIMER.ordinal()));
  }
}
