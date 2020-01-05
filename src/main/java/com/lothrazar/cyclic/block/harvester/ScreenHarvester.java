package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenHarvester extends ContainerScreen<ContainerHarvester> {

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
    this.minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.GUI);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    //energy
    energy.renderEnergy(container.getEnergy());
  }
}
