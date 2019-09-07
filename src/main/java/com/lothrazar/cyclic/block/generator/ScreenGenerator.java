package com.lothrazar.cyclic.block.generator;

import com.lothrazar.cyclic.ModCyclic;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenGenerator extends ContainerScreen<ContainerGenerator> {

  private ResourceLocation GUI = new ResourceLocation(ModCyclic.MODID, "textures/gui/peat_generator.png");
  private ResourceLocation SLOT = new ResourceLocation(ModCyclic.MODID, "textures/gui/inventory_slot.png");
  private ResourceLocation ENERGY_CTR = new ResourceLocation(ModCyclic.MODID, "textures/gui/energy_ctr.png");
  private ResourceLocation ENERGY_INNER = new ResourceLocation(ModCyclic.MODID, "textures/gui/energy_inner.png");

  public ScreenGenerator(ContainerGenerator screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    this.renderBackground();
    super.render(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    int x = 10, y = 50;
    drawString(Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy(), x, y, 0xffffff);
    drawString(Minecraft.getInstance().fontRenderer, "Burn Time: " + container.getBurnTime(), x, y + 10, 0xffffff);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(GUI);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    this.minecraft.getTextureManager().bindTexture(SLOT);
    relX = guiLeft + 60;
    relY = guiTop + 20;
    int size = 18;
    blit(relX, relY, 0, 0, size, size, size, size);
    this.minecraft.getTextureManager().bindTexture(ENERGY_CTR);
    relX = guiLeft + 154;
    relY = guiTop + 8;
    blit(relX, relY, 0, 0, 16, 66, 16, 78);
    this.minecraft.getTextureManager().bindTexture(ENERGY_INNER);
    relX = relX + 1;
    relY = relY + 1;
    float energ = container.getEnergy();
    float pct = Math.min(energ / TilePeatGenerator.MENERGY, 1.0F);
    blit(relX, relY, 0, 0, 14, (int) (64 * pct), 14, 64);
  }
}
