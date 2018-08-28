package com.lothrazar.cyclicmagic.playerupgrade.wheel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonItemstack;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilPlayerInventoryFilestorage;
import com.lothrazar.cyclicmagic.net.PacketSwapPlayerStack;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

//based on my ancient 2015 spellbook concept 
/// https://github.com/PrinceOfAmber/Cyclic/blob/838b9b669a2d1644077d35a91d997a4d5dca0448/src/main/java/com/lothrazar/cyclicmagic/gui/GuiSpellbook.java
public class GuiWheel extends GuiScreen {

  private static final int MIN_RADIUS = 26;
  private static final int BTNCOUNT = 18;
  private static final int YOFFSET = 15;
  private final EntityPlayer player;
  // https://github.com/LothrazarMinecraftMods/EnderBook/blob/66363b544fe103d6abf9bcf73f7a4051745ee982/src/main/java/com/lothrazar/enderbook/GuiEnderBook.java
  private int xCenter;
  private int yCenter;
  private int radius;
  private double arc;

  public GuiWheel(EntityPlayer p) {
    super();
    this.player = p;
  }

  @Override
  public void initGui() {
    super.initGui();
    xCenter = this.width / 2;
    yCenter = this.height / 2 - YOFFSET;
    radius = xCenter / 3 + MIN_RADIUS;
    arc = (2 * Math.PI) / BTNCOUNT;
    double ang = 0;
    double cx, cy;
    int id = 0;
    ang = 0;
    GuiButtonItemstack btn;
    for (int i = 0; i < BTNCOUNT; i++) {
      cx = xCenter + radius * Math.cos(ang) - 2;
      cy = yCenter + radius * Math.sin(ang) - 2;
      btn = new GuiButtonItemstack(id++, (int) cx, (int) cy, 20, 20);
      btn.setStackRender(UtilPlayerInventoryFilestorage.getPlayerInventoryStack(player, i).copy());
      this.buttonList.add(btn);
      ang += arc;
    }
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
          ModCyclic.logger.log("itemstack tooltips " + tooltips.size());
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
