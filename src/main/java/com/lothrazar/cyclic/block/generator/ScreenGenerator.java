package com.lothrazar.cyclic.block.generator;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.ButtonTooltip;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenGenerator extends ContainerScreen<ContainerGenerator> {

  private ButtonTooltip btnToggle;
  private EnergyBar energy;

  public ScreenGenerator(ContainerGenerator screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this);
    energy.max = TilePeatGenerator.MENERGY;
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    int x = guiLeft + 132, y = guiTop + 8;
    btnToggle = addButton(new ButtonTooltip(x, y, 20, 20, "", (p) -> {
      container.tileEntity.setFlowing((container.getFlowing() + 1) % 2);
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(0, container.tileEntity.getFlowing(), container.tileEntity.getPos()));
    }));
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
    drawString(Minecraft.getInstance().fontRenderer, "" + container.getEnergy(), x, y, 0xffffff);
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.getFlowing()));
    btnToggle.setMessage(container.getFlowing() == 1 ? "<>" : "|");
    //    drawString(Minecraft.getInstance().fontRenderer, "Burn Time: " + container.getBurnTime(), x, y + 10, 0xffffff);
    this.drawTooltips(mouseX, mouseY);
  }

  private void drawTooltips(int mouseX, int mouseY) {
    if (this.btnToggle.isMouseOver(mouseX, mouseY)) {
      btnToggle.renderToolTip(mouseX, mouseY);
      this.renderTooltip(btnToggle.getTooltip(), mouseX - guiLeft, mouseY - guiTop);
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.GUI);
    int relX = (this.width - this.xSize) / 2;
    int relY = (this.height - this.ySize) / 2;
    this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    this.minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.SLOT);
    relX = guiLeft + 60;
    relY = guiTop + 20;
    int size = 18;
    blit(relX, relY, 0, 0, size, size, size, size);
    energy.renderEnergy(container.getEnergy());
  }
}
