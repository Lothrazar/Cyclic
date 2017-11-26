package com.lothrazar.cyclicmagic.component.playerext.storage;
import com.lothrazar.cyclicmagic.component.playerext.ButtonToggleHotbar;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.ResourceLocation;

public class GuiPlayerExtended extends InventoryEffectRenderer {
  public static final ResourceLocation background = new ResourceLocation(Const.MODID, "textures/gui/inventory.png");
  public GuiPlayerExtended(ContainerPlayerExtended ctr) {
    super(ctr);
    this.allowUserInput = true;
  }
  @Override
  public void updateScreen() {
    this.updateActivePotionEffects();
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 7, x = this.guiLeft + 168, y = this.guiTop + Const.PAD - 1, w = Const.PAD, h = Const.SQ, row = 1;
    ButtonToggleHotbar btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    row++;
    id++;
    y += Const.SQ;
    btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    row++;
    id++;
    y += Const.SQ;
    btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    row++;
    id++;
    y += Const.SQ;
    btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
  }
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    ITooltipButton btn;
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
        btn = (ITooltipButton) buttonList.get(i);
        if (btn.getTooltips() != null) {
          drawHoveringText(btn.getTooltips(), mouseX, mouseY);
        }
        break;// cant hover on 2 at once
      }
    }
    this.renderHoveredToolTip(mouseX, mouseY);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawDefaultBackground();//dim the background as normal
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(background);
    int k = this.guiLeft;
    int l = this.guiTop;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
}
