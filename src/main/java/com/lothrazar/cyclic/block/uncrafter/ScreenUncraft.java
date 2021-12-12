package com.lothrazar.cyclic.block.uncrafter;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TexturedProgress;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Collections;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class ScreenUncraft extends ScreenBase<ContainerUncraft> {

  private TexturedProgress progress;
  private EnergyBar energy;
  private ButtonMachineField btnRedstone;

  public ScreenUncraft(ContainerUncraft screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileUncraft.MAX);
    this.progress = new TexturedProgress(this, 58, 20, TextureRegistry.SAW);
    this.progress.max = TileUncraft.TIMER.get();
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = progress.guiLeft = guiLeft;
    energy.guiTop = progress.guiTop = guiTop;
    energy.visible = TileUncraft.POWERCONF.get() > 0;
    int x, y;
    x = guiLeft + 6;
    y = guiTop + 6;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileUncraft.Fields.REDSTONE.ordinal(), container.tile.getPos()));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    UncraftStatusEnum status = container.tile.getStatus();
    if (status != UncraftStatusEnum.EMPTY && status != UncraftStatusEnum.MATCH) {
      minecraft.getTextureManager().bindTexture(TextureRegistry.WIDGETS);
      blit(ms, 125, 15, 228, 452, 24, 24, 512, 512);
    }
  }

  @Override
  protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
    super.renderHoveredTooltip(matrixStack, x, y);
    if (this.isPointInRegion(125, 15, 24, 24, x, y)) {
      UncraftStatusEnum status = container.tile.getStatus();
      if (status != UncraftStatusEnum.EMPTY && status != UncraftStatusEnum.MATCH) {
        TranslationTextComponent comp = new TranslationTextComponent(ModCyclic.MODID + ".gui.uncrafter." + container.tile.getStatus().name().toLowerCase());
        GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(comp), x, y, this.width, this.height, 0xFFFFFF, font);
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
    this.drawSlot(ms, 38, 18);
    for (int i = 0; i < 8; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, 44);
      this.drawSlot(ms, 7 + i * Const.SQ, 44 + Const.SQ);
    }
    progress.draw(ms, TileUncraft.TIMER.get() - container.tile.getField(TileUncraft.Fields.TIMER.ordinal()));
  }
}
