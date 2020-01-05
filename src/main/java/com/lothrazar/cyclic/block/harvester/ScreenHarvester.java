package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenHarvester extends ScreenBase<ContainerHarvester> {

  private EnergyBar energy;

  public ScreenHarvester(ContainerHarvester screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this);
    energy.max = TileHarvester.MAX;
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.drawBackground(CyclicRegistry.Textures.GUI);
    //energy
    energy.renderEnergy(container.getEnergy());
  }
}
