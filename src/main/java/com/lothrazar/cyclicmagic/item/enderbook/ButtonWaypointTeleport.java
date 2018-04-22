/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.enderbook;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonWaypointTeleport extends GuiButton implements ITooltipButton {

  private int bookSlot;
  List<String> tooltips = new ArrayList<String>();

  public int getSlot() {
    return bookSlot;
  }

  public ButtonWaypointTeleport(int id, int x, int y, int w, int h, String txt, int slot) {
    super(id, x, y, w, h, txt);
    bookSlot = slot;
  }

  @Override
  public List<String> getTooltips() {
    return tooltips;
  }

  public void addTooltipLine(String s) {
    tooltips.add(s);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      EntityPlayer player = mc.player;
      World world = player.getEntityWorld();
      UtilSound.playSound(player, player.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
      // only spawn particles if they have enough xp
      UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL, player.getPosition());
      UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL, player.getPosition().up());
      // but even if they dont, send packet anyway. server side has the real
      // source of truth
      ModCyclic.network.sendToServer(new PacketWarpButton(bookSlot));
      // we would have to wait until tp finishes and then sendToClient in a new
      // 'particle packet' for this
      // particleAtPlayer(world,mc.thePlayer);
    }
    return pressed;
  }
}
