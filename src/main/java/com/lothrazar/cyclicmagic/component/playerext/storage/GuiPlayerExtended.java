package com.lothrazar.cyclicmagic.component.playerext.storage;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiStats;
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
    try {
      ((ContainerPlayerExtended) inventorySlots).inventory.blockEvents = false;
    }
    catch (Exception e) {}
    this.updateActivePotionEffects();
  }
  @Override
  public void initGui() {
    this.buttonList.clear();
    super.initGui();
  } 
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(background);
    int k = this.guiLeft;
    int l = this.guiTop;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }
  @Override
  protected void actionPerformed(GuiButton button) {
    //    if (button.id == 0) {
    //      this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.player.getStatFileWriter()));
    //    }
    if (button.id == 1) {
      this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
    }
  }
}
