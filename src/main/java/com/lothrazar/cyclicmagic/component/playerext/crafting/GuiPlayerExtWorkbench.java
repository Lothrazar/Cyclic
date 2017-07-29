package com.lothrazar.cyclicmagic.component.playerext.crafting;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.ResourceLocation;

public class GuiPlayerExtWorkbench extends InventoryEffectRenderer {
  public static final ResourceLocation background = new ResourceLocation(Const.MODID, "textures/gui/inventorycraft.png");
  private float oldMouseX;
  private float oldMouseY;
  public GuiPlayerExtWorkbench(ContainerPlayerExtWorkbench ctr) {
    super(ctr);
    this.allowUserInput = true;
  }
  @Override
  public void updateScreen() {
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
    this.oldMouseX = (float) mouseX;
    this.oldMouseY = (float) mouseY;
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(background);
    int i = this.guiLeft;
    int j = this.guiTop;
    this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    //COPIED FROM GuiInventory
    GuiInventory.drawEntityOnScreen(i + 51, j + 75, 30, (float) (i + 51) - this.oldMouseX, (float) (j + 75 - 50) - this.oldMouseY, this.mc.player);
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
