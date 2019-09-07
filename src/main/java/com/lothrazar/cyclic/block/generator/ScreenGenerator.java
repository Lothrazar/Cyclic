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
    drawString(Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy(), 10, 10, 0xffffff);
    drawString(Minecraft.getInstance().fontRenderer, "Burn Time: " + container.getBurnTime(), 10, 30, 0xffffff);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(GUI);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    this.minecraft.getTextureManager().bindTexture(SLOT);
    //    relX = guiLeft + 30;
    relX = guiLeft + 60;
    relY = guiTop + 20;
    blit(relX, relY, 0, 0, 18, 18, 18, 18);
  }
}
