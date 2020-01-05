package com.lothrazar.cyclic.block.generator;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.ButtonTooltip;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenGenerator extends ScreenBase<ContainerGenerator> {

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
    energy.renderHoveredToolTip(mouseX, mouseY, container.getEnergy());
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.getFlowing()));
    //    btnToggle.setMessage(container.getFlowing() == 1 ? "<>" : "|");
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
    //    GlStateManager.color4f(1.0F,  1.0F, 1.0F, 1.0F);
    this.drawBackground(CyclicRegistry.Textures.GUI);
    this.drawSlot(60, 20);
    energy.renderEnergy(container.getEnergy());
  }
}
