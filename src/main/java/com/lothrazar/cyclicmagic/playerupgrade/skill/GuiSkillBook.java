package com.lothrazar.cyclicmagic.playerupgrade.skill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.core.GuiButtonItemstack;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

//based on my ancient 2015 spellbook concept 
/// https://github.com/PrinceOfAmber/Cyclic/blob/838b9b669a2d1644077d35a91d997a4d5dca0448/src/main/java/com/lothrazar/cyclicmagic/gui/GuiSpellbook.java
public class GuiSkillBook extends GuiScreen {

  private final static int textureWidth = 200;
  private final static int textureHeight = 180;
  private final static ResourceLocation background = new ResourceLocation(Const.MODID, "textures/gui/spellbook.png");

  private final EntityPlayer player;

  double ang = 0, cx, cy;

  public GuiSkillBook(EntityPlayer p) {
    super();
    this.player = p;
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    if (ClientProxy.keyWheel != null && GameSettings.isKeyDown(ClientProxy.keyWheel)) {
      player.closeScreen();
    }
  }

  @Override
  public void initGui() {
    super.initGui();

  }


  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button instanceof GuiButtonItemstack) {
      //      ModCyclic.network.sendToServer(new PacketSwapPlayerStack(button.id, player.inventory.currentItem));
      player.closeScreen();
    }
  }

  private void drawBackground() {
    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
    int screenWidth = res.getScaledWidth();
    int screenHeight = res.getScaledHeight();
    int guiLeft = screenWidth / 2 - textureWidth / 2;
    int guiTop = screenHeight / 2 - textureHeight / 2 - 20;
    UtilTextureRender.drawTextureSimple(background, guiLeft, guiTop, textureWidth, textureHeight);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    //    ItemStack curHotbar = player.inventory.getStackInSlot(this.player.inventory.currentItem);
    //    if (curHotbar.isEmpty() == false) {
    //      ModCyclic.proxy.renderItemOnScreen(curHotbar, mouseX, mouseY);
    //    }
    drawButtonTooltips(mouseX, mouseY);
  }

  private void drawButtonTooltips(int mouseX, int mouseY) {
    GuiButtonItemstack button;
    List<String> tooltips = new ArrayList<>();
    for (GuiButton b : buttonList) {
      if (b.isMouseOver() && b instanceof GuiButtonItemstack) {
        button = (GuiButtonItemstack) b;
        if (button.getStackRender().isEmpty()) {
          tooltips.add(UtilChat.lang("skillcircle.swap"));
        }
        else {
          tooltips = button.getStackRender().getTooltip(player, ITooltipFlag.TooltipFlags.ADVANCED);
        }
        drawHoveringText(tooltips, mouseX, mouseY);
        break;// cant hover on 2 at once
      }
    }
  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }
}
