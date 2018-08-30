package com.lothrazar.cyclicmagic.playerupgrade.skill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonItemstack;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;

//based on my ancient 2015 spellbook concept 
/// https://github.com/PrinceOfAmber/Cyclic/blob/838b9b669a2d1644077d35a91d997a4d5dca0448/src/main/java/com/lothrazar/cyclicmagic/gui/GuiSpellbook.java
public class GuiSkills extends GuiScreen {

  private static final int MIN_RADIUS = 26;
  private static final int BTNCOUNT = 16;
  private static final int YOFFSET = 15;
  private final EntityPlayer player;
  // https://github.com/LothrazarMinecraftMods/EnderBook/blob/66363b544fe103d6abf9bcf73f7a4051745ee982/src/main/java/com/lothrazar/enderbook/GuiEnderBook.java
  private int xCenter;
  private int yCenter;
  private int radius;
  private double arc;
  double ang = 0, cx, cy;

  public GuiSkills(EntityPlayer p) {
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
    xCenter = this.width / 2;
    yCenter = this.height / 2 - YOFFSET;
    radius = xCenter / 3 + MIN_RADIUS;
    arc = (2 * Math.PI) / BTNCOUNT;
    ang = Math.PI;

    for (int i = 10; i < 18; i++) {
      addStackButton(i);
    }
    for (int i = 26; i > 18; i--) {
      addStackButton(i);
    }
    radius -= 36;
    for (int i = 34; i > 26; i--) {
      addStackButton(i);
    }
    for (int i = 42; i > 34; i--) {
      addStackButton(i);
    }
  }

  private void addStackButton(int slot) {
    GuiButtonItemstack btn;
    cx = xCenter + radius * Math.cos(ang) - 2;
    cy = yCenter + radius * Math.sin(ang) - 2;
    btn = new GuiButtonItemstack(slot, (int) cx, (int) cy);
    ModCyclic.logger.log("SKILLS WIP ");
    //    btn.setStackRender(UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, slot).copy());
    this.buttonList.add(btn);
    ang += arc;
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button instanceof GuiButtonItemstack) {
      //      ModCyclic.network.sendToServer(new PacketSwapPlayerStack(button.id, player.inventory.currentItem));
      player.closeScreen();
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
