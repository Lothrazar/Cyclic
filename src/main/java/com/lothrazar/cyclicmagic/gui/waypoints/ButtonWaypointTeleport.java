package com.lothrazar.cyclicmagic.gui.waypoints;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketWarpButton;
import com.lothrazar.cyclicmagic.util.UtilParticle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonWaypointTeleport extends GuiButton {
	private int bookSlot;

	public int getSlot() {
		return bookSlot;
	}

	public ButtonWaypointTeleport(int id, int x, int y, int w, int h, String txt, int slot) {
		super(id, x, y, w, h, txt);
		bookSlot = slot;
	}

	private String tooltip = null;

	public void setTooltip(String s) {
		tooltip = s;
	}

	public String getTooltip() {
		return tooltip;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if (pressed) {
			World world = mc.thePlayer.worldObj;

			// only spawn particles if they have enough xp
			UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL,  mc.thePlayer.getPosition());
			UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL,  mc.thePlayer.getPosition().up());
		 
			// but even if they dont, send packet anyway. server side has the real
			// source of truth

			ModMain.network.sendToServer(new PacketWarpButton(bookSlot));

			// we would have to wait until tp finishes and then sendToClient in a new
			// 'particle packet' for this
			// particleAtPlayer(world,mc.thePlayer);
		}

		return pressed;
	}
}
