package com.lothrazar.cyclicmagic.playerupgrade.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonItemstack;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilPlayerInventoryFilestorage;
import com.lothrazar.cyclicmagic.net.PacketSwapPlayerStack;
import com.lothrazar.cyclicmagic.proxy.ClientProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

//based on my ancient 2015 spellbook concept 
/// https://github.com/PrinceOfAmber/Cyclic/blob/838b9b669a2d1644077d35a91d997a4d5dca0448/src/main/java/com/lothrazar/cyclicmagic/gui/GuiSpellbook.java
public class GuiTools extends GuiScreen {

  private static final int MIN_RADIUS = 26;
  private static final int BTNCOUNT = 16;
  private static final int YOFFSET = 15;
  private final EntityPlayer player;
  // https://github.com/LothrazarMinecraftMods/EnderBook/blob/66363b544fe103d6abf9bcf73f7a4051745ee982/src/main/java/com/lothrazar/enderbook/GuiEnderBook.java
  private int xCenter;
  private int yCenter;

  public GuiTools(EntityPlayer p) {
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
    int vspace = 22, hspace = 22;
    int myWidth = (hspace - 1) * 9;
    int leftStart = xCenter - myWidth / 2;
    int cx = leftStart;// full width is 
    int cy = yCenter - 50;
    for (int i = 9; i < 18; i++) {
      cx += hspace;
      addStackButton(i, cx, cy);
    }
    cx = leftStart;
    cy += vspace;
    for (int i = 18; i < 27; i++) {
      cx += hspace;
      addStackButton(i, cx, cy);
    }
    cx = leftStart;
    cy += vspace;
    for (int i = 27; i < 36; i++) {
      cx += hspace;
      addStackButton(i, cx, cy);
    }
    cx = leftStart;
    cy += vspace;
    for (int i = 36; i < 45; i++) {
      cx += hspace;
      addStackButton(i, cx, cy);
    }
  }

  private void addStackButton(int slot, int cx, int cy) {
    GuiButtonItemstack btn;
    btn = new GuiButtonItemstack(slot, cx, cy);
    btn.setStackRender(UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, slot).copy());
    this.buttonList.add(btn);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button instanceof GuiButtonItemstack) {
      ModCyclic.network.sendToServer(new PacketSwapPlayerStack(button.id, player.inventory.currentItem));
      player.closeScreen();
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    ItemStack curHotbar = player.inventory.getStackInSlot(this.player.inventory.currentItem);
    if (curHotbar.isEmpty() == false) {
      ModCyclic.proxy.renderItemOnScreen(curHotbar, mouseX, mouseY);
    }
    drawButtonTooltips(mouseX, mouseY);
  }

  private void drawButtonTooltips(int mouseX, int mouseY) {
    GuiButtonItemstack button;
    List<String> tooltips = new ArrayList<>();
    for (GuiButton b : buttonList) {
      if (b.isMouseOver() && b instanceof GuiButtonItemstack) {
        button = (GuiButtonItemstack) b;
        if (button.getStackRender().isEmpty()) {
          tooltips.add(UtilChat.lang("toolcircle.swap"));
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
