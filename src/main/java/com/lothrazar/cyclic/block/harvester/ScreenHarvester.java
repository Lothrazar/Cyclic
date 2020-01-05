package com.lothrazar.cyclic.block.harvester;

import com.lothrazar.cyclic.CyclicRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenHarvester extends ContainerScreen<ContainerHarvester> {

  public ScreenHarvester(ContainerHarvester screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
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
    renderEnergy();
  }

  private void renderEnergy() {
    int relX;
    int relY;
    this.minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.ENERGY_CTR);
    relX = guiLeft + 154;
    relY = guiTop + 8;
    blit(relX, relY, 0, 0, 16, 66, 16, 78);
    this.minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.ENERGY_INNER);
    relX = relX + 1;
    relY = relY + 1;
    float energ = container.getEnergy();
    float pct = Math.min(energ / TileHarvester.MAX, 1.0F);
    blit(relX, relY, 0, 0, 14, (int) (64 * pct), 14, 64);
  }
}
